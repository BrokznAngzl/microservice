package open.microservice.accountmanagement.model.request;


import lombok.Getter;
import lombok.Setter;
import open.microservice.accountmanagement.model.dto.AddressDto;

@Getter
@Setter
public class AccountRequest extends Request {
    private String action;
    private String accountName;
    private String orderReason;
    private Phone phone;
    private AddressDto address;
}
