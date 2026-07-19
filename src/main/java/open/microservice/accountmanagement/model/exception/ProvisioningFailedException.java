package open.microservice.accountmanagement.model.exception;

import lombok.Getter;
import open.microservice.accountmanagement.model.om.ErrorModel;

import java.util.List;

@Getter
public class ProvisioningFailedException extends BaseException {

    public ProvisioningFailedException(String message, List<ErrorModel> errorDetails) {
        super(message, errorDetails);
    }

    public ProvisioningFailedException(String message, String errorDetail) {
        super(message, null, errorDetail);
    }
}
