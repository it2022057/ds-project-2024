package gr.hua.dit.ds.ds_project_2024.controllers;

import gr.hua.dit.ds.ds_project_2024.entities.*;
import gr.hua.dit.ds.ds_project_2024.service.CitizenService;
import gr.hua.dit.ds.ds_project_2024.service.PetService;
import gr.hua.dit.ds.ds_project_2024.service.ShelterService;
import gr.hua.dit.ds.ds_project_2024.service.UserService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("pet")
public class PetController {

    private ShelterService shelterService;
    private CitizenService citizenService;
    private UserService userService;
    private PetService petService;

    public PetController(PetService petService, UserService userService, CitizenService citizenService, ShelterService shelterService) {
        this.petService = petService;
        this.userService = userService;
        this.citizenService = citizenService;
        this.shelterService = shelterService;
    }

    @GetMapping()
    public String showPets(Principal loggedInUser, Model model) {
        List<Pet> allPets = petService.getPets();
        if(loggedInUser == null) {
            model.addAttribute("pets", allPets);
            return "pet/pets";
        }
        User user = userService.getUserByUsername(loggedInUser.getName());
        Citizen citizen = citizenService.getCitizen(user.getId());
        Shelter shelter = shelterService.getShelter(user.getId());
        if (citizen != null) {
            List<Pet> approvedPets = new ArrayList<>();
            for (Pet pet : allPets) {
                boolean hasRejectedHealthCheck = false;
                if(pet.getApprovalStatus() == Status.APPROVED) {
                    for(HealthCheck health : pet.getHealth()) {
                        if(health.getStatus() == Status.REJECTED) {
                            hasRejectedHealthCheck = true;
                            break;
                        }
                    }
                    if (!hasRejectedHealthCheck) {
                        approvedPets.add(pet);
                    }
                }
            }
            model.addAttribute("pets", approvedPets);
        } else if (shelter != null) {
            model.addAttribute("pets", shelter.getPetsAvailable());
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
        String username = loggedInUser.getName();
        petService.savePet(pet, username);
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
