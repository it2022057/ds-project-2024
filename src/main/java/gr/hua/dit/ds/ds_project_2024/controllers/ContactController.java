package gr.hua.dit.ds.ds_project_2024.controllers;

import gr.hua.dit.ds.ds_project_2024.entities.*;
import gr.hua.dit.ds.ds_project_2024.service.CitizenService;
import gr.hua.dit.ds.ds_project_2024.service.ContactService;
import gr.hua.dit.ds.ds_project_2024.service.MailService;
import gr.hua.dit.ds.ds_project_2024.service.ShelterService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("contact")
public class ContactController {

    private  CitizenService citizenService;
    private ContactService contactService;
    private ShelterService shelterService;
    private MailService mailService;

    public ContactController(ContactService contactService, ShelterService shelterService, CitizenService citizenService, MailService mailService) {
        this.contactService = contactService;
        this.shelterService = shelterService;
        this.citizenService = citizenService;
        this.mailService = mailService;
    }

    @Secured("ROLE_SHELTER")
    @GetMapping("/inbox")
    public String showInbox(Principal loggedInUser, Model model) {
        Shelter shelter = shelterService.getShelterByUsername(loggedInUser.getName());
        model.addAttribute("contacts", shelter.getContacts());
        return "contact/inbox";
    }

    @Secured("ROLE_SHELTER")
    @GetMapping("/confirm/{id}")
    public String confirmVisit(@PathVariable int id, Principal loggedInUser, Model model) {
        Shelter shelter = shelterService.getShelterByUsername(loggedInUser.getName());
        Contact contact = contactService.getContact(id);
        contact.setStatus(Status.APPROVED);
        contactService.updateContact(contact);
        mailService.sendMail(contact.getCitizen().getEmail(), "Visit", "Your visit scheduled for " + contact.getDateFormatted() + " was just accepted from the shelter. Can't wait to meet you in person!");

        model.addAttribute("contacts", shelter.getContacts());
        return "contact/inbox";
    }

    @Secured("ROLE_SHELTER")
    @GetMapping("/deny/{id}")
    public String denyVisit(@PathVariable int id, Principal loggedInUser, Model model) {
        Shelter shelter = shelterService.getShelterByUsername(loggedInUser.getName());
        mailService.sendMail(contactService.getContact(id).getCitizen().getEmail(), "Visit", "Your visit scheduled for " + contactService.getContact(id).getDateFormatted() + " was just rejected from the shelter. Do not hesitate to contact again!");
        contactService.deleteContact(id);

        model.addAttribute("contacts", shelter.getContacts());
        return "contact/inbox";
    }

    @Secured("ROLE_CITIZEN")
    @RequestMapping("")
    public String showContactForms(Model model) {
        model.addAttribute("shelters", shelterService.getShelters());
        return "contact/contacts";
    }

    @Secured("ROLE_CITIZEN")
    @GetMapping("/{id}")
    public String showPlanVisit(@PathVariable int id, Model model) {
        Contact contact = new Contact();
        Shelter shelter = shelterService.getShelter(id);
        contact.setShelter(shelter);

        model.addAttribute("shelter", shelter);
        model.addAttribute("contact", contact);
        return "contact/contact";
    }

    @Secured("ROLE_CITIZEN")
    @PostMapping("/planVisit")
    public String planVisit(@ModelAttribute("contact") Contact contact, Principal loggedInUser, Model model) {
        Citizen citizen = citizenService.getCitizenByUsername(loggedInUser.getName());
        Shelter shelter = contact.getShelter();
        contactService.saveContact(contact, citizen, shelter);
        mailService.sendMail(shelter.getEmail(), "Visit request", "Someone is interested to pay a visit in your shelter. Don't keep him waiting!");

        model.addAttribute("msg", "Your message has been sent successfully!\n\tWait for response...");
        return "home";
    }
}
