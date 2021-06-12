package com.ajaskiewicz.PlantManager.web.rest;

import com.ajaskiewicz.PlantManager.model.Plant;
import com.ajaskiewicz.PlantManager.service.PlantService;
import com.ajaskiewicz.PlantManager.service.RoomService;
import com.ajaskiewicz.PlantManager.service.WateringScheduleService;
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

    @Autowired
    public WateringController(PlantService plantService, RoomService roomService, WateringScheduleService wateringScheduleService) {
        this.plantService = plantService;
        this.roomService = roomService;
        this.wateringScheduleService = wateringScheduleService;
    }

    @RequestMapping
    public String showToBeWateredSoon(Model model) {
        model.addAttribute("plant", plantService.findPlantsToBeWateredSoon());
        return "waterSoonPage";
    }

    @RequestMapping("/confirm/{id}")
    public String showConfirmWateringView(@PathVariable("id") Integer id, Model model) throws NotFoundException {
        var plant = plantService.find(id);
        model.addAttribute("plant", plant);

        return "wateringConfirmationPage";
    }

    @RequestMapping(path = "/confirmWatering", method = RequestMethod.POST)
    public String confirmWateringToday(Plant plant) throws NotFoundException {
        plantService.updateLastWateredDate(plant);
        return "redirect:/toBeWateredSoon";
    }


}
