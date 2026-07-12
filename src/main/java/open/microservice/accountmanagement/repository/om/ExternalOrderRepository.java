package open.microservice.accountmanagement.repository.om;


import open.microservice.accountmanagement.model.hibernate.om.ExternalOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExternalOrderRepository extends JpaRepository<ExternalOrder, String> {
}
