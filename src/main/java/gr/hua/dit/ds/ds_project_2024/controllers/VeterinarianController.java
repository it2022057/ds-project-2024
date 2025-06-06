package gr.hua.dit.ds.ds_project_2024.controllers;

import gr.hua.dit.ds.ds_project_2024.entities.Pet;
import gr.hua.dit.ds.ds_project_2024.entities.Status;
import gr.hua.dit.ds.ds_project_2024.entities.Veterinarian;
import gr.hua.dit.ds.ds_project_2024.service.HealthCheckService;
import gr.hua.dit.ds.ds_project_2024.service.MailService;
import gr.hua.dit.ds.ds_project_2024.service.PetService;
import gr.hua.dit.ds.ds_project_2024.service.VeterinarianService;
import org.springframework.security.access.annotation.Secured;
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
    private MailService mailService;

    public VeterinarianController(VeterinarianService veterinarianService, HealthCheckService healthCheckService, PetService petService, MailService mailService) {
        this.veterinarianService = veterinarianService;
        this.healthCheckService = healthCheckService;
        this.petService = petService;
        this.mailService = mailService;
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
        mailService.sendMail(veterinarian.getEmail(), "Veterinarian registration", "Welcome to our pet adoption family!");
        model.addAttribute("msg", message);
        return "home";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/delete/{id}")
    public String deleteVeterinarian(@PathVariable Integer id, Model model) {
        mailService.sendMail(veterinarianService.getVeterinarian(id).getEmail(), "Account delete", "Hi, we just want to inform you that your account got deleted from our webpage");
        veterinarianService.deleteVeterinarian(id);
        model.addAttribute("veterinarians", veterinarianService.getVeterinarians());
        return "veterinarian/veterinarians";
    }

    @Secured("ROLE_VETERINARIAN")
    @GetMapping("/examine/{pet_id}")
    public String showExamine(@PathVariable Integer pet_id, Model model) {
        Pet pet = petService.getPet(pet_id);
        model.addAttribute("pet", pet);
        return "veterinarian/examine";
    }

    @Secured("ROLE_VETERINARIAN")
    @PostMapping("/examine/{pet_id}")
    public String examine(@PathVariable Integer pet_id, @ModelAttribute("status") Status status, @ModelAttribute("details")String details, Model model, Principal loggedInUser) {
        Pet pet = petService.getPet(pet_id);
        Veterinarian veterinarian = veterinarianService.getVeterinarianByUsername(loggedInUser.getName());
        veterinarianService.examination(pet, status, details, veterinarian);
        mailService.sendMail(pet.getOnShelter().getEmail(), "Examination", "Pet " + pet.getName() + " has just been examined by " + veterinarian.getFirstName() + " " + veterinarian.getLastName() + "!");
        model.addAttribute("healthChecks", healthCheckService.getHealthChecks());
        return "healthCheck/healthChecks";
    }

    @Secured("ROLE_VETERINARIAN")
    @GetMapping("/{vet_id}/healthChecks")
    public String showHealthChecks(@PathVariable Integer vet_id, Model model) {
        Veterinarian veterinarian = veterinarianService.getVeterinarian(vet_id);
        model.addAttribute("healthChecks", veterinarian.getHealthTests());
        return "healthCheck/healthChecks";
    }
}
