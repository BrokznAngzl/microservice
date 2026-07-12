package open.microservice.accountmanagement.model.hibernate.om;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "external_order")
@Getter
@Setter
public class ExternalOrder {

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
