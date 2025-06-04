package gr.hua.dit.ds.ds_project_2024.controllers;

import gr.hua.dit.ds.ds_project_2024.entities.*;
import gr.hua.dit.ds.ds_project_2024.service.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("pet")
public class PetController {

    private VeterinarianService veterinarianService;
    private ShelterService shelterService;
    private CitizenService citizenService;
    private UserService userService;
    private PetService petService;
    private MailService mailService;
    private MinioService minioService;

    @Value("${minio.bucket.path}")
    private String bucketPath;

    public PetController(PetService petService, UserService userService, CitizenService citizenService, ShelterService shelterService, VeterinarianService veterinarianService, MailService  mailService, MinioService minioService) {
        this.petService = petService;
        this.userService = userService;
        this.citizenService = citizenService;
        this.shelterService = shelterService;
        this.veterinarianService = veterinarianService;
        this.mailService =  mailService;
        this.minioService = minioService;
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

    @Secured("ROLE_SHELTER")
    @GetMapping("/new")
    public String addPet(Model model) {
        Pet pet = new Pet();
        model.addAttribute("pet", pet);
        return "pet/pet";
    }

    @Secured("ROLE_SHELTER")
    @PostMapping("/new")
    public String savePet(@ModelAttribute("pet") Pet pet, Principal loggedInUser, Model model) {
        Shelter shelter = shelterService.getShelterByUsername(loggedInUser.getName());
        petService.savePet(pet, shelter);
        model.addAttribute("pet", pet);
        return "pet/petPhoto";
    }

    @Secured("ROLE_SHELTER")
    @PostMapping("/uploadImage")
    public String uploadPetPhoto(@RequestParam("file") MultipartFile file, @RequestParam("petName") String petName, Principal loggedInUser, Model model) {
        Shelter shelter = shelterService.getShelterByUsername(loggedInUser.getName());
        String originalFilename = file.getOriginalFilename();
        String extension = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        minioService.uploadFile("pet-photos", petName + extension, file);

        Pet petNow = null;
        for (Pet pet : petService.getPets()) {
            if (pet.getName().equalsIgnoreCase(petName)) {
                petNow = pet;
                break;
            }
        }
        if (petNow != null) {
            petNow.setImagePath(bucketPath + "/" + "pet-photos/" + petName + extension);
            petService.updatePet(petNow);
        }
        model.addAttribute("pets", shelter.getPetsAvailable());
        return "pet/pets";
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping("/delete/{id}")
    public String deletePet(@PathVariable Integer id, Model model) {
        mailService.sendMail(petService.getPet(id).getOnShelter().getEmail(), "Pet deleted", "Hi, we just want to inform you that a pet with name " + petService.getPet(id).getName() + " got deleted");
        minioService.deleteFile("pet-photos", petService.getPet(id).getName());
        petService.deletePet(petService.getPet(id));
        model.addAttribute("pets", petService.getPets());
        return "pet/pets";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/approve/{id}")
    public String acceptPet(@PathVariable Integer id, Model model) {
        Pet pet = petService.getPet(id);
        pet.setApprovalStatus(Status.APPROVED);
        pet.getOnShelter().getPetsAvailable().add(pet);
        mailService.sendMail(pet.getOnShelter().getEmail(), "Pet " + id, "Your pet with name " + pet.getName() + " has been approved");
        model.addAttribute("pets", petService.getPets());
        return "pet/pets";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/reject/{id}")
    public String rejectPet(@PathVariable Integer id, Model model) {
        Pet pet = petService.getPet(id);
        pet.setApprovalStatus(Status.REJECTED);
        mailService.sendMail(pet.getOnShelter().getEmail(), "Pet " + id, "Your pet with name " + pet.getName() + " has been rejected");
        model.addAttribute("pets", petService.getPets());
        return "pet/pets";
    }
}
