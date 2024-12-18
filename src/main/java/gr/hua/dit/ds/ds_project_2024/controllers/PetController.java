package gr.hua.dit.ds.ds_project_2024.controllers;

import gr.hua.dit.ds.ds_project_2024.entities.Citizen;
import gr.hua.dit.ds.ds_project_2024.entities.Pet;
import gr.hua.dit.ds.ds_project_2024.service.CitizenService;
import gr.hua.dit.ds.ds_project_2024.service.PetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("pet")
public class PetController {

    private PetService petService;
    private CitizenService citizenService;

    public PetController(PetService petService, CitizenService citizenService) {
        this.petService = petService;
        this.citizenService = citizenService;
    }

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

    @PostMapping("/new")
    public String savePet(@ModelAttribute("pet") Pet pet, Model model) {
        petService.savePet(pet);
        model.addAttribute("pets", petService.getPets());
        return "pet/pets";
    }

    @RequestMapping("/delete/{id}")
    public String deletePet(@PathVariable Integer id, Model model) {
        petService.deletePet(id);
        model.addAttribute("pets", petService.getPets());
        return "pet/pets";
    }

    @RequestMapping("/adopt/{id}")
    public String showAdoptionRequest(@PathVariable Integer id, Model model) {
        Pet pet = petService.getPet(id);
        List<Citizen> citizens = citizenService.getCitizens();
        model.addAttribute("pet", pet);
        model.addAttribute("citizens", citizens);
        return "pet/adoptionRequest";
    }

    @PostMapping("/adopt/{id}")
    public String adoptionRequest(@PathVariable Integer id, @RequestParam(value = "citizen", required = true) int citizenId, Model model) {
        System.out.println(citizenId);
        Citizen citizen = citizenService.getCitizen(citizenId);
        Pet pet = petService.getPet(id);
        System.out.println(pet);
        petService.requestByCitizen(id, citizen);
    }

}
