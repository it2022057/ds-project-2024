package gr.hua.dit.ds.ds_project_2024.service;

import gr.hua.dit.ds.ds_project_2024.entities.Pet;
import gr.hua.dit.ds.ds_project_2024.entities.Shelter;
import gr.hua.dit.ds.ds_project_2024.entities.Status;
import gr.hua.dit.ds.ds_project_2024.repositories.PetRepository;
import gr.hua.dit.ds.ds_project_2024.repositories.ShelterRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PetService {

    private PetRepository petRepository;
    private ShelterRepository shelterRepository;

    public PetService(PetRepository petRepository, ShelterRepository shelterRepository) {
        this.petRepository = petRepository;
        this.shelterRepository = shelterRepository;
    }

    @Transactional
    public List<Pet> getPets() {
        return petRepository.findAll();
    }

    @Transactional
    public Pet getPet(Integer petId) { return petRepository.findById(petId).get(); }

    @Transactional
    public void savePet(Pet pet, String username) {
        pet.setApprovalStatus(Status.PENDING);
        Optional<Shelter> opt = shelterRepository.findShelterByUsername(username);
        Shelter shelter =  opt.orElseThrow(() -> new UsernameNotFoundException("Shelter with username: " + username +" not found !"));
        pet.setOnShelter(shelter);
        petRepository.save(pet);
    }

    @Transactional
    public void deletePet(Pet pet) {
        pet.getOnShelter().getPetsAvailable().remove(pet);
        petRepository.delete(pet);
    }
}
