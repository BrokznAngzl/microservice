package open.microservice.accountmanagement.model.hibernate;

import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.Getter;
import lombok.Setter;
import open.microservice.accountmanagement.model.dto.AccountDto;
import open.microservice.accountmanagement.model.dto.OrderItem;
import open.microservice.accountmanagement.model.hibernate.om.Order;
import open.microservice.accountmanagement.model.request.AccountRequest;
import tools.jackson.databind.ObjectMapper;

@Getter
@Setter
public class OrderPropertyInformation {
    private Order order;
    @JsonRawValue
    private String requestInfo;
    private OrderItem orderItem = new OrderItem();

    public OrderPropertyInformation mockUp() {
        OrderPropertyInformation op = new OrderPropertyInformation();
        AccountDto account = new AccountDto();
        account.setId("1234");
        account.setAccountNo("62142_mock");
        account.setCreateDate("2025/08/11 11:30:00");

        AccountRequest request = new AccountRequest();
        request.setAccountName("Beng Lnwza");
        request.setAction("create");
        ObjectMapper mapper = new ObjectMapper();
        op.getOrderItem().setAccount(account);
        op.setRequestInfo(mapper.writeValueAsString(request));
        return op;
    }
}
