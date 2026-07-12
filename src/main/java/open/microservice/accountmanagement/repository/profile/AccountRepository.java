package open.microservice.accountmanagement.repository.profile;


import open.microservice.accountmanagement.model.hibernate.pf.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
}
