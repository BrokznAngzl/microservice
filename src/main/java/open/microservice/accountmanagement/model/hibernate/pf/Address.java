package open.microservice.accountmanagement.model.hibernate.pf;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import open.microservice.accountmanagement.constant.DatabaseConstant;

import static open.microservice.accountmanagement.constant.DatabaseConstant.PF_ADDRESS;
import static open.microservice.accountmanagement.constant.DatabaseConstant.PF_SCHEMA;

@Getter
@Setter
@Entity
@Table(schema = PF_SCHEMA, name = PF_ADDRESS)
public class Address {

    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "address_detail")
    private String addressDetail;
    @Column(name = "provice")
    private String province;
//    need more columns
}

