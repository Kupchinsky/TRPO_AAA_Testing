package ru.killer666.aaa.controller;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.killer666.trpo.aaa.domains.User;

import java.util.List;

@Controller
@RequestMapping(value = "/ajax/user")
@Transactional
public class UserController {
    @Autowired
    SessionFactory sessionFactory;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public List getAll() throws Exception {
        Session session = this.sessionFactory.getCurrentSession();
        return session.createCriteria(User.class).list();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public User getSingle(@PathVariable String id) throws Exception {
        Session session = this.sessionFactory.getCurrentSession();
        return (User) session.get(User.class, Integer.parseInt(id));
    }
}