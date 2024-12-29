package gr.hua.dit.ds.ds_project_2024.service;

import gr.hua.dit.ds.ds_project_2024.entities.Adoption;
import gr.hua.dit.ds.ds_project_2024.entities.Citizen;
import gr.hua.dit.ds.ds_project_2024.entities.Pet;
import gr.hua.dit.ds.ds_project_2024.entities.Status;
import gr.hua.dit.ds.ds_project_2024.repositories.AdoptionRepository;
import gr.hua.dit.ds.ds_project_2024.repositories.CitizenRepository;
import gr.hua.dit.ds.ds_project_2024.repositories.PetRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdoptionService {

    private final CitizenRepository citizenRepository;
    private AdoptionRepository adoptionRepository;

    public AdoptionService(AdoptionRepository adoptionRepository, CitizenRepository citizenRepository) {
        this.adoptionRepository = adoptionRepository;
        this.citizenRepository = citizenRepository;
    }

    @Transactional
    public List<Adoption> getAdoptions() { return adoptionRepository.findAll(); }

    @Transactional
    public Adoption getAdoption(Integer adoptionId) { return adoptionRepository.findById(adoptionId).get(); }

    @Transactional
    public void saveAdoption(Adoption adoption) { adoptionRepository.save(adoption); }

    @Transactional
    public void deleteAdoption(Integer adoptionId) { adoptionRepository.deleteById(adoptionId); }

    @Transactional
    public void submitAdoptionRequest(Pet pet, String username) {
        Optional<Citizen> opt = citizenRepository.findCitizenByUsername(username);
        Citizen citizen = opt.orElseThrow(() -> new UsernameNotFoundException("User with username: " + username +" not found !"));
        System.out.println(citizen);
        System.out.println(pet);
        Adoption adoptionRequest = new Adoption();
        adoptionRequest.setApplicant(citizen);
        adoptionRequest.setPetToAdopt(pet);
        adoptionRequest.setFromShelter(pet.getOnShelter());
        adoptionRequest.setStatus(Status.PENDING);
        adoptionRepository.save(adoptionRequest);
        pet.getInterest().add(adoptionRequest);
        System.out.println(adoptionRequest);
    }
}
