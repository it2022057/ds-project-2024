package gr.hua.dit.ds.ds_project_2024.controllers;

import gr.hua.dit.ds.ds_project_2024.entities.Adoption;
import gr.hua.dit.ds.ds_project_2024.entities.Citizen;
import gr.hua.dit.ds.ds_project_2024.entities.Pet;
import gr.hua.dit.ds.ds_project_2024.entities.Status;
import gr.hua.dit.ds.ds_project_2024.service.AdoptionService;
import gr.hua.dit.ds.ds_project_2024.service.CitizenService;
import gr.hua.dit.ds.ds_project_2024.service.PetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("adoption")
public class AdoptionController {

    private AdoptionService adoptionService;
    private PetService petService;

    public AdoptionController(AdoptionService adoptionService, PetService petService) {
        this.adoptionService = adoptionService;
        this.petService = petService;
    }

    @RequestMapping()
    public String showAdoptions(Model model) {
        model.addAttribute("adoptions", adoptionService.getAdoptions());
        return "adoption/adoptions";
    }

    @RequestMapping("/{id}")
    public String showAdoption(@PathVariable Integer id, Model model) {
        Adoption adoption = adoptionService.getAdoption(id);
        model.addAttribute("adoptions", adoption);
        return "adoption/adoptions";
    }

    @RequestMapping("/new")
    public String addAdoption(Model model) {
        Adoption adoption = new Adoption();
        List<Pet> pets = petService.getPets();
        model.addAttribute("adoption", adoption);
        model.addAttribute("pets", pets);
        return "adoption/adoption";
    }

    @PostMapping("/new")
    public String saveAdoption(@ModelAttribute("adoption") Adoption adoption, Model model) {
        adoption.setStatus(Status.PENDING);
        adoption.setFromShelter(adoption.getPetToAdopt().getOnShelter());
        adoptionService.saveAdoption(adoption);
        model.addAttribute("adoptions", adoptionService.getAdoptions());
        return "adoption/adoptions";
    }

    @RequestMapping("/delete/{id}")
    public String deleteAdoption(@PathVariable Integer id, Model model) {
        adoptionService.deleteAdoption(id);
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
    public String adoptionRequest(@PathVariable Integer id, Model model) {
        Pet pet = petService.getPet(id);
        adoptionService.submitAdoptionRequest(id);
        model.addAttribute("adoptions", adoptionService.getAdoptions());
        return "adoption/adoptions";
    }

}
