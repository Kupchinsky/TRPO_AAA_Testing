package ru.killer666.aaa.service;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.springframework.beans.factory.InitializingBean;
import ru.killer666.trpo.aaa.domains.*;
import ru.killer666.trpo.aaa.services.HibernateSessionService;

import java.util.Properties;

public class HibernateSessionServiceImpl implements HibernateSessionService, InitializingBean {
    private SessionFactory sessionFactory;

    @Override
    public SessionFactory getObject() throws Exception {
        return this.sessionFactory;
    }

    @Override
    public Class<?> getObjectType() {
        return SessionFactory.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.setProperty("org.jboss.logging.provider", "slf4j");

        Properties prop = new Properties();
        prop.setProperty("hibernate.hbm2ddl.auto", "update");
        prop.setProperty("hibernate.connection.url", "jdbc:h2:./webapp");
        prop.setProperty("hibernate.connection.username", "sa");
        prop.setProperty("hibernate.connection.password", "");
        prop.setProperty("dialect", "org.hibernate.dialect.H2Dialect");

        AnnotationConfiguration annotationConfiguration = new AnnotationConfiguration()
                .addPackage("ru.killer666.trpo.aaa.domains")
                .addProperties(prop);

        annotationConfiguration.addAnnotatedClass(User.class)
                .addAnnotatedClass(Resource.class)
                .addAnnotatedClass(ResourceWithRole.class)
                .addAnnotatedClass(Accounting.class)
                .addAnnotatedClass(AccountingResource.class);

        this.sessionFactory = annotationConfiguration.buildSessionFactory();
    }
}
