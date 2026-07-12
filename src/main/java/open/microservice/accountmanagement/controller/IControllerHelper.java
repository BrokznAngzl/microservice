package open.microservice.accountmanagement.controller;


import open.microservice.accountmanagement.model.hibernate.OrderPropertyInformation;
import open.microservice.accountmanagement.model.hibernate.om.Order;
import org.springframework.http.ResponseEntity;

public interface IControllerHelper<T> {
    void validateRequest(T request) throws Exception;
    void saveOrderAndExOrder(Order order);
    void composeExOrderAndParam(OrderPropertyInformation opi) throws Exception;
    void provisioning(Order order) throws Exception;
    ResponseEntity<?> composeResponse(OrderPropertyInformation opi, T request) throws Exception;
}
