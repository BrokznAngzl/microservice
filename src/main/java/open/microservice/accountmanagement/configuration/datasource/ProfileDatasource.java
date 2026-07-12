package open.microservice.accountmanagement.configuration.datasource;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.boot.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "aiEntityManagerFactory",
        basePackages = {"open.microservice.accountmanagement.repository"},
        transactionManagerRef = "pfTransactionManager")
public class ProfileDatasource {

    @Bean(name = "pfDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.pf")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "pfDataSource")
    @ConfigurationProperties("spring.datasource.pf.hikari")
    public DataSource dataSource(@Qualifier("pfDataSourceProperties") DataSourceProperties pfDataSourceProperties) {
        return pfDataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean(name = "pfEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("pfDataSource") DataSource rbmDataSource) {
        return builder.dataSource(rbmDataSource).packages("open.microservice.accountmanagement.models.hibernate.pf").persistenceUnit("oracle").build();
    }

    @Bean(name = "pfTransactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("pfEntityManagerFactory") EntityManagerFactory pfEntityManagerFactory) {
        return new JpaTransactionManager(pfEntityManagerFactory);
    }
}
