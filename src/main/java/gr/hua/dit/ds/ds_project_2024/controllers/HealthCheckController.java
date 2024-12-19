package gr.hua.dit.ds.ds_project_2024.controllers;

import gr.hua.dit.ds.ds_project_2024.entities.HealthCheck;
import gr.hua.dit.ds.ds_project_2024.entities.Pet;
import gr.hua.dit.ds.ds_project_2024.entities.Veterinarian;
import gr.hua.dit.ds.ds_project_2024.service.HealthCheckService;
import gr.hua.dit.ds.ds_project_2024.service.PetService;
import gr.hua.dit.ds.ds_project_2024.service.VeterinarianService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("healthCheck")
public class HealthCheckController {

    private final PetService petService;
    private final VeterinarianService veterinarianService;
    private HealthCheckService healthCheckService;

    public HealthCheckController(HealthCheckService healthCheckService, PetService petService, VeterinarianService veterinarianService) {
        this.healthCheckService = healthCheckService;
        this.petService = petService;
        this.veterinarianService = veterinarianService;
    }

    @RequestMapping()
    public String showHealthChecks(Model model) {
        model.addAttribute("healthChecks", healthCheckService.getHealthChecks());
        return "healthCheck/healthChecks";
    }

    @RequestMapping("/{id}")
    public String showHealthCheck(@PathVariable Integer id, Model model) {
        HealthCheck healthCheck = healthCheckService.getHealthCheck(id);
        model.addAttribute("healthChecks", healthCheck);
        return "healthCheck/healthChecks";
    }

    @RequestMapping("/new")
    public String addHealthCheck(Model model) {
        HealthCheck healthCheck = new HealthCheck();
        List<Pet> pets = petService.getPets();
        List<Veterinarian> veterinarians = veterinarianService.getVeterinarians();
        model.addAttribute("healthCheck", healthCheck);
        model.addAttribute("pets", pets);
        model.addAttribute("veterinarians", veterinarians);
        return "healthCheck/healthCheck";
    }

    @PostMapping("/new")
    public String saveHealthCheck(@ModelAttribute("healthCheck") HealthCheck healthCheck, Model model) {
        healthCheckService.saveHealthCheck(healthCheck);
        model.addAttribute("healthChecks", healthCheckService.getHealthChecks());
        return "healthCheck/healthChecks";
    }

    @RequestMapping("/delete/{id}")
    public String deleteHealthCheck(@PathVariable Integer id, Model model) {
        healthCheckService.deleteHealthCheck(id);
        model.addAttribute("healthChecks", healthCheckService.getHealthChecks());
        return "healthCheck/healthChecks";
    }
}
