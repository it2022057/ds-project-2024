package gr.hua.dit.ds.ds_project_2024.controllers;

import gr.hua.dit.ds.ds_project_2024.entities.Citizen;
import gr.hua.dit.ds.ds_project_2024.service.CitizenService;
import gr.hua.dit.ds.ds_project_2024.service.PetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("citizen")
public class CitizenController {

    private PetService petService;
    private CitizenService citizenService;

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
        model.addAttribute("citizens", citizenService.getCitizens());
        return "citizen/citizens";
    }

    @GetMapping("/delete/{id}")
    public String deleteCitizen(@PathVariable Integer id, Model model) {
        citizenService.deleteCitizen(id);
        model.addAttribute("citizens", citizenService.getCitizens());
        return "citizen/citizens";
    }

    @GetMapping("/search")
    public String searchPets(Model model) {
        model.addAttribute("pets", citizenService.searchPets());
        return "pet/search";
    }
//
//    @GetMapping("/adopt/{id}")
//    public String showAdoptionRequest(@PathVariable Integer id, Model model) {
//        Pet pet = petService.getPet(id);
//        model.addAttribute("pet", pet);
//        model.addAttribute("citizen", citizen);
//        return "citizen/adoption";
//    }
//
//    @PostMapping("/adopt/{id}")
//    public String adoptionRequest(@PathVariable Integer id, @RequestParam(value = "citizen", required = true) Citizen citizen, Model model) {
//        citizenService.submitAdoptionRequest(id, citizen);
//        model.addAttribute("pets", citizenService.getCitizens());
//        return "citizen/citizens";
//    }

}
