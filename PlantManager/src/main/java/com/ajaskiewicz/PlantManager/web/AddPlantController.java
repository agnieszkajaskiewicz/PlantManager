package com.ajaskiewicz.PlantManager.web;

import com.ajaskiewicz.PlantManager.model.Plant;
import com.ajaskiewicz.PlantManager.model.Room;
import com.ajaskiewicz.PlantManager.model.User;
import com.ajaskiewicz.PlantManager.repository.PlantRepository;
import com.ajaskiewicz.PlantManager.repository.RoomRepository;
import com.ajaskiewicz.PlantManager.repository.UserRepository;
import com.ajaskiewicz.PlantManager.repository.WateringScheduleRepository;
import com.ajaskiewicz.PlantManager.service.PlantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/dashboard")
public class AddPlantController {
    private PlantRepository plantRepository;
    private WateringScheduleRepository wateringScheduleRepository;
    private RoomRepository roomRepository;
    private UserRepository userRepository;

    private Plant plant;

    @Autowired
    private PlantService plantService;

    @Autowired
    public void addPlantController(PlantRepository plantRepository, WateringScheduleRepository wateringScheduleRepository, RoomRepository roomRepository) {
        this.plantRepository = plantRepository;
        this.wateringScheduleRepository = wateringScheduleRepository;
        this.roomRepository = roomRepository;
    }

    @PostMapping("/addPlant")
    public String addPlant(
            @ModelAttribute Plant plant,
            Room room,
            Model model) {

        System.out.println(plant);

        plant.setRoom(room);
        roomRepository.save(room);
        plantRepository.save(plant);

        model.addAttribute("plant", plantRepository.findAll());

        return "redirect:/dashboard";
    }

    @RequestMapping("")
    public String showDashboard(Model model) {
        model.addAttribute("plant", plantRepository.findAll());
        return "dashboardPage";
    }



}
