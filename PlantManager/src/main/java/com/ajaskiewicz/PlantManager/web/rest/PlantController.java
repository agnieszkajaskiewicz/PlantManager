package com.ajaskiewicz.PlantManager.web.rest;

import com.ajaskiewicz.PlantManager.model.Plant;
import com.ajaskiewicz.PlantManager.service.*;
import com.ajaskiewicz.PlantManager.web.utils.FileDeleteUtil;
import com.ajaskiewicz.PlantManager.web.utils.FileUploadUtil;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/dashboard")
public class PlantController {

    private PlantService plantService;
    private RoomService roomService;
    private WateringScheduleService wateringScheduleService;
    private UserService userService;
    private SecurityService securityService;

    @Autowired
    public PlantController(PlantService plantService, RoomService roomService, WateringScheduleService wateringScheduleService, UserService userService, SecurityService securityService) {
        this.plantService = plantService;
        this.roomService = roomService;
        this.wateringScheduleService = wateringScheduleService;
        this.userService = userService;
        this.securityService = securityService;
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() { //todo remove when no longer needed
        if (!securityService.isAuthenticated()) {
            return ResponseEntity.ok(" Nie udało się");
        }

        return ResponseEntity.ok("Udało się");
    }

    @RequestMapping
    public String showDashboard(Model model) {
        if (!securityService.isAuthenticated()) {
            return "redirect:/";
        }

        model.addAttribute("plant", plantService.findAllByUserId(userService.findIdOfLoggedUser()));
        return "dashboardPage";
    }

    @GetMapping(value = "/v2")
    public ResponseEntity<List<Plant>> getPlantsForLoggedUser() {
        if (!securityService.isAuthenticated()) { //todo to powinno być załatwione z automatu spring security
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        var plants = plantService.findAllByUserId(userService.findIdOfLoggedUser());

        return ResponseEntity.ok(plants);
    }

    @RequestMapping(path = {"/editPlant", "/editPlant/{id}"})
    public String editPlantById(Model model, @PathVariable("id") Optional<Integer> id) throws NotFoundException {
        if (!securityService.isAuthenticated()) {
            return "redirect:/";
        }

        if (id.isPresent()) {
            var plant = plantService.find(id.get());
            model.addAttribute("plant", plant);
        } else {
            model.addAttribute("plant", new Plant());
        }

        return "editPage";
    }

    @RequestMapping(path = "/addPlant", method = RequestMethod.POST)
    public String createOrUpdatePlant(Plant plant, @RequestParam("image") MultipartFile multipartFile) throws IOException {
        if (!securityService.isAuthenticated()) {
            return "redirect:/";
        }

        var filename = multipartFile.getOriginalFilename();

        if (multipartFile.isEmpty()) {
            plantService.createOrUpdatePlant(plant);
        } else {
            plant.setImageName(filename);
            var savedPlant = plantService.createOrUpdatePlant(plant);

            var uploadDirectory = "uploadedImages/" + savedPlant.getId();
            FileUploadUtil.saveFile(uploadDirectory, filename, multipartFile);
        }

        return "redirect:/dashboard";
    }

    @RequestMapping(value = "/deletePlant/{id}")
    public String deletePlantById(@PathVariable("id") Integer id) throws NotFoundException, IOException {
        if (!securityService.isAuthenticated()) {
            return "redirect:/";
        }

        var deleteDirectory = "uploadedImages/" + id;
        FileDeleteUtil.deleteFile(deleteDirectory);

        plantService.delete(id);

        return "redirect:/dashboard";
    }
}
