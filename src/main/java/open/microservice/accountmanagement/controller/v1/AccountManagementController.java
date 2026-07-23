package open.microservice.accountmanagement.controller.v1;



import open.microservice.accountmanagement.model.hibernate.OrderPropertyInformation;
import open.microservice.accountmanagement.model.request.internal.AccountRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static open.microservice.accountmanagement.constant.APIConstant.*;

@RestController
@RequestMapping(SERVICE_PREFIX_V1)
public class AccountManagementController {
    @Autowired
    CreateAccountWorkFlow createAccountWorkFlow;

    @PostMapping(value = REGISTRY)
    public ResponseEntity<?> createNewAccount(@RequestBody AccountRequest requestBody) throws Exception{
        createAccountWorkFlow.validateRequest(requestBody);

        OrderPropertyInformation orderProperty = createAccountWorkFlow.buildOrderProperty(requestBody);

        createAccountWorkFlow.composeExOrderAndParam(orderProperty);

        createAccountWorkFlow.saveOrderAndExOrder(orderProperty.getOrder());

        createAccountWorkFlow.provisioning(orderProperty.getOrder());

        return createAccountWorkFlow.composeResponse(orderProperty);
    }

}
