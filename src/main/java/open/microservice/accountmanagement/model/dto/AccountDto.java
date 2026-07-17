package open.microservice.accountmanagement.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDto {
    private String id;
    private String accountNo;
    private String accountName;
    private String createDate;
    private String createBy;
    private String lastUpdatedDate;
    private String lastUpdateBy;
    private AddressDto address;
}