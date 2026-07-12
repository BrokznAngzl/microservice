package open.microservice.accountmanagement.service;


import open.microservice.accountmanagement.model.hibernate.OrderPropertyInformation;
import open.microservice.accountmanagement.model.hibernate.om.ExternalOrder;
import open.microservice.accountmanagement.model.hibernate.om.Order;

public interface IComposer {
    boolean canCompose(String value);
    void compose(OrderPropertyInformation orderProperty, ExternalOrder externalOrder, Order order) throws Exception;
}
