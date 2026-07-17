package open.microservice.accountmanagement.repository.profile;


import open.microservice.accountmanagement.model.hibernate.pf.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {
    Address findByAddressDetailAndProvince(String addressDetail, String province);
}
