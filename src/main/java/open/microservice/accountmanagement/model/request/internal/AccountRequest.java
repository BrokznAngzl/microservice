package open.microservice.accountmanagement.model.request.internal;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import open.microservice.accountmanagement.model.dto.AddressDto;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountRequest extends Request {
    private String action;
    private String accountName;
    private String orderReason;
    private Phone phone;
    private AddressDto address;
}
