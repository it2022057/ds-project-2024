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
@RequestMapping("adoption")
public class AdoptionController {

    private UserService userService;
    private CitizenService citizenService;
    private ShelterService shelterService;
    private AdoptionService adoptionService;
    private PetService petService;
    private MailService mailService;

    public AdoptionController(AdoptionService adoptionService, PetService petService, CitizenService citizenService, ShelterService shelterService, UserService userService, MailService mailService) {
        this.adoptionService = adoptionService;
        this.petService = petService;
        this.citizenService = citizenService;
        this.shelterService = shelterService;
        this.userService = userService;
        this.mailService = mailService;
    }

    @GetMapping()
    public String showAdoptions(Principal loggedInUser, Model model) {
        User user = userService.getUserByUsername(loggedInUser.getName());
        Citizen citizen = citizenService.getCitizen(user.getId());
        Shelter shelter = shelterService.getShelter(user.getId());
        if (citizen != null) {
            model.addAttribute("adoptions", citizen.getPendingAdoptions());
        } else if (shelter != null) {
            model.addAttribute("adoptions", shelter.getAdoptionRequests());
        } else {
            model.addAttribute("adoptions", adoptionService.getAdoptions());
        }
        return "adoption/adoptions";
    }

    @RequestMapping("/{id}")
    public String showAdoption(@PathVariable Integer id, Model model) {
        Adoption adoption = adoptionService.getAdoption(id);
        model.addAttribute("adoptions", adoption);
        return "adoption/adoptions";
    }

    @Secured("ROLE_CITIZEN")
    @GetMapping("/new")
    public String addAdoption(Model model) {
        Adoption adoption = new Adoption();
        List<Pet> pets = petService.getPets();
        model.addAttribute("adoption", adoption);
        model.addAttribute("pets", petService.getApprovedPets(pets));
        return "adoption/adoption";
    }

    @Secured("ROLE_CITIZEN")
    @PostMapping("/new")
    public String saveAdoption(@ModelAttribute("adoption") Adoption adoption, Principal principal, Model model) {
        String username = principal.getName();
        Citizen citizen = adoptionService.saveAdoption(adoption, username);
        mailService.sendMail(citizen.getEmail(), "Adoption request", "Your adoption request to shelter " + adoption.getFromShelter().getName() + " has been sent. Wait for their answer!");
        mailService.sendMail(adoption.getFromShelter().getEmail(), "Adoption request", "User " + citizen.getFirstName() + " " + citizen.getLastName() + " has made a new adoption request. Go check it out!");
        model.addAttribute("adoptions", citizen.getPendingAdoptions());
        return "adoption/adoptions";
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping("/delete/{id}")
    public String deleteAdoption(@PathVariable Integer id, Model model) {
        mailService.sendMail(adoptionService.getAdoption(id).getApplicant().getEmail(), "Adoption request", "Your adoption request for pet " + adoptionService.getAdoption(id).getPetToAdopt().getName() + " has been deleted");
        mailService.sendMail(adoptionService.getAdoption(id).getFromShelter().getEmail(), "Adoption request", "Adoption request for pet " + adoptionService.getAdoption(id).getPetToAdopt().getName() + " has been deleted");
        adoptionService.deleteAdoption(id);
        model.addAttribute("adoptions", adoptionService.getAdoptions());
        return "adoption/adoptions";
    }

    @Secured("ROLE_SHELTER")
    @GetMapping("/accept/{id}")
    public String acceptAdoption(@PathVariable Integer id, Model model) {
        Adoption adoption = adoptionService.getAdoption(id);
        adoption.setStatus(Status.APPROVED);
        adoption.getPetToAdopt().setApprovalStatus(Status.ADOPTED);
        Citizen citizen = citizenService.getCitizen(adoption.getApplicant().getId());
        adoption.getPetToAdopt().setOwner(citizen);
        citizen.getAdoptedPets().addLast(adoption.getPetToAdopt());
        petService.updatePet(adoption.getPetToAdopt());
        mailService.sendMail(adoptionService.getAdoption(id).getApplicant().getEmail(), "Adoption request", "Your adoption request for pet " + adoptionService.getAdoption(id).getPetToAdopt().getName() + " has been accepted. We hope you love your new pet!");
        model.addAttribute("adoptions", adoption.getFromShelter().getAdoptionRequests());
        return "adoption/adoptions";
    }

    @Secured("ROLE_SHELTER")
    @GetMapping("/reject/{id}")
    public String rejectAdoption(@PathVariable Integer id, Model model) {
        Adoption adoption = adoptionService.getAdoption(id);
        adoption.setStatus(Status.REJECTED);
        mailService.sendMail(adoptionService.getAdoption(id).getApplicant().getEmail(), "Adoption request", "Adoption request for pet " + adoptionService.getAdoption(id).getPetToAdopt().getName() + " has been rejected, though do not get discouraged. Every person has a matching pet :)");
        model.addAttribute("adoptions", adoption.getFromShelter().getAdoptionRequests());
        return "adoption/adoptions";
    }
}
