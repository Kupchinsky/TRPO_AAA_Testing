package ru.killer666.aaa;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.killer666.aaa.service.RoleResolverServiceImpl;
import ru.killer666.trpo.aaa.services.RoleResolverService;

@Configuration
@ComponentScan
@Import(ru.killer666.trpo.aaa.BeanConfiguration.class)
public class BeanConfiguration {
    @Bean
    RoleResolverService roleResolverService() {
        return new RoleResolverServiceImpl();
    }
}
