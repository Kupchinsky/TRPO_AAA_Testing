package ru.killer666.aaa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Controller
@RequestMapping(value = "/echo")
public class EchoController {
    @RequestMapping(value = "/")
    public String index(ModelMap modelMap) {
        return "echo/index";
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public String get(@RequestParam(value = "id", required = true) String X) {
        return X;
    }

    @RequestMapping(value = "/post", method = RequestMethod.POST)
    @ResponseBody
    public void post(@RequestParam(value = "id", required = true) String X, HttpServletResponse response) throws UnsupportedEncodingException {
        response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
        response.setHeader("Location", "/echo/get?id=" + URLEncoder.encode(X, "UTF-8"));
    }
}

