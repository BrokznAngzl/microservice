package open.microservice.accountmanagement.model.hibernate.om;

import jakarta.persistence.*;

import java.util.Date;

import static open.microservice.accountmanagement.constant.DatabaseConstant.OM_ORDER;
import static open.microservice.accountmanagement.constant.DatabaseConstant.OM_SCHEMA;

@Entity
@Table(schema = OM_SCHEMA, name = OM_ORDER)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "last_updated_date")
    private Date lastUpdatedDate;

    @Column(name = "last_updated_by")
    private String lastUpdatedBy;

    @Column(name = "status")
    private String status;

    @Column(name = "respones")
    private String respones;

}
