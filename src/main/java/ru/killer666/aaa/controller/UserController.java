package ru.killer666.aaa.controller;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.killer666.aaa.service.GsonService;
import ru.killer666.trpo.aaa.domains.User;

@Controller
@RequestMapping(value = "/ajax/user")
@Transactional
public class UserController {
    @Autowired
    GsonService gsonService;

    @Autowired
    SessionFactory sessionFactory;

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String getAll(@RequestParam(value = "id", required = false) String id) throws Exception {
        Session session = this.sessionFactory.getCurrentSession();

        Object toSerialize;

        if (id == null) {
            toSerialize = session.createCriteria(User.class).list();
        } else {
            toSerialize = session.get(User.class, Integer.parseInt(id));
        }

        return this.gsonService.getObject().toJson(toSerialize);
    }
}