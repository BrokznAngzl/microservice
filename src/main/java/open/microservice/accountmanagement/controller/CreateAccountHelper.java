package open.microservice.accountmanagement.controller;


import lombok.extern.log4j.Log4j2;
import open.microservice.accountmanagement.model.hibernate.OrderPropertyInformation;
import open.microservice.accountmanagement.model.hibernate.om.Condition;
import open.microservice.accountmanagement.model.hibernate.om.External;
import open.microservice.accountmanagement.model.hibernate.om.ExternalOrder;
import open.microservice.accountmanagement.model.hibernate.om.Order;
import open.microservice.accountmanagement.model.request.AccountRequest;
import open.microservice.accountmanagement.model.om.ErrorModel;
import open.microservice.accountmanagement.model.request.Phone;
import open.microservice.accountmanagement.repository.om.ExternalOrderRepository;
import open.microservice.accountmanagement.repository.om.OrderRepository;
import open.microservice.accountmanagement.service.IComposer;
import open.microservice.accountmanagement.util.*;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import static open.microservice.accountmanagement.constant.APIConstant.CREATE_ACCOUNT;
import static open.microservice.accountmanagement.constant.ParameterConstance.PHONE_NUMBER;


@Component
@Log4j2
public class CreateAccountHelper implements IControllerHelper<AccountRequest> {
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

    @Override
    public void validateRequest(AccountRequest request) throws Exception {
        List<ErrorModel> errorList = new ArrayList<>();
        validateRequiredParameter(request, errorList);
        validateValue(request, errorList);
    }

    private void validateRequiredParameter(AccountRequest request, List<ErrorModel> errorList) throws Exception {

        validateUtil.validateMandatory(request.getPublicId(), errorList, "publicId");
        validateUtil.validateMandatory(request.getPrivateId(), errorList, "privateId");
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
            throw new BadRequestException("Validation failed\n" + errorList);
        }
    }

    private void validateValue(AccountRequest request, List<ErrorModel> errorList) throws BadRequestException {
        if (ObjectUtil.isNotEmpty(request.getPhone())) {
            validateUtil.validatePhone(request.getPhone(), errorList);
        }

        if (!errorList.isEmpty()) {
            throw new BadRequestException("Validation failed\n" + errorList);
        }
    }

    @Override
    @Transactional(timeout = 60, transactionManager = "omTransactionManager", rollbackFor = Exception.class)
    public void saveOrderAndExOrder(Order order) {
        orderRepository.save(order);
        exOrderRepository.saveAll(order.getExternalOrders());
    }

    @Override
    public ResponseEntity<?> composeResponse(OrderPropertyInformation orderProperty, AccountRequest request) throws Exception {
//        Account profile = obm.getProfileRequestParam().getAccount();
//        ResponseModel responseModel = new ResponseModel();
//        responseModel.setPublicId(request.getPublicId());
//        responseModel.setAccountNo(profile.getAccountNo());
        return ResponseEntity.status(HttpStatus.OK.value()).body(null);
    }

    @Override
    public void composeExOrderAndParam(OrderPropertyInformation orderProperty) throws Exception {
        Order order = orderProperty.getOrder();
        if (ObjectUtil.isNotEmpty(order.getExternalOrders())) {
            for (ExternalOrder exOrder : order.getExternalOrders()) {
                IComposer composer = composers.stream()
                        .filter(c -> c.canCompose(exOrder.getComposeId()))
                        .findFirst()
                        .orElseThrow(() -> new Exception("No composer found for composeId: " + exOrder.getComposeId()));
                composer.compose(orderProperty, exOrder, order);
            }
        } else {
            log.error("has no external order to compose");
            throw new Exception("has no external order to compose");
        }
    }

    @Override
    public void provisioning(Order order) throws Exception {
//        if (ObjectUtil.isNotEmpty(order.getExternalOrders())) {
//            for (ExternalOrder externalOrder : order.getExternalOrders()) {
//                IProvisioner provisioner = provisioners.stream().filter(p -> p.canProvisioning("")).findFirst().get();
//                provisioner.provisioning(externalOrder);
//            }
//        } else {
//            log.error("order does not have any tasks to provision");
//        }
    }

    public OrderPropertyInformation buildOrderProperty(AccountRequest request) {
        OrderPropertyInformation orderProperty = new OrderPropertyInformation();
        Order order = new Order();
        order.setRequest(mapper.writeValueAsString(request));
        order.setExternalOrders(getExternalOrder(request));
        orderProperty.setOrder(order);
        return orderProperty;
    }

    private List<ExternalOrder> getExternalOrder(AccountRequest request) {
        String jsonModel = mapper.writeValueAsString(request);
        String action = request.getAction();
        String apiName = CREATE_ACCOUNT;
        String key = action + "|" + apiName;

        List<External> externals = cacheUtil.getExternal(key);
        List<ExternalOrder> externalOrders = new ArrayList<>();

        if (ObjectUtil.isNotEmpty(externals)) {
            ConditionUtil conditionUtil = new ConditionUtil(jsonModel);

            for (External external : externals) {
                Condition condition = external.getCondition();
                if (ObjectUtil.isEmpty(condition)) {
                    externalOrders.add(getDraftExternalOrder(external));
                } else {
                    if (conditionUtil.activeCondition(condition)) {
                        externalOrders.add(getDraftExternalOrder(external));
                    } else {
                        log.info("Condition is not active for external: {}", external.getId());
                    }
                }
            }
        } else {
            throw new RuntimeException("External not found for key: " + key);
        }

        return externalOrders;
    }

    private ExternalOrder getDraftExternalOrder(External external) {
        ExternalOrder externalOrder = new ExternalOrder();
        externalOrder.setComposeId(String.valueOf(external.getId()));
        externalOrder.setOrderName(external.getOrderName());
        externalOrder.setExternalNode(external.getExternalNode());
        externalOrder.setCreatedBy("SOOD LORE");
//        externalOrder.setEndpoint();
        log.info("External order draft: {}", externalOrder.getOrderName());
        return externalOrder;
    }
}

