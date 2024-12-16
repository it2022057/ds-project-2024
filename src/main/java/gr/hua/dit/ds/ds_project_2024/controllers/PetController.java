package gr.hua.dit.ds.ds_project_2024.controllers;

import gr.hua.dit.ds.ds_project_2024.entities.Pet;
import gr.hua.dit.ds.ds_project_2024.service.CitizenService;
import gr.hua.dit.ds.ds_project_2024.service.PetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("pet")
public class PetController {

    private PetService petService;
    private CitizenService citizenService;

    @RequestMapping()
    public String showPets(Model model) {
        model.addAttribute("pets", petService.getPets());
        return "pet/pets";
    }

    @RequestMapping("/{id}")
    public String showPet(@PathVariable Integer id, Model model) {
        Pet pet = petService.getPet(id);
        model.addAttribute("pets", pet);
        return "pet/pets";
    }

    @RequestMapping("/new")
    public String addPet(Model model) {
        Pet pet = new Pet();
        model.addAttribute("pet", pet);
        return "pet/pet";
    }

    @RequestMapping("/delete/{id}")
    public String deletePet(@PathVariable Integer id, Model model) {
        petService.deletePet(id);
        model.addAttribute("pets", petService.getPets());
        return "pet/pet";
    }
}
