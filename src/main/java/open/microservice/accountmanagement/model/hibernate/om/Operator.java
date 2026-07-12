package open.microservice.accountmanagement.model.hibernate.om;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import static open.microservice.accountmanagement.constant.DatabaseConstant.OM_OPERATOR;
import static open.microservice.accountmanagement.constant.DatabaseConstant.OM_SCHEMA;

@Entity
@Table(schema = OM_SCHEMA, name = OM_OPERATOR)
@Getter
@Setter
public class Operator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "operator_name")
    private String operatorName;
}
