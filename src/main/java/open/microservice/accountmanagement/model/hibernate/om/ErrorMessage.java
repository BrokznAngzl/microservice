package open.microservice.accountmanagement.model.hibernate.om;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import static open.microservice.accountmanagement.constant.DatabaseConstant.OM_ERROR_MESSAGE;
import static open.microservice.accountmanagement.constant.DatabaseConstant.OM_SCHEMA;

@Entity
@Getter
@Setter
@Table(schema = OM_SCHEMA, name = OM_ERROR_MESSAGE)
public class ErrorMessage {
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "message")
    private String message;
    @Column(name = "statusCode")
    private String statusCode;
}
