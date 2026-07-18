package open.microservice.accountmanagement.model.exception;

import lombok.Getter;
import open.microservice.accountmanagement.model.om.ErrorModel;

import java.util.List;

@Getter
public abstract class BaseException extends RuntimeException {
    private final List<ErrorModel> errorDetails;
    private final String errorDetail;

    protected BaseException(String message) {
        super(message);
        this.errorDetails = null;
        this.errorDetail = null;
    }

    protected BaseException(String message, List<ErrorModel> errorDetails) {
        super(message);
        this.errorDetails = errorDetails;
        this.errorDetail = null;
    }

    protected BaseException(String message, List<ErrorModel> errorDetails, String errorDetail) {
        super(message);
        this.errorDetails = errorDetails;
        this.errorDetail = errorDetail;
    }

}