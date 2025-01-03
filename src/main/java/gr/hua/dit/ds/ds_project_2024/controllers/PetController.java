package gr.hua.dit.ds.ds_project_2024.controllers;

import gr.hua.dit.ds.ds_project_2024.entities.*;
import gr.hua.dit.ds.ds_project_2024.service.*;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("pet")
public class PetController {

    private final VeterinarianService veterinarianService;
    private ShelterService shelterService;
    private CitizenService citizenService;
    private UserService userService;
    private PetService petService;

    public PetController(PetService petService, UserService userService, CitizenService citizenService, ShelterService shelterService, VeterinarianService veterinarianService) {
        this.petService = petService;
        this.userService = userService;
        this.citizenService = citizenService;
        this.shelterService = shelterService;
        this.veterinarianService = veterinarianService;
    }

    @GetMapping()
    public String showPets(Principal loggedInUser, Model model) {
        List<Pet> allPets = petService.getPets();

        User user = userService.getUserByUsername(loggedInUser.getName());
        Citizen citizen = citizenService.getCitizen(user.getId());
        Shelter shelter = shelterService.getShelter(user.getId());
        Veterinarian veterinarian = veterinarianService.getVeterinarian(user.getId());

        if (citizen != null) {
            model.addAttribute("pets", petService.getApprovedPets(allPets));
        } else if (shelter != null) {
            model.addAttribute("pets", shelter.getPetsAvailable());
        } else if(veterinarian != null) {
            veterinarianService.canExamine(allPets, veterinarian);
            model.addAttribute("pets", allPets);
        } else {
            model.addAttribute("pets", allPets);
        }
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
    public String savePet(@ModelAttribute("pet") Pet pet, Principal loggedInUser, Model model) {
        Shelter shelter = shelterService.getShelterByUsername(loggedInUser.getName());
        petService.savePet(pet, shelter);
        model.addAttribute("pets", petService.getPets());
        return "pet/pets";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/approve/{id}")
    public String acceptPet(@PathVariable Integer id, Model model) {
        Pet pet = petService.getPet(id);
        pet.setApprovalStatus(Status.APPROVED);
        pet.getOnShelter().getPetsAvailable().add(pet);
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
    @RequestMapping("/delete/{id}")
    public String deletePet(@PathVariable Integer id, Model model) {
        petService.deletePet(petService.getPet(id));
        model.addAttribute("pets", petService.getPets());
        return "pet/pets";
    }
}
