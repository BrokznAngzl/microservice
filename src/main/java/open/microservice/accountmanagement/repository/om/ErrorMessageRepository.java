package open.microservice.accountmanagement.repository.om;


import open.microservice.accountmanagement.model.hibernate.om.ErrorMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ErrorMessageRepository extends JpaRepository<ErrorMessage, String> {
}
