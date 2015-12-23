package ru.killer666.aaa.controller;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.killer666.aaa.service.GsonService;
import ru.killer666.trpo.aaa.domains.User;
import ru.killer666.trpo.aaa.services.HibernateSessionService;

@Controller
@RequestMapping(value = "/ajax/user")
public class UserController {
    @Autowired
    GsonService gsonService;

    @Autowired
    HibernateSessionService sessionService;

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String getAll(@RequestParam(value = "id", required = false) String id) throws Exception {
        Session session = this.sessionService.getObject().openSession();
        Object toSerialize;

        if (id == null) {
            toSerialize = session.createCriteria(User.class).list();
        } else {
            toSerialize = session.get(User.class, Integer.parseInt(id));
        }

        session.close();

        return this.gsonService.getObject().toJson(toSerialize);
    }
}