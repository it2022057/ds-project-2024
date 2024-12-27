package gr.hua.dit.ds.ds_project_2024.controllers;

import gr.hua.dit.ds.ds_project_2024.entities.Shelter;
import gr.hua.dit.ds.ds_project_2024.service.ShelterService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("shelter")
public class ShelterController {

    private ShelterService shelterService;

    public ShelterController(ShelterService shelterService) {
        this.shelterService = shelterService;
    }

    @GetMapping()
    public String showShelters(Model model) {
        model.addAttribute("shelters", shelterService.getShelters());
        return "shelter/shelters";
    }

    @GetMapping("/{id}")
    public String showShelter(@PathVariable Integer id, Model model) {
        Shelter shelter = shelterService.getShelter(id);
        model.addAttribute("shelters", shelter);
        return "shelter/shelters";
    }

    @GetMapping("/new")
    public String addShelter(Model model) {
        Shelter shelter = new Shelter();
        model.addAttribute("shelter", shelter);
        return "shelter/shelter";
    }

    @PostMapping("/new")
    public String saveShelter(@ModelAttribute("shelter") Shelter shelter, Model model) {
        shelterService.saveShelter(shelter);
        model.addAttribute("shelters", shelterService.getShelters());
        return "shelter/shelters";
    }

    @GetMapping("/delete/{id}")
    public String deleteShelter(@PathVariable Integer id, Model model) {
        shelterService.deleteShelter(id);
        model.addAttribute("shelters", shelterService.getShelters());
        return "shelter/shelters";
    }
}
