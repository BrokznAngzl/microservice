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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private String id;
    @Column(name = "account_no")
    private String accountNo;
    @Column(name = "createDate")
    private Date createDate;

    @OneToOne(mappedBy = "accountNo", fetch = FetchType.LAZY)
    private Address address;
}
