package com.ajaskiewicz.PlantManager.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class homePageController {

    @RequestMapping("/forgotPass")
    public String forgotPassPage() {
        return "forgotPassPage";
    }

}
