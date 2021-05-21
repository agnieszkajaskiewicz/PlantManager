package com.ajaskiewicz.PlantManager.web;

import com.ajaskiewicz.PlantManager.model.Plant;
import com.ajaskiewicz.PlantManager.model.Room;
import com.ajaskiewicz.PlantManager.model.WateringSchedule;
import com.ajaskiewicz.PlantManager.service.PlantService;
import com.ajaskiewicz.PlantManager.service.RoomService;
import com.ajaskiewicz.PlantManager.service.WateringScheduleService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.Optional;

@Controller
@RequestMapping(value = "/dashboard")
public class AddPlantController {

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

    /*
    @PostMapping("/addPlant")
    public String addPlant(
            @ModelAttribute Plant plant,
            Room room,
            WateringSchedule wateringSchedule,
            Model model) {

        System.out.println(plant);

        plant.setRoom(room);
        plant.setWateringSchedule(wateringSchedule);
        roomService.save(room);
        wateringScheduleService.save(wateringSchedule);
        plantService.save(plant);

        model.addAttribute("plant", plantService.findAll());

        return "redirect:/dashboard";
    }

    @GetMapping("/forward/{id}")
    public String forwardToPlantEdition(@PathVariable("id") Integer id, Model model) throws NotFoundException {
        Plant plant = plantService.find(id);
        model.addAttribute("plant", plant);
        return "editPage";
    }

    @RequestMapping("/editPlant")
    public String editPlant(
            @RequestParam("id") Integer id,
            @RequestParam("plantName") String plantName,
            @RequestParam("Room.roomName") String room,
            @RequestParam("WateringSchedule.watering_interval") Integer wateringInterval,
            @RequestParam("WateringSchedule.last_watered_date") Date lastWateredDate,
            Model model) {

        Plant editedPlant = new Plant(id, plantName, room, wateringInterval, lastWateredDate);

        plantService.save(editedPlant);
        model.addAttribute("plant", editedPlant);
        model.addAttribute("plant", plantService.findAll());
        return "dashboardPage";
    }

     */

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
    public String createOrUpdatePlant(Plant plant, Room room, WateringSchedule wateringSchedule)
    {
        plantService.createOrUpdatePlant(plant, room, wateringSchedule);
        return "redirect:/dashboard";
    }

}
