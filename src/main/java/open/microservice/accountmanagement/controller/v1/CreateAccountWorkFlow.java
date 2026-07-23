package open.microservice.accountmanagement.controller.v1;


import lombok.extern.log4j.Log4j2;
import open.microservice.accountmanagement.controller.IWorkFlow;
import open.microservice.accountmanagement.model.dto.AccountDto;
import open.microservice.accountmanagement.model.exception.ComposeFailedException;
import open.microservice.accountmanagement.model.exception.ProvisioningFailedException;
import open.microservice.accountmanagement.model.exception.ResourceNotFoundException;
import open.microservice.accountmanagement.model.exception.ValidateFailedException;
import open.microservice.accountmanagement.model.hibernate.OrderPropertyInformation;
import open.microservice.accountmanagement.model.hibernate.om.Condition;
import open.microservice.accountmanagement.model.hibernate.om.External;
import open.microservice.accountmanagement.model.hibernate.om.ExternalOrder;
import open.microservice.accountmanagement.model.hibernate.om.Order;
import open.microservice.accountmanagement.model.om.ErrorModel;
import open.microservice.accountmanagement.model.request.internal.AccountRequest;
import open.microservice.accountmanagement.model.request.internal.Phone;
import open.microservice.accountmanagement.model.response.interal.ResponseModel;
import open.microservice.accountmanagement.povisioner.IProvisioner;
import open.microservice.accountmanagement.repository.om.ExternalOrderRepository;
import open.microservice.accountmanagement.repository.om.OrderRepository;
import open.microservice.accountmanagement.service.IComposer;
import open.microservice.accountmanagement.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static open.microservice.accountmanagement.constant.APIConstant.CREATE_ACCOUNT;
import static open.microservice.accountmanagement.constant.ErrorConstant.*;
import static open.microservice.accountmanagement.constant.ParameterConstance.PHONE_NUMBER;
import static open.microservice.accountmanagement.constant.StatusConstant.*;


@Component
@Log4j2
public class CreateAccountWorkFlow implements IWorkFlow<AccountRequest> {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private ValidateUtil validateUtil;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ExternalOrderRepository exOrderRepository;
    @Autowired
    private CacheUtil cacheUtil;
    @Autowired
    private List<IComposer> composers;
    @Autowired
    private List<IProvisioner> provisioners;

    @Override
    public void validateRequest(AccountRequest request) {
        List<ErrorModel> errorList = new ArrayList<>();
        validateRequiredParameter(request, errorList);
        validateValue(request, errorList);
    }

    private void validateRequiredParameter(AccountRequest request, List<ErrorModel> errorList) {

        validateUtil.validateMandatory(request.getRequestId(), errorList, "requestId");
        validateUtil.validateMandatory(request.getAccountName(), errorList, "accountName");

        if (ObjectUtil.isNotEmpty(request.getPhone())) {
            Phone phone = request.getPhone();
            if (StringUtil.isEmpty(phone.getPhoneNumber())) {
                validateUtil.addErrorNotFound(errorList, PHONE_NUMBER);
            }
            if (StringUtil.isEmpty(phone.getPhoneType())) {
                validateUtil.addErrorNotFound(errorList, PHONE_NUMBER);
            }
        }

        if (!errorList.isEmpty()) {
            throw new ValidateFailedException(VALIDATE_FAILED, errorList);
        }
    }

    private void validateValue(AccountRequest request, List<ErrorModel> errorList) {
        if (ObjectUtil.isNotEmpty(request.getPhone())) {
            validateUtil.validatePhone(request.getPhone(), errorList);
        }

        if (!errorList.isEmpty()) {
            throw new ValidateFailedException(VALIDATE_FAILED, errorList);
        }
    }

    @Override
    @Transactional(timeout = 30, transactionManager = "omTransactionManager", rollbackFor = Exception.class)
    public void saveOrderAndExOrder(Order order) {
        exOrderRepository.saveAll(order.getExternalOrder());
        orderRepository.save(order);
    }

    @Override
    public ResponseEntity<?> composeResponse(OrderPropertyInformation orderProperty) {
        AccountDto profile = orderProperty.getOrderItem().getAccount();
        ResponseModel responseModel = new ResponseModel();
        responseModel.setAccountNo(profile.getAccountNo());
        responseModel.setAccountName(profile.getAccountName());

        Order order = orderProperty.getOrder();
        order.setResponse(mapper.writeValueAsString(responseModel));
        orderRepository.save(order);

        return ResponseEntity.status(HttpStatus.OK.value()).body(responseModel);
    }

    @Override
    public void composeExOrderAndParam(OrderPropertyInformation orderProperty) {
        Order order = orderProperty.getOrder();
        if (ObjectUtil.isNotEmpty(order.getExternalOrder())) {
            for (ExternalOrder exOrder : order.getExternalOrder()) {
                IComposer composer = composers.stream()
                        .filter(c -> c.canCompose(exOrder.getExternalId()))
                        .findFirst()
                        .orElseThrow(() -> new ComposeFailedException(COMPOSE_FAILED, StringUtil.format(COMPOSER_NOT_FOUND_DETAIL, exOrder.getExternalId())));
                composer.compose(orderProperty, exOrder, order);
            }

        } else {
            log.error("has no external order to compose");
            throw new ComposeFailedException(COMPOSE_FAILED, StringUtil.format(EXTERNAL_ORDER_NOT_FOUND_DETAIL, order.getId()));
        }
    }

