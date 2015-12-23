package ru.killer666.aaa;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import ru.killer666.aaa.service.HibernateSessionServiceImpl;
import ru.killer666.aaa.service.RoleResolverServiceImpl;
import ru.killer666.trpo.aaa.services.HibernateSessionService;
import ru.killer666.trpo.aaa.services.RoleResolverService;

@Configuration
@ComponentScan
@Import(ru.killer666.trpo.aaa.BeanConfiguration.class)
@EnableWebMvc
public class BeanConfiguration extends WebMvcConfigurerAdapter {
    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/jsp/");
        resolver.setSuffix(".jsp");

        return resolver;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("home");
    }

    @Bean
    RoleResolverService roleResolverService() {
        return new RoleResolverServiceImpl();
    }

    @Bean
    public HibernateSessionService hibernateSessionService() {
        HibernateSessionServiceImpl hibernateSessionService = new HibernateSessionServiceImpl();
        return hibernateSessionService;
    }
}
