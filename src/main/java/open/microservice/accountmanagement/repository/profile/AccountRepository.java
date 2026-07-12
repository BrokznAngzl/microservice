package open.microservice.accountmanagement.repository.profile;

import com.open.project.account.models.hibernate.pf.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
}
