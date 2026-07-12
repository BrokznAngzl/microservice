package open.microservice.accountmanagement.configuration.datasource;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.boot.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "omEntityManagerFactory",
        basePackages = {"open.microservice.accountmanagement.repository"},
        transactionManagerRef = "transactionManager")
public class OrderManagementDatasource {

    @Primary
    @Bean(name = "omDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.om")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean(name = "omDataSource")
    @ConfigurationProperties("spring.datasource.om.hikari")
    public DataSource dataSource(@Qualifier("omDataSourceProperties") DataSourceProperties omDataSourceProperties) {
        return omDataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Primary
    @Bean(name = "omEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("omDataSource") DataSource rbmDataSource) {
        return builder.dataSource(rbmDataSource).packages("open.microservice.accountmanagement.models.hibernate.om").persistenceUnit("sqlserver").build();
    }

    @Primary
    @Bean(name = "omTransactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("omEntityManagerFactory") EntityManagerFactory omEntityManagerFactory) {
        return new JpaTransactionManager(omEntityManagerFactory);
    }
}
