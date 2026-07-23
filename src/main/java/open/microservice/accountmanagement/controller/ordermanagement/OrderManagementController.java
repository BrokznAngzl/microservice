package open.microservice.accountmanagement.controller.ordermanagement;

import open.microservice.accountmanagement.service.om.OrderManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static open.microservice.accountmanagement.constant.APIConstant.RE_PROVISIOING;
import static open.microservice.accountmanagement.constant.APIConstant.SERVICE_PREFIX_V1;

@RestController
@RequestMapping(SERVICE_PREFIX_V1)
public class OrderManagementController {

    @Autowired
    private OrderManagementService orderManagementService;

    @PutMapping(value = RE_PROVISIOING + "/{orderId}")
    public ResponseEntity<?> reProvisioningExternalOrder(@PathVariable String orderId) {
        return orderManagementService.reProvisioningExternalOrder(orderId);
    }
}
