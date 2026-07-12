package open.microservice.accountmanagement.repository.om;


import open.microservice.accountmanagement.model.hibernate.om.ExternalParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExternalParamRepository extends JpaRepository<ExternalParam, String> {
    public List<ExternalParam> findByExternalId(String externalId);
}
