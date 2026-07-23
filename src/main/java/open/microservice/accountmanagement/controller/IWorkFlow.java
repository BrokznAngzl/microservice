package open.microservice.accountmanagement.controller;


import open.microservice.accountmanagement.model.hibernate.OrderPropertyInformation;
import open.microservice.accountmanagement.model.hibernate.om.Order;
import org.springframework.http.ResponseEntity;

public interface IWorkFlow<T> {

    void validateRequest(T request);

    void saveOrderAndExOrder(Order order);

    void composeExOrderAndParam(OrderPropertyInformation opi);

    void provisioning(Order order);

    ResponseEntity<?> composeResponse(OrderPropertyInformation opi);
}