    @Override
    public void provisioning(Order order) {

        List<ExternalOrder> exOrders = order.getExternalOrder();
        if (ObjectUtil.isNotEmpty(exOrders)) {

            try {
                for (ExternalOrder exOrder : exOrders) {
                    IProvisioner provisioner = provisioners.stream()
                            .filter(p -> p.canProvisioning(exOrder.getExternalNode()))
                            .findFirst()
                            .orElseThrow(() -> new ProvisioningFailedException(PROVISIONING_FAILED,
                                    StringUtil.format(PROVISIONER_NOT_FOUND_DETAIL, exOrder.getExternalNode())));
                    provisioner.provisioning(exOrder);
                }

                boolean anyTaskFailed = exOrders.stream().anyMatch(exOrder -> !StringUtil.equals(exOrder.getStatus(), COMPLETED));
                if (anyTaskFailed) {
                    log.error("found failed task in order: {}", order.getId());
                    throw new ProvisioningFailedException(PROVISIONING_FAILED, StringUtil.format(PROVISIONING_FAILED_DETAIL, "Order", order.getId()));
                } else {
                    setOrderLastUpdate(order, COMPLETED);
                    orderRepository.save(order);
                }

            } catch (ProvisioningFailedException e) {
                setOrderLastUpdate(order, FAILED);
                orderRepository.save(order);

                throw e;
            } catch (Exception e) {
                setOrderLastUpdate(order, FAILED);
                orderRepository.save(order);

                throw new ProvisioningFailedException(PROVISIONING_FAILED, StringUtil.format(PROVISIONING_FAILED_DETAIL, "Order", order.getId()));

            }


        } else {
            log.error("order does not have any tasks to provision");
        }
    }

    public OrderPropertyInformation buildOrderProperty(AccountRequest request) {
        OrderPropertyInformation orderProperty = new OrderPropertyInformation();
        Order order = new Order();
        order.setId(IdGeneratorUtil.generateId(1000, 10000)); // 1000 - 9999
        String requestInfo = mapper.writeValueAsString(request);
        order.setRequest(requestInfo);
        order.setStatus(PENDING);
        order.setCreatedDate(DateUtil.getCurrentLocalDateTime());
        order.setCreatedBy("SOOD LORE");

        orderProperty.setRequestInfo(requestInfo);

        String action = request.getAction();
        String apiName = CREATE_ACCOUNT;
        String composeKey = action + "|" + apiName;

        order.setExternalOrder(getExternalOrder(orderProperty, composeKey, order.getId()));
        orderProperty.setOrder(order);
        return orderProperty;
    }

    private List<ExternalOrder> getExternalOrder(OrderPropertyInformation orderProperty, String composeKey, String orderId) {
        String jsonModel = mapper.writeValueAsString(orderProperty);

        List<External> externals = cacheUtil.getExternal(composeKey);
        externals.sort(Comparator.comparingInt(External::getComposeSequence));
        List<ExternalOrder> externalOrders = new ArrayList<>();

        if (ObjectUtil.isNotEmpty(externals)) {
            ConditionUtil conditionUtil = new ConditionUtil(jsonModel);

            int exOrderSeq = 1;
            for (External external : externals) {
                Condition condition = external.getCondition();
                if (ObjectUtil.isEmpty(condition)) {
                    externalOrders.add(getDraftExternalOrder(external, orderId, exOrderSeq));
                    exOrderSeq++;
                } else {
                    if (conditionUtil.activeCondition(condition)) {
                        externalOrders.add(getDraftExternalOrder(external, orderId, exOrderSeq));
                        exOrderSeq++;
                    } else {
                        log.info("Condition is not active for external: {}", external.getId());
                    }
                }
            }
        } else {
            throw new ResourceNotFoundException(StringUtil.format(RESOURCE_NOT_FOUND_DETAIL, "External", composeKey));
        }

        return externalOrders;
    }

    private ExternalOrder getDraftExternalOrder(External external, String orderId, int exOrderSeq) {
        ExternalOrder externalOrder = new ExternalOrder();
        externalOrder.setId(String.format("%s-%03d", orderId, exOrderSeq));
        externalOrder.setExternalId(external.getId());
        externalOrder.setOrderName(external.getOrderName());
        externalOrder.setExternalNode(external.getExternalNode());
        log.info("external order draft: {}", externalOrder.getOrderName());
        return externalOrder;
    }

    public void setOrderLastUpdate(Order order, String status) {
        order.setLastUpdatedDate(DateUtil.getCurrentLocalDateTime());
        order.setLastUpdatedBy("SOOD LORE");
        order.setStatus(status);
    }
}

