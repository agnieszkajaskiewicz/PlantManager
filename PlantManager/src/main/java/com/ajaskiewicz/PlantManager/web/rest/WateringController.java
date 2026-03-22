package com.ajaskiewicz.PlantManager.web.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ajaskiewicz.PlantManager.model.Plant;
import com.ajaskiewicz.PlantManager.model.PlantCardDTO;
import com.ajaskiewicz.PlantManager.model.mapper.PlantMapper;
import com.ajaskiewicz.PlantManager.service.PlantService;
import com.ajaskiewicz.PlantManager.service.SecurityService;
import com.ajaskiewicz.PlantManager.service.UserService;

import javassist.NotFoundException;

@Controller
@RequestMapping(value = "/toBeWateredSoon") //to do: remove old endpoint later
public class WateringController {

    private final PlantService plantService;
    private final UserService userService;
    private final SecurityService securityService;
    private final PlantMapper plantMapper;

    @Autowired
    public WateringController(PlantService plantService, UserService userService, SecurityService securityService, PlantMapper plantMapper) {
        this.plantService = plantService;
        this.userService = userService;
        this.securityService = securityService;
        this.plantMapper = plantMapper;
    }

    @RequestMapping
    public String showToBeWateredSoon(Model model) {
        if (!securityService.isAuthenticated()) { return "redirect:/"; }

        model.addAttribute("plant", plantService.findPlantsToBeWateredSoon(userService.findIdOfLoggedUser()));

        return "waterSoonPage";
    }

    @GetMapping(value = "/v2")
    public ResponseEntity<List<PlantCardDTO>> getPlantsToBeWateredSoon() {
        List<Plant> plants = plantService.findPlantsToBeWateredSoon(userService.findIdOfLoggedUser());
        List<PlantCardDTO> plantCardDTOs = plants.stream()
                .map(plantMapper::plantToPlantCardDto)
                .toList();
        return ResponseEntity.ok(plantCardDTOs);
    }

    @RequestMapping("/confirm/{id}")
    public String showConfirmWateringView(@PathVariable("id") Long id, Model model) throws NotFoundException {
        if (!securityService.isAuthenticated()) { return "redirect:/"; }

        Plant plant = plantService.find(id);
        model.addAttribute("plant", plant);

        return "wateringConfirmationPage";
    }

    @RequestMapping(path = "/confirmWatering", method = RequestMethod.POST)
    public String confirmWateringToday(Plant plant) throws NotFoundException {
        if (!securityService.isAuthenticated()) { return "redirect:/"; }

        plantService.updateLastWateredDate(plant);

        return "redirect:/toBeWateredSoon";
    }

    @PostMapping("/confirmWatering/v2/{id}")
    public ResponseEntity<PlantCardDTO> confirmWateringTodayV2(@PathVariable("id") Long id) throws NotFoundException {
        Plant plant = plantService.find(id);
        if (!plant.getUser().getId().equals(userService.findIdOfLoggedUser())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Plant updatedPlant = plantService.updateLastWateredDate(plant);
        PlantCardDTO plantCardDTO = plantMapper.plantToPlantCardDto(updatedPlant);
        return ResponseEntity.ok(plantCardDTO);
    }
}
