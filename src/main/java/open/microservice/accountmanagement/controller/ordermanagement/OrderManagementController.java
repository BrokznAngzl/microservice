package open.microservice.accountmanagement.controller.ordermanagement;

import open.microservice.accountmanagement.model.request.internal.OrderFilter;
import open.microservice.accountmanagement.service.om.OrderManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static open.microservice.accountmanagement.constant.APIConstant.*;

@RestController
@RequestMapping(SERVICE_PREFIX_V1)
public class OrderManagementController {

    @Autowired
    private OrderManagementService orderManagementService;

    @PutMapping(value = RE_PROVISIOING + "/{orderId}")
    public ResponseEntity<?> reProvisioningExternalOrder(@PathVariable String orderId) {
        return orderManagementService.reProvisioningExternalOrder(orderId);
    }

    @GetMapping(value = ORDERS)
    public ResponseEntity<?> getOrders(OrderFilter filter) {
        return orderManagementService.getOrdersData(filter);
    }
}

