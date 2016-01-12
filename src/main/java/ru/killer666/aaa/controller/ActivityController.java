package ru.killer666.aaa.controller;

import com.google.common.base.Preconditions;
import com.google.gson.annotations.Expose;
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
import ru.killer666.aaa.RoleEnum;
import ru.killer666.trpo.aaa.InjectLogger;
import ru.killer666.trpo.aaa.domains.*;
import ru.killer666.trpo.aaa.exceptions.IncorrectPasswordException;
import ru.killer666.trpo.aaa.exceptions.ResourceDeniedException;
import ru.killer666.trpo.aaa.exceptions.ResourceNotFoundException;
import ru.killer666.trpo.aaa.exceptions.UserNotFoundException;
import ru.killer666.trpo.aaa.services.AccountingService;
import ru.killer666.trpo.aaa.services.AuthorizationService;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

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

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public List getAll() throws Exception {
        Session session = this.sessionFactory.getCurrentSession();
        return session.createCriteria(Accounting.class).list();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Accounting getSingle(@PathVariable String id) throws Exception {
        Session session = this.sessionFactory.getCurrentSession();
        return (Accounting) session.get(Accounting.class, Integer.parseInt(id));
    }

    @RequestMapping(value = "/authority/{authorityId}", method = RequestMethod.GET)
    @ResponseBody
    public List getList(@PathVariable String authorityId) throws Exception {
        Session session = this.sessionFactory.getCurrentSession();
        ResourceWithRole resourceWithRole = (ResourceWithRole) session.get(ResourceWithRole.class, Integer.parseInt(authorityId));

        Preconditions.checkArgument(resourceWithRole != null, "authority not found");

        Criteria criteria = session.createCriteria(AccountingResource.class);
        criteria.add(Expression.eq("resourceWithRole", resourceWithRole));

        return criteria.list();
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    public String postAll(@RequestBody AuthRequest authRequest) {
        logger.debug("Auth request: {}", authRequest);

        Preconditions.checkArgument(authRequest.getLogin() != null, "-login can't be empty");
        Preconditions.checkArgument(authRequest.getPass() != null, "-pass can't be empty");

        final User authorizedUser;

        try {
            authorizedUser = this.authorizationService.authorizeUser(authRequest.getLogin(), authRequest.getPass());
        } catch (UserNotFoundException | IncorrectPasswordException e) {
            return e.toString();
        }

        if (authRequest.getRes() != null) {
            Preconditions.checkArgument(authRequest.getRole() != null, "-role can't be empty");

            final ResourceWithRole resourceWithRole;

            try {
                Resource resource = this.authorizationService.findResourceByName(authRequest.getRes());
                resourceWithRole = this.authorizationService.authorizeOnResource(authorizedUser, resource,
                        RoleEnum.valueOf(authRequest.getRole()));
            } catch (ResourceNotFoundException | ResourceDeniedException e) {
                return e.toString();
            }

            if (authRequest.getDs() != null &&
                    authRequest.getDe() != null &&
                    authRequest.getVol() != null) {
                Accounting accounting = this.accountingService.createForUser(authorizedUser);

                accounting.pushResource(resourceWithRole);

                DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                format.setLenient(false);

                try {
                    accounting.setLoginDate(format.parse(authRequest.getDs()));
                    accounting.setLogoutDate(format.parse(authRequest.getDe()));
                } catch (ParseException e) {
                    return e.toString();
                }

                accounting.increaseVolume(Integer.parseInt(authRequest.getVol()));

                this.accountingService.save(accounting);
            }
        }

        return "Okay!";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public String handleIllegalArgument(Exception exception) {
        return exception.toString();
    }

    @Data
    @ToString
    public static class AuthRequest {
        @Expose
        private String login;

        @Expose
        private String pass;

        @Expose
        private String res;

        @Expose
        private String role;

        @Expose
        private String ds;

        @Expose
        private String de;

        @Expose
        private String vol;
    }
}

