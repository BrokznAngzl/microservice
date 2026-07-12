package open.microservice.accountmanagement.service.profile;


import lombok.extern.slf4j.Slf4j;
import open.microservice.accountmanagement.model.request.pf.ProfileRequestParam;
import open.microservice.accountmanagement.repository.profile.AccountRepository;
import open.microservice.accountmanagement.repository.profile.AddressRepository;
import open.microservice.accountmanagement.util.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Service
public class ProfileService {
    @Autowired
    private AccountRepository accountRepo;
    @Autowired
    private AddressRepository addressRepo;
    @Autowired
    private ObjectMapper objectMapper;

    @Transactional(timeout = 60, transactionManager = "pfTransactionManager", rollbackFor = Exception.class)
    public void createNewProfile(String requestParam) {
        ProfileRequestParam request = objectMapper.readValue(requestParam, ProfileRequestParam.class);
        try {
            saveProfile(request);
            flushProfile(request);
        } catch (Exception e) {
            log.error("Error creating new profile: {}", e.getMessage(), e);
        }
    }

    private void saveProfile(ProfileRequestParam request) {
        if (ObjectUtil.isNotEmpty(request.getAccount())) {
            log.info("saving account...");
            accountRepo.save(request.getAccount());
            log.info("account saved successfully");
        }
        if (ObjectUtil.isNotEmpty(request.getAddress())) {
            log.info("saving address...");
            addressRepo.save(request.getAddress());
            log.info("address saved successfully");
        }
    }

    private void flushProfile(ProfileRequestParam request) {
        if (ObjectUtil.isNotEmpty(request.getAccount())) {
            accountRepo.flush();
        }
        if (ObjectUtil.isNotEmpty(request.getAddress())) {
            addressRepo.flush();
        }
    }
}
