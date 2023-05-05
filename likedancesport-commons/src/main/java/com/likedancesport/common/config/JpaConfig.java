package com.likedancesport.common.config;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.SpringBeanContainer;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableJpaRepositories(basePackages = "com.likedancesport")
public class JpaConfig {

    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(DataSource dataSource, ConfigurableListableBeanFactory beanFactory) {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        factoryBean.setJpaProperties(additionalProperties());
        factoryBean.setPackagesToScan("com.likedancesport");
        factoryBean.getJpaPropertyMap().put(AvailableSettings.BEAN_CONTAINER, new SpringBeanContainer(beanFactory));
        return factoryBean;
    }

    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

    @Bean
    @Profile("local")
    public DataSource localDataSource() {
        System.out.println("---- BUILD DATASOURCE ------");
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/likedancesport");
        dataSource.setUsername("postgres");
        dataSource.setPassword("2jzgtevvti");
        dataSource.setConnectionProperties(additionalProperties());
        return dataSource;
    }

    @Bean
    @Profile("!local")
    public DataSource dataSource(DbCredentialsBean dbCredentials) {
        System.out.println("---- BUILD DATASOURCE ------");
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(dbCredentials.getDbUrl());
        dataSource.setUsername(dbCredentials.getDbUsername());
        dataSource.setPassword(dbCredentials.getDbPassword());
        dataSource.setConnectionProperties(additionalProperties());
        return dataSource;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Bean
    public Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL81Dialect");
        return properties;
    }
}
