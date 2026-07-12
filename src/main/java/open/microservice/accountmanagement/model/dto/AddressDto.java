package open.microservice.accountmanagement.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDto {
    private String id;
    private String homeNumber;
    private String province;
    private String accountNo;
}
