package open.microservice.accountmanagement.model.hibernate.pf;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

import static open.microservice.accountmanagement.constant.DatabaseConstant.PF_ACCOUNT;
import static open.microservice.accountmanagement.constant.DatabaseConstant.PF_SCHEMA;


@Getter
@Setter
@Entity
@Table(schema = PF_SCHEMA, name = PF_ACCOUNT)
public class Account {

    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "account_no")
    private String accountNo;
    @Column(name = "account_name")
    private String accountName;
    @Column(name = "createDate")
    private Date createDate;
    @Column(name = "create_by")
    private String createBy;
    @Column(name = "last_updated_date")
    private Date lastUpdatedDate;
    @Column(name = "last_update_by")
    private String lastUpdateBy;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_primary_id", insertable = false, updatable = false)
    private Address address;
}
