package open.microservice.accountmanagement.model.response.interal;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponseException(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        Object errorDetails,
        Object errorDetail,
        String path
) {
}