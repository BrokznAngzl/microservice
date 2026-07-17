package open.microservice.accountmanagement;

import jakarta.annotation.PostConstruct;
import open.microservice.accountmanagement.util.CacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AccountManagementApplication {
	@Autowired
	private CacheUtil cacheUtil;

	public static void main(String[] args) {
		SpringApplication.run(AccountManagementApplication.class, args);
	}

	@PostConstruct
	public void loadCache() {
		cacheUtil.loadOperatorManagementCache();
	}

}
