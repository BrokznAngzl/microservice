package open.microservice.accountmanagement.model.hibernate;

import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.Getter;
import lombok.Setter;
import open.microservice.accountmanagement.model.dto.OrderItem;
import open.microservice.accountmanagement.model.hibernate.om.Order;

@Getter
@Setter
public class OrderPropertyInformation {
    private Order order;
    @JsonRawValue
    private String requestInfo;
    private OrderItem orderItem = new OrderItem();
}
