package gr.hua.dit.ds.ds_project_2024.controllers;

import gr.hua.dit.ds.ds_project_2024.entities.Veterinarian;
import gr.hua.dit.ds.ds_project_2024.service.VeterinarianService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("veterinarian")
public class VeterinarianController {

    private VeterinarianService veterinarianService;

    public VeterinarianController(VeterinarianService veterinarianService) {
        this.veterinarianService = veterinarianService;
    }

    @GetMapping()
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

    @GetMapping("/healthTests/{id}")
    public String showHealthTests(@PathVariable Integer id, Model model) {
        Veterinarian veterinarian = veterinarianService.getVeterinarian(id);
        model.addAttribute("healthTests", veterinarian.getHealthTests());
        return "veterinarian/healthTests";
    }
}
