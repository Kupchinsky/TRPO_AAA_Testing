package ru.killer666.aaa.controller;

import com.google.common.base.Preconditions;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Expression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.killer666.trpo.aaa.domains.ResourceWithRole;
import ru.killer666.trpo.aaa.domains.User;

import java.util.List;

@Controller
@RequestMapping(value = "/ajax/authority")
@Transactional
public class AuthorityController {
    @Autowired
    SessionFactory sessionFactory;

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List getAll() throws Exception {
        Session session = this.sessionFactory.getCurrentSession();
        return session.createCriteria(ResourceWithRole.class).list();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResourceWithRole getSingle(@PathVariable String id) throws Exception {
        Session session = this.sessionFactory.getCurrentSession();
        return (ResourceWithRole) session.get(ResourceWithRole.class, Integer.parseInt(id));
    }

    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List getList(@PathVariable String userId) throws Exception {
        Session session = this.sessionFactory.getCurrentSession();
        User user = (User) session.get(User.class, Integer.parseInt(userId));

        Preconditions.checkArgument(user != null, "user not found");

        Criteria criteria = session.createCriteria(ResourceWithRole.class);
        criteria.add(Expression.eq("user", user));

        return criteria.list();
    }
}

