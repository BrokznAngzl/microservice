package open.microservice.accountmanagement.model.exception;

import lombok.Getter;
import open.microservice.accountmanagement.model.om.ErrorModel;

import java.util.List;

@Getter
public class ValidateFailedException extends BaseException {

    public ValidateFailedException(String message) {
        super(message);
    }

    public ValidateFailedException(String message, List<ErrorModel> errorDetails) {
        super(message, errorDetails);
    }
}
