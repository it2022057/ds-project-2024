package gr.hua.dit.ds.ds_project_2024.controllers;

import gr.hua.dit.ds.ds_project_2024.entities.*;
import gr.hua.dit.ds.ds_project_2024.repositories.CitizenRepository;
import gr.hua.dit.ds.ds_project_2024.service.*;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("adoption")
public class AdoptionController {

    private CitizenService citizenService;
    private ShelterService shelterService;
    private AdoptionService adoptionService;
    private PetService petService;

    public AdoptionController(AdoptionService adoptionService, PetService petService, CitizenService citizenService, ShelterService shelterService) {
        this.adoptionService = adoptionService;
        this.petService = petService;
        this.citizenService = citizenService;
        this.shelterService = shelterService;
    }

    @RequestMapping()
    public String showAdoptions(Principal principal, Model model, UserDetails authenticatedPrincipal) {
        Citizen citizen = citizenService.getCitizenByUsername(authenticatedPrincipal.getUsername());
        Shelter shelter = shelterService.getShelterByUsername(principal.getName());
        if (citizen != null) {
            model.addAttribute("citizen", citizen);
            throw new UsernameNotFoundException("You are not authorized to view this resource");
        } else if (shelter != null) {
            model.addAttribute("shelter", shelter);
            throw new UsernameNotFoundException("You are not authorized to view this resource");
        }

        model.addAttribute("adoptions", adoptionService.getAdoptions());
        return "adoption/adoptions";
    }

    @RequestMapping("/{id}")
    public String showAdoption(@PathVariable Integer id, Model model) {
        Adoption adoption = adoptionService.getAdoption(id);
        model.addAttribute("adoptions", adoption);
        return "adoption/adoptions";
    }

    @GetMapping("/new")
    public String addAdoption(Model model) {
        Adoption adoption = new Adoption();
        List<Pet> pets = petService.getPets();
        model.addAttribute("adoption", adoption);
        model.addAttribute("pets", pets);
        return "adoption/adoption";
    }

    @PostMapping("/new")
    public String saveAdoption(@ModelAttribute("adoption") Adoption adoption, Principal principal, Model model) {
        String username = principal.getName();
        adoptionService.saveAdoption(adoption, username);
        model.addAttribute("adoptions", adoptionService.getAdoptions());
        return "adoption/adoptions";
    }

    @RequestMapping("/delete/{id}")
    public String deleteAdoption(@PathVariable Integer id, Model model) {
        adoptionService.deleteAdoption(id);
        model.addAttribute("adoptions", adoptionService.getAdoptions());
        return "adoption/adoptions";
    }

    @Secured("ROLE_SHELTER")
    @GetMapping("/approve/{id}")
    public String acceptAdoption(@PathVariable Integer id, Model model) {
        Adoption adoption = adoptionService.getAdoption(id);
        adoption.setStatus(Status.APPROVED);
        model.addAttribute("adoptions", adoptionService.getAdoptions());
        return "adoption/adoptions";
    }

    @Secured("ROLE_SHELTER")
    @GetMapping("/reject/{id}")
    public String rejectAdoption(@PathVariable Integer id, Model model) {
        Adoption adoption = adoptionService.getAdoption(id);
        adoption.setStatus(Status.REJECTED);
        model.addAttribute("adoptions", adoptionService.getAdoptions());
        return "adoption/adoptions";
    }

    @GetMapping("/request/{id}")
    public String showAdoptionRequest(@PathVariable Integer id, Model model) {
        Pet pet = petService.getPet(id);
        model.addAttribute("pet", pet);
        return "adoption/request";
    }

    @PostMapping("/request/{id}")
    public String adoptionRequestByButton(@PathVariable Integer id, Model model, Principal principal) {
        Pet pet = petService.getPet(id);
        String username = principal.getName();
        adoptionService.submitAdoptionRequest(pet, username);
        model.addAttribute("adoptions", adoptionService.getAdoptions());
        return "adoption/adoptions";
    }

}
