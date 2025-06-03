package gr.hua.dit.ds.ds_project_2024.controllers;

import gr.hua.dit.ds.ds_project_2024.entities.Citizen;
import gr.hua.dit.ds.ds_project_2024.entities.Pet;
import gr.hua.dit.ds.ds_project_2024.service.CitizenService;
import gr.hua.dit.ds.ds_project_2024.service.MailService;
import gr.hua.dit.ds.ds_project_2024.service.PetService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("citizen")
public class CitizenController {

    private CitizenService citizenService;
    private PetService petService;
    private MailService mailService;

    public CitizenController(CitizenService citizenService, PetService petService, MailService mailService) {
        this.citizenService = citizenService;
        this.petService = petService;
        this.mailService = mailService;
    }

    @RequestMapping()
    public String showCitizens(Model model) {
        model.addAttribute("citizens", citizenService.getCitizens());
        return "citizen/citizens";
    }

    @GetMapping("/{id}")
    public String showCitizen(@PathVariable Integer id, Model model) {
        Citizen citizen = citizenService.getCitizen(id);
        model.addAttribute("citizens", citizen);
        return "citizen/citizens";
    }

    @GetMapping("/new")
    public String addCitizen(Model model) {
        Citizen citizen = new Citizen();
        model.addAttribute("citizen", citizen);
        return "citizen/citizen";
    }

    @PostMapping("/new")
    public String saveCitizen(@ModelAttribute("citizen") Citizen citizen, Model model) {
        citizenService.saveCitizen(citizen);
        String message = "Citizen " + citizen.getId() + " saved successfully!";
        mailService.sendMail(citizen.getEmail(), "Citizen registration", "Welcome to our pet adoption family!");
        model.addAttribute("msg", message);
        return "home";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/delete/{id}")
    public String deleteCitizen(@PathVariable Integer id, Model model) {
        mailService.sendMail(citizenService.getCitizen(id).getEmail(), "Account delete", "Hi, we just want to inform you that your account got deleted from our webpage");
        citizenService.deleteCitizen(id);
        model.addAttribute("citizens", citizenService.getCitizens());
        return "citizen/citizens";
    }

    @Secured("ROLE_CITIZEN")
    @GetMapping("/adoptedPets")
    public String showAdoptedPets(Principal loggedInUser, Model model) {
        Citizen citizen = citizenService.getCitizenByUsername(loggedInUser.getName());
        model.addAttribute("pets", citizen.getAdoptedPets());
        return "pet/pets";
    }

    @Secured("ROLE_CITIZEN")
    @GetMapping("/visits")
    public String showVisits(Principal loggedInUser, Model model) {
        Citizen citizen = citizenService.getCitizenByUsername(loggedInUser.getName());

        model.addAttribute("contacts", citizenService.getVisits(citizen));
        return "contact/inbox";
    }

    @Secured("ROLE_CITIZEN")
    @GetMapping("/adoptionRequest/{id}")
    public String showAdoptionRequest(@PathVariable Integer id, Model model) {
        Pet pet = petService.getPet(id);
        model.addAttribute("pet", pet);
        return "citizen/adoptionRequest";
    }

    @Secured("ROLE_CITIZEN")
    @PostMapping("/adoptionRequest/{id}")
    public String adoptionRequest(@PathVariable Integer id, @RequestParam("action") String action, Model model, Principal loggedInUser) {
        if (action.equalsIgnoreCase("Yes")) {
            Pet pet = petService.getPet(id);
            Citizen citizen = citizenService.getCitizenByUsername(loggedInUser.getName());
            citizenService.submitAdoptionRequest(pet, citizen);
            mailService.sendMail(citizen.getEmail(), "Adoption request", "Your adoption request to shelter " + pet.getOnShelter().getName() + " has been sent. Wait for their answer!");
            mailService.sendMail(pet.getOnShelter().getEmail(), "Adoption request", "User " + citizen.getFirstName() + " " + citizen.getLastName() + " has made a new adoption request. Go check it out!");
            model.addAttribute("adoptions", citizen.getPendingAdoptions());
            return "adoption/adoptions";
        }
        model.addAttribute("pets", petService.getApprovedPets(petService.getPets()));
        return "pet/pets";
    }
}
