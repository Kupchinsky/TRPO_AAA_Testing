package ru.killer666.aaa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.killer666.aaa.service.GsonService;
import ru.killer666.trpo.aaa.services.HibernateSessionService;

@Controller
@RequestMapping(value = "/ajax/authority")
public class AuthorityController {
    @Autowired
    GsonService gsonService;

    @Autowired
    HibernateSessionService sessionService;
}

