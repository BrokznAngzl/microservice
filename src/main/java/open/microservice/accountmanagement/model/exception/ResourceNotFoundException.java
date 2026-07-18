package open.microservice.accountmanagement.model.exception;

import lombok.Getter;
import open.microservice.accountmanagement.model.om.ErrorModel;

import java.util.List;

@Getter
public class ResourceNotFoundException extends BaseException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, List<ErrorModel> errorDetails) {
        super(message, errorDetails);
    }

    public ResourceNotFoundException(String message, String errorDetail) {
        super(message, null, errorDetail);
    }
}
