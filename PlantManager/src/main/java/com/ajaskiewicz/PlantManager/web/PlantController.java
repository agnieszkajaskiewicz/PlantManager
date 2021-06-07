package com.ajaskiewicz.PlantManager.web;

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

import java.util.Optional;

@Controller
@RequestMapping(value = "/dashboard")
public class PlantController {

    @Autowired
    private PlantService plantService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private WateringScheduleService wateringScheduleService;

    @RequestMapping("")
    public String showDashboard(Model model) {
        model.addAttribute("plant", plantService.findAll());
        return "dashboardPage";
    }

    @RequestMapping(path = {"/editPlant", "/editPlant/{id}"})
    public String editPlantById(Model model, @PathVariable("id") Optional<Integer> id)
            throws NotFoundException
    {
        if (id.isPresent()) {
            Plant entity = plantService.find(id.get());
            model.addAttribute("plant", entity);
        } else {
            model.addAttribute("plant", new Plant());
        }
        return "editPage";
    }

    @RequestMapping(path = "/addPlant", method = RequestMethod.POST)
    public String createOrUpdatePlant(Plant plant)
    {
        plantService.createOrUpdatePlant(plant);
        return "redirect:/dashboard";
    }

    @RequestMapping("/deletePlant/{id}")
    public String deletePlantById(Plant plant, @PathVariable("id") Optional<Integer> id) throws NotFoundException {
        plantService.delete(id.get());
        return "redirect:/dashboard";
    }

}
