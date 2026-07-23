package open.microservice.accountmanagement.service.om;

import lombok.extern.log4j.Log4j2;
import open.microservice.accountmanagement.model.dto.AccountDto;
import open.microservice.accountmanagement.model.exception.ProvisioningFailedException;
import open.microservice.accountmanagement.model.exception.ResourceNotFoundException;
import open.microservice.accountmanagement.model.hibernate.om.ExternalOrder;
import open.microservice.accountmanagement.model.hibernate.om.Order;
import open.microservice.accountmanagement.model.hibernate.pf.Account;
import open.microservice.accountmanagement.model.request.pf.ProfileRequestParam;
import open.microservice.accountmanagement.model.response.interal.ResponseModel;
import open.microservice.accountmanagement.povisioner.IProvisioner;
import open.microservice.accountmanagement.repository.om.OrderRepository;
import open.microservice.accountmanagement.util.DateUtil;
import open.microservice.accountmanagement.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;

import static open.microservice.accountmanagement.constant.ErrorConstant.*;
import static open.microservice.accountmanagement.constant.ExternalNodeConstant.PF;
import static open.microservice.accountmanagement.constant.StatusConstant.COMPLETED;
import static open.microservice.accountmanagement.constant.StatusConstant.FAILED;

@Log4j2
@Service
public class OrderManagementService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private List<IProvisioner> provisioners;
    @Autowired
    private ObjectMapper mapper;

    public ResponseEntity<?> reProvisioningExternalOrder(String orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);

        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            List<ExternalOrder> externalOrders = order.getExternalOrder();
            List<ExternalOrder> reProviseExOrder = externalOrders.stream().filter(exOrder -> !StringUtil.equals(exOrder.getStatus(), COMPLETED)).toList();

            try {
                for (ExternalOrder exOrder : reProviseExOrder) {
                    IProvisioner provisioner = provisioners.stream()
                            .filter(p -> p.canProvisioning(exOrder.getExternalNode()))
                            .findFirst()
                            .orElseThrow(() -> new ProvisioningFailedException(PROVISIONING_FAILED,
                                    StringUtil.format(PROVISIONER_NOT_FOUND_DETAIL, exOrder.getExternalNode())));
                    provisioner.provisioning(exOrder);
                }

                boolean anyTaskFailed = externalOrders.stream().anyMatch(exOrder -> !StringUtil.equals(exOrder.getStatus(), COMPLETED));
                if (anyTaskFailed) {
                    log.error("found failed task in order: {}", order.getId());
                    throw new ProvisioningFailedException(PROVISIONING_FAILED, StringUtil.format(PROVISIONING_FAILED_DETAIL, "Order", order.getId()));

                } else {
                    setOrderLastUpdate(order, COMPLETED);
                    ResponseEntity<?> response = composeResponse(order);
                    orderRepository.save(order);
                    return response;
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
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND, StringUtil.format(RESOURCE_NOT_FOUND_DETAIL, "Order", orderId));
        }
    }

    private ResponseEntity<?> composeResponse(Order order) {

        ExternalOrder profileOrder = order.getExternalOrder()
                .stream()
                .filter(exOrder -> StringUtil.equals(exOrder.getExternalNode(), PF))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NOT_FOUND, StringUtil.format(RESOURCE_NOT_FOUND_DETAIL, "ExternalOrder", PF)));

        ProfileRequestParam profileRequest = mapper.readValue(profileOrder.getRequestInfo(), ProfileRequestParam.class);
        Account profile = profileRequest.getAccount();

        ResponseModel responseModel = new ResponseModel();
        responseModel.setAccountNo(profile.getAccountNo());
        responseModel.setAccountName(profile.getAccountName());

        order.setResponse(mapper.writeValueAsString(responseModel));

        return ResponseEntity.status(HttpStatus.OK.value()).body(responseModel);
    }


    public void setOrderLastUpdate(Order order, String status) {
        order.setLastUpdatedDate(DateUtil.getCurrentLocalDateTime());
        order.setLastUpdatedBy("SOOD LORE");
        order.setStatus(status);
    }

}

