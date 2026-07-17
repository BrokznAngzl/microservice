package open.microservice.accountmanagement.model.hibernate.om;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import static open.microservice.accountmanagement.constant.DatabaseConstant.OM_EXTERNAL;
import static open.microservice.accountmanagement.constant.DatabaseConstant.OM_SCHEMA;

@Entity
@Table(schema = OM_SCHEMA, name = OM_EXTERNAL)
@Getter
@Setter
public class External {

    @Id
    private String id;

    @Column(name = "order_name")
    private String orderName;

    @Column(name = "external_node")
    private String externalNode;

    @Column(name = "compose_sequence")
    private int composeSequence;

    private String action;

    private String api;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "condition_id")
    private Condition condition;

}