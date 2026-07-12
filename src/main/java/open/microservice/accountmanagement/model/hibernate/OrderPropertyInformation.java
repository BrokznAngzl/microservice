package open.microservice.accountmanagement.model.hibernate;

import lombok.Getter;
import lombok.Setter;
import open.microservice.accountmanagement.model.dto.OrderItem;
import open.microservice.accountmanagement.model.hibernate.om.Order;

@Getter
@Setter
public class OrderPropertyInformation {
    private Order order;
    private OrderItem orderItem;
}
