package open.microservice.accountmanagement.model.hibernate.om;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

import static open.microservice.accountmanagement.constant.DatabaseConstant.OM_EXTERNAL_ORDER;
import static open.microservice.accountmanagement.constant.DatabaseConstant.OM_SCHEMA;

@Entity
@Table(schema = OM_SCHEMA, name = OM_EXTERNAL_ORDER)
@Getter
@Setter
public class ExternalOrder {

    @Id
    private String id;

    @Column(name = "order_name")
    private String orderName;

    @Column(name = "external_id")
    private String externalId;

    @Column(name = "external_node")
    private String externalNode;

    private String endpoint;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "last_updated_date")
    private Date lastUpdatedDate;

    @Column(name = "last_updated_by")
    private String lastUpdatedBy;

    @Column(name = "request_date")
    private Date requestDate;

    @Column(name = "response_date")
    private Date responseDate;

    @Column(name = "request_info")
    private String requestInfo;

    @Column(name = "response_info")
    private String responseInfo;

    @Column(name = "status")
    private String status;
}
