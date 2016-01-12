package ru.killer666.aaa.controller;

import com.google.common.base.Preconditions;
import lombok.Data;
import lombok.ToString;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Expression;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.killer666.trpo.aaa.InjectLogger;
import ru.killer666.trpo.aaa.domains.Accounting;
import ru.killer666.trpo.aaa.domains.AccountingResource;
import ru.killer666.trpo.aaa.domains.ResourceWithRole;
import ru.killer666.trpo.aaa.services.AccountingService;
import ru.killer666.trpo.aaa.services.AuthorizationService;

import java.util.List;

@Controller
@RequestMapping(value = "/ajax/activity")
@Transactional
public class ActivityController {
    @InjectLogger
    private static Logger logger;

    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    AuthorizationService authorizationService;

    @Autowired
    AccountingService accountingService;

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List getAll() throws Exception {
        Session session = this.sessionFactory.getCurrentSession();
        return session.createCriteria(Accounting.class).list();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Accounting getSingle(@PathVariable String id) throws Exception {
        Session session = this.sessionFactory.getCurrentSession();
        return (Accounting) session.get(Accounting.class, Integer.parseInt(id));
    }

    @RequestMapping(value = "/authority/{authorityId}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List getList(@PathVariable String authorityId) throws Exception {
        Session session = this.sessionFactory.getCurrentSession();
        ResourceWithRole resourceWithRole = (ResourceWithRole) session.get(ResourceWithRole.class, Integer.parseInt(authorityId));

        Preconditions.checkArgument(resourceWithRole != null, "authority not found");

        Criteria criteria = session.createCriteria(AccountingResource.class);
        criteria.add(Expression.eq("resourceWithRole", resourceWithRole));

        return criteria.list();
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String postAll(@RequestBody AuthRequest authRequest) throws Exception {
        logger.warn("Auth request: {}", authRequest);

        //User authorizedUser;
        //Accounting accounting;

        return "{}";
    }

    @Data
    @ToString
    public static class AuthRequest {
        private String login;
        private String pass;
        private String res;
        private String role;
        private String ds;
        private String de;
        private String vol;
    }
}

