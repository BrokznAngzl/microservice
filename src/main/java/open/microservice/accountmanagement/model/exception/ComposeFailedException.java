package open.microservice.accountmanagement.model.exception;

import lombok.Getter;
import open.microservice.accountmanagement.model.om.ErrorModel;

import java.util.List;

@Getter
public class ComposeFailedException extends BaseException {

    public ComposeFailedException(String message, List<ErrorModel> errorDetails) {
        super(message, errorDetails);
    }

    public ComposeFailedException(String message, String errorDetail) {
        super(message, null, errorDetail);
    }
}
