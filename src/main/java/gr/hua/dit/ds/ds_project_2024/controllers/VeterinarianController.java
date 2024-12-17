package gr.hua.dit.ds.ds_project_2024.controllers;

import gr.hua.dit.ds.ds_project_2024.entities.Veterinarian;
import gr.hua.dit.ds.ds_project_2024.service.VeterinarianService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("veterinarian")
public class VeterinarianController {

    private VeterinarianService veterinarianService;

    public VeterinarianController(VeterinarianService veterinarianService) {
        this.veterinarianService = veterinarianService;
    }

    @RequestMapping()
    public String showVeterinarians(Model model) {
        model.addAttribute("veterinarians", veterinarianService.getVeterinarians());
        return "veterinarian/veterinarians";
    }

    @RequestMapping("/{id}")
    public String showVeterinarian(@PathVariable Integer id, Model model) {
        Veterinarian veterinarian = veterinarianService.getVeterinarian(id);
        model.addAttribute("veterinarians", veterinarian);
        return "veterinarian/veterinarians";
    }

    @RequestMapping("/new")
    public String addVeterinarian(Model model) {
        Veterinarian veterinarian = new Veterinarian();
        model.addAttribute("veterinarian", veterinarian);
        return "veterinarian/veterinarian";
    }

    @PostMapping("/new")
    public String saveVeterinarian(@ModelAttribute("veterinarian") Veterinarian veterinarian, Model model) {
        veterinarianService.saveVeterinarian(veterinarian);
        model.addAttribute("veterinarians", veterinarianService.getVeterinarians());
        return "veterinarian/veterinarians";
    }

    @RequestMapping("/delete/{id}")
    public String deleteVeterinarian(@PathVariable Integer id, Model model) {
        veterinarianService.deleteVeterinarian(id);
        model.addAttribute("veterinarians", veterinarianService.getVeterinarians());
        return "veterinarian/veterinarians";
    }
}
