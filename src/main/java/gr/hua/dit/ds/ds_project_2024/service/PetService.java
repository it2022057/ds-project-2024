package gr.hua.dit.ds.ds_project_2024.service;

import gr.hua.dit.ds.ds_project_2024.entities.*;
import gr.hua.dit.ds.ds_project_2024.repositories.PetRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PetService {

    private PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @Transactional
    public List<Pet> getPets() { return petRepository.findAll(); }

    @Transactional
    public Pet getPet(Integer petId) { return petRepository.findById(petId).get(); }

    @Transactional
    public List<Pet> getApprovedPets(List<Pet> allPets) {
        List<Pet> approvedPets = new ArrayList<>();
        for (Pet pet : allPets) {
            boolean hasRejectedHealthCheck = false;
            if (pet.getApprovalStatus() == Status.APPROVED) {
                for (HealthCheck health : pet.getHealth()) {
                    if (health.getStatus() != Status.APPROVED) {
                        hasRejectedHealthCheck = true;
                        break;
                    }
                }
                if (!hasRejectedHealthCheck && !pet.getHealth().isEmpty()) {
                    approvedPets.addLast(pet);
                }
            }
        }
        return approvedPets;
    }

    @Transactional
    public void savePet(Pet pet, Shelter shelter) {
        pet.setApprovalStatus(Status.PENDING);
        pet.setOnShelter(shelter);
        shelter.getPetsAvailable().addLast(pet);
        petRepository.save(pet);
    }

    @Transactional
    public void updatePet(Pet pet) {
        petRepository.save(pet);
    }

    @Transactional
    public void deletePet(Pet pet) {
        pet.getOnShelter().getPetsAvailable().remove(pet);
        if (pet.getOwner() != null) {
            pet.getOwner().getAdoptedPets().remove(pet);
        }
        petRepository.delete(pet);
    }
}
