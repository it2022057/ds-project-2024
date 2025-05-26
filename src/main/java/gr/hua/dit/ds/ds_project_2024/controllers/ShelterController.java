package gr.hua.dit.ds.ds_project_2024.controllers;

import gr.hua.dit.ds.ds_project_2024.entities.Shelter;
import gr.hua.dit.ds.ds_project_2024.entities.Status;
import gr.hua.dit.ds.ds_project_2024.service.MailService;
import gr.hua.dit.ds.ds_project_2024.service.ShelterService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("shelter")
public class ShelterController {

    private ShelterService shelterService;
    private MailService mailService;

    public ShelterController(ShelterService shelterService, MailService mailService) {
        this.shelterService = shelterService;
        this.mailService = mailService;
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
        shelter.setApprovalStatus(Status.PENDING);
        shelterService.saveShelter(shelter);
        String message = "Shelter " + shelter.getId() + " waits for approval!";
        model.addAttribute("msg", message);
        return "home";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/delete/{id}")
    public String deleteShelter(@PathVariable Integer id, Model model) {
        mailService.sendMail(shelterService.getShelter(id).getEmail(), "Account delete", "Hi, we just want to inform you that your account got deleted from our webpage");
        shelterService.deleteShelter(id);
        model.addAttribute("shelters", shelterService.getShelters());
        return "shelter/shelters";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/approve/{id}")
    public String acceptShelter(@PathVariable Integer id, Model model) {
        Shelter shelter = shelterService.getShelter(id);
        shelter.setApprovalStatus(Status.APPROVED);
        shelterService.saveShelter(shelter);
        mailService.sendMail(shelter.getEmail(), "Shelter approval", "Your registration has just been accepted and you can now log in with your credentials. Welcome to our pet adoption family!");
        model.addAttribute("shelters", shelterService.getShelters());
        return "shelter/shelters";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/reject/{id}")
    public String rejectShelter(@PathVariable Integer id, Model model) {
        Shelter shelter = shelterService.getShelter(id);
        shelter.setApprovalStatus(Status.REJECTED);
        shelterService.saveShelter(shelter);
        mailService.sendMail(shelter.getEmail(), "Shelter rejection", "Unfortunately your registration has just been rejected. Please do not hesitate to try again soon!");
        model.addAttribute("shelters", shelterService.getShelters());
        return "shelter/shelters";
    }
}
