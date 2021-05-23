package com.ajaskiewicz.PlantManager.web;

import com.ajaskiewicz.PlantManager.service.PlantService;
import com.ajaskiewicz.PlantManager.service.RoomService;
import com.ajaskiewicz.PlantManager.service.WateringScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/toBeWateredSoon")
public class WateringController {

    @Autowired
    private PlantService plantService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private WateringScheduleService wateringScheduleService;

    @RequestMapping("")
    public String showDashboard(Model model) {
        model.addAttribute("plant", plantService.findPlantsToBeWateredSoon());
        return "waterSoon";
    }

}
