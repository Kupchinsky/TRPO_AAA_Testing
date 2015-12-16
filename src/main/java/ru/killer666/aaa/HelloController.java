package ru.killer666.aaa;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HelloController {
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public String get(@RequestParam(value = "id", required = true) String X, ModelMap model) {
        model.addAttribute("value", X);
        return "echo.get";
    }
}

