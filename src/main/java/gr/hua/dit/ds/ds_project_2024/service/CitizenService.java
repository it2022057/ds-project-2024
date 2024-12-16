package gr.hua.dit.ds.ds_project_2024.service;

import gr.hua.dit.ds.ds_project_2024.entities.Adoption;
import gr.hua.dit.ds.ds_project_2024.entities.Citizen;
import gr.hua.dit.ds.ds_project_2024.entities.Pet;
import gr.hua.dit.ds.ds_project_2024.entities.Status;
import gr.hua.dit.ds.ds_project_2024.repositories.AdoptionRepository;
import gr.hua.dit.ds.ds_project_2024.repositories.CitizenRepository;
import gr.hua.dit.ds.ds_project_2024.repositories.PetRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CitizenService {

    private CitizenRepository citizenRepository;

    private PetRepository petRepository;

    private AdoptionRepository adoptionRepository;

    public CitizenService(CitizenRepository citizenRepository, PetRepository petRepository, AdoptionRepository adoptionRepository) {
        this.citizenRepository = citizenRepository;
        this.petRepository = petRepository;
        this.adoptionRepository = adoptionRepository;
    }

    @Transactional
    public List<Citizen> getCitizens() {
        return citizenRepository.findAll();
    }

    @Transactional
    public Citizen getCitizen(Integer citizenId) { return citizenRepository.findById(citizenId).get(); }

    @Transactional
    public void saveCitizen(Citizen citizen) { citizenRepository.save(citizen); }

    @Transactional
    public void deleteCitizen(Integer citizenId) { citizenRepository.deleteById(citizenId); }

    @Transactional
    public List<Pet> searchPets() {
        // Later we can add some filters, like search based on species, name, age, etc., but we will see
        return petRepository.findAll();
    }

    @Transactional
    public void submitAdoptionRequest(Integer petId, Citizen citizen) {
        Pet pet = petRepository.findById(petId).get();
        System.out.println(citizen);
        System.out.println(pet);

        Adoption adoptionRequest = new Adoption();
        adoptionRequest.setApplicant(citizen);
        adoptionRequest.setPetToAdopt(pet);
        adoptionRequest.setFromShelter(pet.getOnShelter());
        adoptionRequest.setStatus(Status.PENDING);
        adoptionRepository.save(adoptionRequest);
        System.out.println(adoptionRequest);
    }
}
