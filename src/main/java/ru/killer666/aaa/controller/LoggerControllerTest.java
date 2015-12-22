package ru.killer666.aaa.controller;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.killer666.trpo.aaa.InjectLogger;

@Controller
@RequestMapping(value = "/test/logger")
public class LoggerControllerTest {
    @InjectLogger
    private static Logger logger;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public String test() {
        logger.warn("Logger works");
        return "Logger works";
    }
}