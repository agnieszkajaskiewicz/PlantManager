package com.ajaskiewicz.PlantManager.web;

import com.ajaskiewicz.PlantManager.model.Plant;
import com.ajaskiewicz.PlantManager.model.WateringSchedule;
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
@RequestMapping(value = "/toBeWateredSoon")
public class WateringController {

    @Autowired
    private PlantService plantService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private WateringScheduleService wateringScheduleService;

    @RequestMapping("")
    public String showToBeWateredSoon(Model model) {
        model.addAttribute("plant", plantService.findPlantsToBeWateredSoon());
//        model.addAttribute("differenceInDays", plantService.differenceInDays());
        return "waterSoonPage";
    }

    @RequestMapping("/confirm/{id}")
    public String showConfirmWateringView(Plant plant, @PathVariable("id") Optional<Integer> id, Model model) throws NotFoundException {
        Plant entity = plantService.find(id.get());
        model.addAttribute("plant", entity);

        System.out.println(plant);
        return "wateringConfirmationPage";
    }

    @RequestMapping(path = "/confirmWatering", method = RequestMethod.POST)
    public String confirmWateringToday(Plant plant) throws NotFoundException {
        plantService.updateLastWateredDate(plant);
        return "redirect:/toBeWateredSoon";
    }


}
