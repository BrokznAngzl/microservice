package open.microservice.accountmanagement.configuration.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app-config")
public class AppConfig {
    private Pod pod;

    /* product order */
    @Data
    public static class Pod {
        private String host;
        private String uri;
        private String conTimeout;
        private String readTimeout;
    }
}

