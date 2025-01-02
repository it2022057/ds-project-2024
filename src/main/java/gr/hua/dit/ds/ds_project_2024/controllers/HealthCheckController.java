package gr.hua.dit.ds.ds_project_2024.controllers;

import gr.hua.dit.ds.ds_project_2024.entities.HealthCheck;
import gr.hua.dit.ds.ds_project_2024.entities.Pet;
import gr.hua.dit.ds.ds_project_2024.entities.Status;
import gr.hua.dit.ds.ds_project_2024.entities.Veterinarian;
import gr.hua.dit.ds.ds_project_2024.service.HealthCheckService;
import gr.hua.dit.ds.ds_project_2024.service.PetService;
import gr.hua.dit.ds.ds_project_2024.service.VeterinarianService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("healthCheck")
public class HealthCheckController {

    private PetService petService;
    private VeterinarianService veterinarianService;
    private HealthCheckService healthCheckService;

    public HealthCheckController(HealthCheckService healthCheckService, PetService petService, VeterinarianService veterinarianService) {
        this.healthCheckService = healthCheckService;
        this.petService = petService;
        this.veterinarianService = veterinarianService;
    }

    @GetMapping()
    public String showHealthChecks(Model model) {
        model.addAttribute("healthChecks", healthCheckService.getHealthChecks());
        return "healthCheck/healthChecks";
    }

    @GetMapping("/{id}")
    public String showHealthCheck(@PathVariable Integer id, Model model) {
        HealthCheck healthCheck = healthCheckService.getHealthCheck(id);
        model.addAttribute("healthChecks", healthCheck);
        return "healthCheck/healthChecks";
    }

    @Secured("ROLE_VETERINARIAN")
    @GetMapping("/edit/{id}")
    public String showEditHealthCheck(@PathVariable Integer id, Model model) {
        HealthCheck healthCheck = healthCheckService.getHealthCheck(id);
        model.addAttribute("healthCheck", healthCheck);
        model.addAttribute("pet", healthCheck.getPetExamined());
        model.addAttribute("veterinarian", healthCheck.getByVeterinarian());
        return "healthCheck/editHealthCheck";
    }

    @Secured("ROLE_VETERINARIAN")
    @PostMapping("/edit/{id}")
    public String editHealthCheck(@PathVariable Integer id, @ModelAttribute("healthCheck") HealthCheck healthCheck, Model model) {
        HealthCheck edit_healthCheck = healthCheckService.getHealthCheck(id);
        edit_healthCheck.setStatus(healthCheck.getStatus());
        edit_healthCheck.setDetails(healthCheck.getDetails());
        edit_healthCheck.setPetExamined(healthCheck.getPetExamined());
        edit_healthCheck.setByVeterinarian(healthCheck.getByVeterinarian());
        healthCheckService.updateHealthCheck(edit_healthCheck);

        model.addAttribute("healthChecks", healthCheckService.getHealthChecks());
        return "healthCheck/healthChecks";
    }

    @Secured("ROLE_VETERINARIAN")
    @GetMapping("/new")
    public String addHealthCheck(Principal loggedInUser, Model model) {
        Veterinarian veterinarian = veterinarianService.getVeterinarianByUsername(loggedInUser.getName());
        HealthCheck healthCheck = new HealthCheck();
        model.addAttribute("healthCheck", healthCheck);
        model.addAttribute("pets", petService.getPets());
        model.addAttribute("veterinarians", veterinarian);
        return "healthCheck/healthCheck";
    }

    @Secured("ROLE_VETERINARIAN")
    @PostMapping("/new")
    public String saveHealthCheck(@ModelAttribute("healthCheck") HealthCheck healthCheck, Model model) {
        healthCheckService.saveHealthCheck(healthCheck);
        model.addAttribute("healthChecks", healthCheckService.getHealthChecks());
        return "healthCheck/healthChecks";
    }

    @Secured("ROLE_VETERINARIAN")
    @GetMapping("/delete/{id}")
    public String deleteHealthCheck(@PathVariable Integer id, Model model) {
        healthCheckService.deleteHealthCheck(id);
        model.addAttribute("healthChecks", healthCheckService.getHealthChecks());
        return "healthCheck/healthChecks";
    }
}
