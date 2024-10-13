package com.ajaskiewicz.PlantManager.web.rest;

import com.ajaskiewicz.PlantManager.model.Plant;
import com.ajaskiewicz.PlantManager.service.PlantService;
import com.ajaskiewicz.PlantManager.service.RoomService;
import com.ajaskiewicz.PlantManager.service.WateringScheduleService;
import com.ajaskiewicz.PlantManager.service.UserService;
import com.ajaskiewicz.PlantManager.service.SecurityService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/toBeWateredSoon")
public class WateringController {

    private PlantService plantService;
    private RoomService roomService;
    private WateringScheduleService wateringScheduleService;
    private UserService userService;
    private SecurityService securityService;

    @Autowired
    public WateringController(PlantService plantService, RoomService roomService, WateringScheduleService wateringScheduleService, UserService userService, SecurityService securityService) {
        this.plantService = plantService;
        this.roomService = roomService;
        this.wateringScheduleService = wateringScheduleService;
        this.userService = userService;
        this.securityService = securityService;
    }

    @RequestMapping
    public String showToBeWateredSoon(Model model) {
        if (!securityService.isAuthenticated()) { return "redirect:/"; }

        model.addAttribute("plant", plantService.findPlantsToBeWateredSoon(userService.findIdOfLoggedUser()));

        return "waterSoonPage";
    }

    @RequestMapping("/confirm/{id}")
    public String showConfirmWateringView(@PathVariable("id") Integer id, Model model) throws NotFoundException {
        if (!securityService.isAuthenticated()) { return "redirect:/"; }

        var plant = plantService.find(id);
        model.addAttribute("plant", plant);

        return "wateringConfirmationPage";
    }

    @RequestMapping(path = "/confirmWatering", method = RequestMethod.POST)
    public String confirmWateringToday(Plant plant) throws NotFoundException {
        if (!securityService.isAuthenticated()) { return "redirect:/"; }

        plantService.updateLastWateredDate(plant);

        return "redirect:/toBeWateredSoon";
    }
}
