package gr.hua.dit.ds.ds_project_2024.controllers;

import gr.hua.dit.ds.ds_project_2024.entities.Pet;
import gr.hua.dit.ds.ds_project_2024.entities.Status;
import gr.hua.dit.ds.ds_project_2024.entities.Veterinarian;
import gr.hua.dit.ds.ds_project_2024.service.HealthCheckService;
import gr.hua.dit.ds.ds_project_2024.service.PetService;
import gr.hua.dit.ds.ds_project_2024.service.VeterinarianService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("veterinarian")
public class VeterinarianController {

    private VeterinarianService veterinarianService;
    private HealthCheckService healthCheckService;
    private PetService petService;

    public VeterinarianController(VeterinarianService veterinarianService, HealthCheckService healthCheckService, PetService petService) {
        this.veterinarianService = veterinarianService;
        this.healthCheckService = healthCheckService;
        this.petService = petService;
    }

    @RequestMapping()
    public String showVeterinarians(Model model) {
        model.addAttribute("veterinarians", veterinarianService.getVeterinarians());
        return "veterinarian/veterinarians";
    }

    @GetMapping("/{id}")
    public String showVeterinarian(@PathVariable Integer id, Model model) {
        Veterinarian veterinarian = veterinarianService.getVeterinarian(id);
        model.addAttribute("veterinarians", veterinarian);
        return "veterinarian/veterinarians";
    }

    @GetMapping("/new")
    public String addVeterinarian(Model model) {
        Veterinarian veterinarian = new Veterinarian();
        model.addAttribute("veterinarian", veterinarian);
        return "veterinarian/veterinarian";
    }

    @PostMapping("/new")
    public String saveVeterinarian(@ModelAttribute("veterinarian") Veterinarian veterinarian, Model model) {
        veterinarianService.saveVeterinarian(veterinarian);
        String message = "Veterinarian " + veterinarian.getId() + " saved successfully!";
        model.addAttribute("msg", message);
        return "index";
    }

    @GetMapping("/delete/{id}")
    public String deleteVeterinarian(@PathVariable Integer id, Model model) {
        veterinarianService.deleteVeterinarian(id);
        model.addAttribute("veterinarians", veterinarianService.getVeterinarians());
        return "veterinarian/veterinarians";
    }

    @GetMapping("/examine/{pet_id}")
    public String showExamine(@PathVariable Integer pet_id, Model model) {
        Pet pet = petService.getPet(pet_id);
        model.addAttribute("pet", pet);
        return "veterinarian/examine";
    }

    @PostMapping("/examine/{pet_id}")
    public String examine(@PathVariable Integer pet_id, @ModelAttribute("status") Status status, @ModelAttribute("details")String details, Model model, Principal loggedInUser) {
        Pet pet = petService.getPet(pet_id);
        Veterinarian veterinarian = veterinarianService.getVeterinarianByUsername(loggedInUser.getName());
        veterinarianService.examination(pet, status, details, veterinarian);
        model.addAttribute("healthChecks", healthCheckService.getHealthChecks());
        return "healthCheck/healthChecks";
    }

    @GetMapping("/{vet_id}/healthChecks")
    public String showHealthChecks(@PathVariable Integer vet_id, Model model) {
        Veterinarian veterinarian = veterinarianService.getVeterinarian(vet_id);
        model.addAttribute("healthChecks", veterinarian.getHealthTests());
        return "healthCheck/healthChecks";
    }
}
