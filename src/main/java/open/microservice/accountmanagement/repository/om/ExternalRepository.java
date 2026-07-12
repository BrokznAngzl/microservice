package open.microservice.accountmanagement.repository.om;


import open.microservice.accountmanagement.model.hibernate.om.External;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExternalRepository extends JpaRepository<External, String> {
}
