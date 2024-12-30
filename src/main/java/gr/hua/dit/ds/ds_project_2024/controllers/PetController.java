package gr.hua.dit.ds.ds_project_2024.controllers;

import gr.hua.dit.ds.ds_project_2024.entities.Adoption;
import gr.hua.dit.ds.ds_project_2024.entities.Pet;
import gr.hua.dit.ds.ds_project_2024.entities.Status;
import gr.hua.dit.ds.ds_project_2024.service.CitizenService;
import gr.hua.dit.ds.ds_project_2024.service.PetService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("pet")
public class PetController {

    private PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping()
    public String showPets(Model model) {
        model.addAttribute("pets", petService.getPets());
        return "pet/pets";
    }

    @GetMapping("/{id}")
    public String showPet(@PathVariable Integer id, Model model) {
        Pet pet = petService.getPet(id);
        model.addAttribute("pets", pet);
        return "pet/pets";
    }

    @GetMapping("/new")
    public String addPet(Model model) {
        Pet pet = new Pet();
        model.addAttribute("pet", pet);
        return "pet/pet";
    }

    @PostMapping("/new")
    public String savePet(@ModelAttribute("pet") Pet pet, Principal principal, Model model) {
        String username = principal.getName();
        petService.savePet(pet, username);
        model.addAttribute("pets", petService.getPets());
        return "pet/pets";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/approve/{id}")
    public String acceptPet(@PathVariable Integer id, Model model) {
        Pet pet = petService.getPet(id);
        pet.setApprovalStatus(Status.APPROVED);
        model.addAttribute("pets", petService.getPets());
        return "pet/pets";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/reject/{id}")
    public String rejectPet(@PathVariable Integer id, Model model) {
        Pet pet = petService.getPet(id);
        pet.setApprovalStatus(Status.REJECTED);
        model.addAttribute("pets", petService.getPets());
        return "pet/pets";
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/delete/{id}")
    public String deletePet(@PathVariable Integer id, Model model) {
        petService.deletePet(id);
        model.addAttribute("pets", petService.getPets());
        return "pet/pets";
    }
}
