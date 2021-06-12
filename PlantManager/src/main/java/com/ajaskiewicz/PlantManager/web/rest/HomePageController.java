package com.ajaskiewicz.PlantManager.web.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomePageController {

    @RequestMapping("/forgotPass")
    public String forgotPassPage() {
        return "forgotPassPage";
    }

}
