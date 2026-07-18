package open.microservice.accountmanagement.model.request.pf;


import lombok.Getter;
import lombok.Setter;
import open.microservice.accountmanagement.model.hibernate.pf.Account;
import open.microservice.accountmanagement.model.hibernate.pf.Address;

@Getter
@Setter
public class ProfileRequestParam {
    private Account account;
    private Address address;

    public ProfileRequestParam(Account account, Address address) {
        this.account = account;
        this.address = address;
    }
}
