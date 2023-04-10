package com.likedancesport.config;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.mediaconvert.MediaConvertClient;
import software.amazon.awssdk.services.mediaconvert.model.DescribeEndpointsRequest;
import software.amazon.awssdk.services.mediaconvert.model.DescribeEndpointsResponse;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.net.URI;
import java.util.Properties;

@Configuration
@EnableJpaRepositories(basePackages = "com.likedancesport")
public class AppConfig {
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        factoryBean.setJpaProperties(additionalProperties());
        factoryBean.setPackagesToScan("com.likedancesport");
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

    public Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL81Dialect");
        return properties;
    }

    @Bean
    public MediaConvertClient mediaConvertClient() {
        try (MediaConvertClient mc = MediaConvertClient.create()) {
            DescribeEndpointsResponse res = mc
                    .describeEndpoints(DescribeEndpointsRequest.builder().maxResults(20).build());

            String endpointURL = res.endpoints().get(0).url();
            return MediaConvertClient.builder()
                    .region(mc.serviceClientConfiguration().region())
                    .endpointOverride(URI.create(endpointURL))
                    .build();
        }
    }
}
