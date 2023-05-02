package com.app.springmultidatasource.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;

@Slf4j
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactoryUsers",
        transactionManagerRef = "transactionManagerUsers",
        basePackages = {"com.app.springmultidatasource.repository.users"})
public class MySQLDBConfiguration {

    @Value("${spring.users.datasource.url}")
    private String urlDb;

    @Value("${spring.users.datasource.username}")
    private String usernameDb;

    @Value("${spring.users.datasource.password}")
    private String passwordDb;

    @Value("${spring.users.datasource.driverClassName}")
    private String driverClassNameDb;

    @Primary
    @Bean(name = "datasourceUsers")
    @ConfigurationProperties(prefix = "spring.users")
    public DataSource dataSource(){
        log.info("initilize bean dataSource() - users");
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(urlDb);
        dataSource.setUsername(usernameDb);
        dataSource.setPassword(passwordDb);
        dataSource.setDriverClassName(driverClassNameDb);
        return dataSource;
    }

    @Primary
    @Bean(name = "entityManagerFactoryUsers")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(EntityManagerFactoryBuilder builder, @Qualifier("datasourceUsers")DataSource dataSource){
        log.info("initialize bean entityManagerFactoryBean() - users");
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");

        return builder
                .dataSource(dataSource)
                .properties(properties)
                .packages("com.app.springmultidatasource.entity.users")
                .persistenceUnit("Users").build();
    }

    @Primary
    @Bean(name = "transactionManagerUsers")
    public PlatformTransactionManager transactionManager(@Qualifier("entityManagerFactoryUsers") EntityManagerFactory entityManagerFactory){
        log.info("initialize bean transactionManagerUsers() - users");
        return new JpaTransactionManager(entityManagerFactory);
    }
}
