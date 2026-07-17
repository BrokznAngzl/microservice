package open.microservice.accountmanagement.model.hibernate.om;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import static open.microservice.accountmanagement.constant.DatabaseConstant.OM_CONDITION;
import static open.microservice.accountmanagement.constant.DatabaseConstant.OM_SCHEMA;

@Entity
@Table(schema = OM_SCHEMA, name = OM_CONDITION)
@Getter
@Setter
public class Condition {

    @Id
    private String id;

    @Column(name = "source_type")
    private String sourceType;

    @Column(name = "source_value")
    private String sourceValue;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "operator_id")
    private Operator operator;

    @Column(name = "lookup_value")
    private String lookupValue;
}