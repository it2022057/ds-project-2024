package gr.hua.dit.ds.ds_project_2024.service;

import gr.hua.dit.ds.ds_project_2024.entities.Citizen;
import gr.hua.dit.ds.ds_project_2024.entities.Pet;
import gr.hua.dit.ds.ds_project_2024.repositories.PetRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {

    private PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @Transactional
    public List<Pet> getPets() {
        return petRepository.findAll();
    }

    @Transactional
    public Pet getPet(Integer petId) { return petRepository.findById(petId).get(); }

    @Transactional
    public void savePet(Pet pet) { petRepository.save(pet); }

    @Transactional
    public void deletePet(Integer petId) { petRepository.deleteById(petId); }

    @Transactional
    public void requestByCitizen(int petId, Citizen citizen) {
        Pet pet = petRepository.findById(petId).get();
        System.out.println(pet);
        System.out.println(citizen);

    }
}
