package com.ajaskiewicz.PlantManager.web.rest;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ajaskiewicz.PlantManager.model.Plant;
import com.ajaskiewicz.PlantManager.model.PlantCardDTO;
import com.ajaskiewicz.PlantManager.model.PlantCreationDTO;
import com.ajaskiewicz.PlantManager.model.mapper.PlantMapper;
import com.ajaskiewicz.PlantManager.service.PlantService;
import com.ajaskiewicz.PlantManager.service.SecurityService;
import com.ajaskiewicz.PlantManager.service.UserService;
import com.ajaskiewicz.PlantManager.web.utils.FileDeleteUtil;
import com.ajaskiewicz.PlantManager.web.utils.FileUploadUtil;

import jakarta.validation.Valid;
import javassist.NotFoundException;

@Controller
@RequestMapping(value = "/dashboard")
public class PlantController {

    private static final String UPLOADED_IMAGES_PATH = "uploadedImages/";

    private final PlantService plantService;
    private final UserService userService;
    private final SecurityService securityService;
    private final PlantMapper plantMapper;

    @Autowired
    public PlantController(PlantService plantService, UserService userService, SecurityService securityService, PlantMapper plantMapper) {
        this.plantService = plantService;
        this.userService = userService;
        this.securityService = securityService;
        this.plantMapper = plantMapper;
    }

    @RequestMapping // old endpoint, to be removed
    public String showDashboard(Model model) {
        if (!securityService.isAuthenticated()) {
            return "redirect:/";
        }

        model.addAttribute("plant", plantService.findAllByUserId(userService.findIdOfLoggedUser()));
        return "dashboardPage";
    }

    @GetMapping(value = "/v2", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PlantCardDTO>> getPlantsForLoggedUser() {
        List<Plant> plants = plantService.findAllByUserId(userService.findIdOfLoggedUser());
        List<PlantCardDTO> plantDtos = plants.stream()
                .map(plantMapper::plantToPlantCardDto)
                .toList();

        return ResponseEntity.ok(plantDtos);
    }

    @GetMapping(value = "/v2/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PlantCardDTO> getPlantByIdForLoggedUser(@PathVariable("id") Long id) throws NotFoundException {
        Plant plant = plantService.find(id);
        if (!plant.getUser().getId().equals(userService.findIdOfLoggedUser())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        PlantCardDTO plantDto = plantMapper.plantToPlantCardDto(plant);
        return ResponseEntity.ok(plantDto);
    }

    @RequestMapping(path = {"/editPlant", "/editPlant/{id}"}) // old endpoint, to be removed
    public String editPlantById(Model model, @PathVariable("id") Optional<Long> id) throws NotFoundException {
        if (!securityService.isAuthenticated()) {
            return "redirect:/";
        }

        if (id.isPresent()) {
            Plant plant = plantService.find(id.get());
            model.addAttribute("plant", plant);
        } else {
            model.addAttribute("plant", new Plant());
        }

        return "editPage";
    }

    @PutMapping(path = "/updatePlant/v2/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PlantCardDTO> updatePlantByIdV2(@PathVariable("id") Long id, @RequestBody @Valid PlantCreationDTO plantCreationDTO) throws NotFoundException {
        Plant existingPlant = plantService.find(id);

        if (!existingPlant.getUser().getId().equals(userService.findIdOfLoggedUser())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        plantMapper.updatePlantFromDto(plantCreationDTO, existingPlant);

        Plant savedPlant = plantService.createOrUpdatePlant(existingPlant);
        PlantCardDTO plantDto = plantMapper.plantToPlantCardDto(savedPlant);

        return ResponseEntity.ok(plantDto);
    }

    @RequestMapping(path = "/addPlant", method = RequestMethod.POST) // old endpoint, to be removed
    public String createOrUpdatePlant(Plant plant, @RequestParam("image") MultipartFile multipartFile) throws IOException {
        if (!securityService.isAuthenticated()) {
            return "redirect:/";
        }

        String filename = multipartFile.getOriginalFilename();

        if (multipartFile.isEmpty()) {
            plantService.createOrUpdatePlant(plant);
        } else {
            plant.setImageName(filename);
            Plant savedPlant = plantService.createOrUpdatePlant(plant);

            String uploadDirectory = UPLOADED_IMAGES_PATH + savedPlant.getId();
            FileUploadUtil.saveFile(uploadDirectory, filename, multipartFile);
        }

        return "redirect:/dashboard";
    }

    @PostMapping(path = "/addPlant/v2", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PlantCardDTO> createPlant(@RequestBody @Valid PlantCreationDTO plantCreationDTO) {
        Plant plantToSave = plantMapper.plantToPlantEntity(plantCreationDTO);
        Plant savedPlant = plantService.createOrUpdatePlant(plantToSave);
        PlantCardDTO plantDto = plantMapper.plantToPlantCardDto(savedPlant);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(plantDto);
    }

    @RequestMapping(value = "/deletePlant/{id}") // old endpoint, to be removed
    public String deletePlantById(@PathVariable("id") Long id) throws NotFoundException, IOException {
        if (!securityService.isAuthenticated()) {
            return "redirect:/";
        }

        deletePlantImage(id);

        plantService.delete(id);

        return "redirect:/dashboard";
    }

    private void deletePlantImage(Long id) throws IOException {
        String deleteDirectory = UPLOADED_IMAGES_PATH + id;
        FileDeleteUtil.deleteFile(deleteDirectory);
    }

    @DeleteMapping(value = "/deletePlant/v2/{id}")
    public ResponseEntity<?> deletePlantByIdV2(@PathVariable("id") Long id) throws IOException, NotFoundException {
        Plant plant = plantService.find(id);
        if (!plant.getUser().getId().equals(userService.findIdOfLoggedUser())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        deletePlantImage(id);
        plantService.delete(id);
        return ResponseEntity.ok().build();
    }
}
