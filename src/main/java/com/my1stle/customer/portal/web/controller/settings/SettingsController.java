package com.my1stle.customer.portal.web.controller.settings;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/settings")
public class SettingsController {

    public static final String BASE_PATH = SettingsController.class.getAnnotation(RequestMapping.class).value()[0];

    @GetMapping(value = "")
    public String viewSettings(Model model) {

        return "settings/index";

    }

}
