package com.ajaskiewicz.PlantManager;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class homePageController {

    @RequestMapping("/")
    public String homePage() {
        return "homePage";
    }

    @RequestMapping("/forgotPass")
    public String forgotPassPage() {
        return "forgotPassPage";
    }

    @RequestMapping("/dashboard")
    public String dashboardPage() {
        return "dashboardPage";
    }

}
