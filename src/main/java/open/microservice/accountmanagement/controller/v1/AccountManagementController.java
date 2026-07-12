package open.microservice.accountmanagement.controller.v1;



import open.microservice.accountmanagement.controller.CreateAccountHelper;
import open.microservice.accountmanagement.model.hibernate.OrderPropertyInformation;
import open.microservice.accountmanagement.model.request.AccountRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static open.microservice.accountmanagement.constant.APIConstant.*;

@RestController
@RequestMapping(SERVICE_PREFIX_V1)
public class AccountManagementController {
    @Autowired
    CreateAccountHelper createAccountHelper;

    @PostMapping(value = REGISTRY)
    public ResponseEntity<?> createNewAccount(@RequestBody AccountRequest requestBody) throws Exception {
        createAccountHelper.validateRequest(requestBody);
        OrderPropertyInformation orderProperty = createAccountHelper.buildOrderProperty(requestBody);
        createAccountHelper.composeExOrderAndParam(orderProperty);
        createAccountHelper.saveOrderAndExOrder(orderProperty.getOrder());
        createAccountHelper.provisioning(orderProperty.getOrder());
        ResponseEntity<?> responseBody = createAccountHelper.composeResponse(orderProperty, requestBody);
        return responseBody;
    }

//    @PatchMapping(value = MODIFY)
//    public ResponseEntity<?> modifyProfile(@RequestBody AccountRequest requestBody) {
//        return ResponseEntity.status(HttpStatus.OK).build();
//    }

//    @PostMapping(value = RESEND)
//    public ResponseEntity<?> resendExternalOrder(@RequestBody AccountRequest requestBody) throws Exception {
//        ResponseEntity<?> responseBody = createAccountHelper.composeResponse(new OrderBufferModel(), requestBody);
//        return responseBody;
//    }

}
