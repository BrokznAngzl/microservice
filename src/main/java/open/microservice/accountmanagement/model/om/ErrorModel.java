package open.microservice.accountmanagement.model.om;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorModel {
    private String id;
    private String statement;
}
