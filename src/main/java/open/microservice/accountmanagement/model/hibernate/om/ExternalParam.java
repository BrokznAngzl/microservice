package open.microservice.accountmanagement.model.hibernate.om;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import static open.microservice.accountmanagement.constant.DatabaseConstant.OM_EXTERNAL_PARAM;
import static open.microservice.accountmanagement.constant.DatabaseConstant.OM_SCHEMA;

@Entity
@Table(schema = OM_SCHEMA, name = OM_EXTERNAL_PARAM)
@Getter
@Setter
public class ExternalParam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "external_id")
    private String externalId;

    @Column(name = "parameter_name")
    private String parameterName;

    private String type;

    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "condition_id")
    private Condition condition;
}
