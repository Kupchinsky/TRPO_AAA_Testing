package ru.killer666.aaa.controller;

import com.google.common.base.Preconditions;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.killer666.aaa.RoleEnum;
import ru.killer666.aaa.service.GsonService;
import ru.killer666.trpo.aaa.domains.ResourceWithRole;
import ru.killer666.trpo.aaa.domains.User;
import ru.killer666.trpo.aaa.services.HibernateSessionService;

import java.util.Arrays;

@Controller
@RequestMapping(value = "/ajax/authority")
public class AuthorityController {
    @Autowired
    GsonService gsonService;

    @Autowired
    HibernateSessionService sessionService;

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String getAll(@RequestParam(value = "id", required = false) String id,
                         @RequestParam(value = "userId", required = false) String userId) throws Exception {
        Session session = this.sessionService.getObject().openSession();
        Object toSerialize;

        if (id != null) {
            toSerialize = session.get(ResourceWithRole.class, Integer.parseInt(id));
        } else if (userId != null) {
            User user = (User) session.get(User.class, Integer.parseInt(userId));

            Preconditions.checkArgument(user != null, "user not found");

            Criteria criteria = session.createCriteria(ResourceWithRole.class);
            criteria.add(Expression.eq("user", user));

            toSerialize = criteria.list();
        } else {
            toSerialize = Arrays.asList(RoleEnum.values());
        }

        session.close();

        return this.gsonService.getObject().toJson(toSerialize);
    }
}

