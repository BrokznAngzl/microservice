package open.microservice.accountmanagement.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDto {
    private String id;
    private String accountNo;
    private String createDate;
    private AddressDto address;
}