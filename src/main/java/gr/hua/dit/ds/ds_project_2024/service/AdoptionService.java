package gr.hua.dit.ds.ds_project_2024.service;

import gr.hua.dit.ds.ds_project_2024.entities.Adoption;
import gr.hua.dit.ds.ds_project_2024.entities.Citizen;
import gr.hua.dit.ds.ds_project_2024.entities.Pet;
import gr.hua.dit.ds.ds_project_2024.entities.Status;
import gr.hua.dit.ds.ds_project_2024.repositories.AdoptionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdoptionService {

    private AdoptionRepository adoptionRepository;

    public AdoptionService(AdoptionRepository adoptionRepository) {
        this.adoptionRepository = adoptionRepository;
    }

    @Transactional
    public List<Adoption> getAdoptions() {
        return adoptionRepository.findAll();
    }

    @Transactional
    public Adoption getAdoption(Integer adoptionId) { return adoptionRepository.findById(adoptionId).get(); }

    @Transactional
    public void saveAdoption(Adoption adoption) { adoptionRepository.save(adoption); }

    @Transactional
    public void deleteAdoption(Integer adoptionId) { adoptionRepository.deleteById(adoptionId); }

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
