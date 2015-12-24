package ru.killer666.aaa;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.orm.hibernate4.support.OpenSessionInViewInterceptor;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.killer666.aaa.service.GsonService;
import ru.killer666.aaa.service.RoleResolverServiceImpl;
import ru.killer666.trpo.aaa.services.RoleResolverService;

import java.util.Properties;

@Configuration
@ComponentScan
@EnableTransactionManagement
@Import(ru.killer666.trpo.aaa.BeanConfiguration.class)
public class BeanConfiguration {
    @Bean
    GsonService gsonService() {
        return new GsonService();
    }

    @Bean
    RoleResolverService roleResolverService() {
        return new RoleResolverServiceImpl();
    }

    @Bean
    public HibernateTransactionManager hibernateTransactionManager() {
        HibernateTransactionManager hibernateTransactionManager = new HibernateTransactionManager();

        hibernateTransactionManager.setDataSource(this.driverManagerDataSource());
        hibernateTransactionManager.setSessionFactory(this.localSessionFactoryBean().getObject());

        return hibernateTransactionManager;
    }

    @Bean
    public DriverManagerDataSource driverManagerDataSource() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();

        driverManagerDataSource.setUrl("jdbc:h2:./webapp");
        driverManagerDataSource.setUsername("sa");
        driverManagerDataSource.setPassword("");

        return driverManagerDataSource;
    }

    @Bean
    public LocalSessionFactoryBean localSessionFactoryBean() {
        System.setProperty("org.jboss.logging.provider", "slf4j");

        LocalSessionFactoryBean localSessionFactoryBean = new LocalSessionFactoryBean();

        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "update");
        hibernateProperties.setProperty("dialect", "org.hibernate.dialect.H2Dialect");

        localSessionFactoryBean.setHibernateProperties(hibernateProperties);
        localSessionFactoryBean.setPackagesToScan("ru.killer666.trpo.aaa", "ru.killer666.aaa");
        localSessionFactoryBean.setDataSource(this.driverManagerDataSource());

        return localSessionFactoryBean;
    }

    @Bean
    public OpenSessionInViewInterceptor openSessionInViewInterceptor() {
        OpenSessionInViewInterceptor openSessionInViewInterceptor = new OpenSessionInViewInterceptor();

        openSessionInViewInterceptor.setSessionFactory(this.localSessionFactoryBean().getObject());

        return openSessionInViewInterceptor;
    }
}
