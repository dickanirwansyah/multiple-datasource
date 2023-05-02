package com.app.springmultidatasource.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;

@Slf4j
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactoryBook",
        basePackages = {"com.app.springmultidatasource.repository.book"},
        transactionManagerRef = "transactionManagerBook"
)
public class PostgreSQLDBConfiguration {

    @Value("${spring.book.datasource.url}")
    private String urlDb;

    @Value("${spring.book.datasource.username}")
    private String usernameDb;

    @Value("${spring.book.datasource.password}")
    private String passwordDb;

    @Value("${spring.book.datasource.driverClassName}")
    private String driverClassNameDb;

    @Bean(name = "datasourceBook")
    @ConfigurationProperties(prefix = "spring.book")
    public DataSource dataSource(){
        log.info("initialize bean datasource() - book");
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(urlDb);
        dataSource.setUsername(usernameDb);
        dataSource.setPassword(passwordDb);
        dataSource.setDriverClassName(driverClassNameDb);
        return dataSource;
    }

    @Bean(name = "entityManagerFactoryBook")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(EntityManagerFactoryBuilder builder,
                                                                           @Qualifier("datasourceBook")DataSource dataSource){
        log.info("initialize bean entityManagerFactoryBean() - book");
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQL94Dialect");

        return builder
                .dataSource(dataSource)
                .properties(properties)
                .packages("com.app.springmultidatasource.entity.book")
                .persistenceUnit("Book").build();
    }

    @Bean(name = "transactionManagerBook")
    public TransactionManager transactionManager(@Qualifier("entityManagerFactoryBook")EntityManagerFactory entityManagerFactory){
        log.info("initialize bean transactionManagerBook() - book");
        return new JpaTransactionManager(entityManagerFactory);
    }
}
