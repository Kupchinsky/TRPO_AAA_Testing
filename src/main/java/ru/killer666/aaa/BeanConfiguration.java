package ru.killer666.aaa;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.killer666.aaa.service.GsonService;
import ru.killer666.aaa.service.HibernateSessionServiceImpl;
import ru.killer666.aaa.service.RoleResolverServiceImpl;
import ru.killer666.trpo.aaa.services.HibernateSessionService;
import ru.killer666.trpo.aaa.services.RoleResolverService;

@Configuration
@ComponentScan
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
    public HibernateSessionService hibernateSessionService() {
        HibernateSessionServiceImpl hibernateSessionService = new HibernateSessionServiceImpl();

        // TODO: Configuration

        return hibernateSessionService;
    }
}
