package open.microservice.accountmanagement.repository.om;


import open.microservice.accountmanagement.model.hibernate.om.External;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExternalRepository extends JpaRepository<External, String> {
}
