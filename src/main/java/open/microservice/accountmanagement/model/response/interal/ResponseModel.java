package open.microservice.accountmanagement.model.response.interal;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

// field sequence
@JsonPropertyOrder({
        "accountNo",
        "accountName"
})
@Setter
@Getter
public class ResponseModel {
    private String accountNo;
    private String accountName;
}
