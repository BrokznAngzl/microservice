package open.microservice.accountmanagement.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class OrderDto {
    private String id;
    private String request;
    private Date createdDate;
    private String createdBy;
    private Date lastUpdatedDate;
    private String lastUpdatedBy;
    private String status;
    private String response;
}
