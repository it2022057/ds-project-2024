package gr.hua.dit.ds.ds_project_2024.service;

import gr.hua.dit.ds.ds_project_2024.entities.Adoption;
import gr.hua.dit.ds.ds_project_2024.entities.Citizen;
import gr.hua.dit.ds.ds_project_2024.repositories.AdoptionRepository;
import gr.hua.dit.ds.ds_project_2024.repositories.CitizenRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdoptionService {

    private CitizenRepository citizenRepository;
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
    public Citizen saveAdoption(Adoption adoption, String username) {
        Optional<Citizen> opt = citizenRepository.findCitizenByUsername(username);
        Citizen citizen = opt.orElseThrow(() -> new UsernameNotFoundException("Citizen with username: " + username +" not found !"));

        adoption.setApplicant(citizen);
        adoption.setFromShelter(adoption.getPetToAdopt().getOnShelter());

        adoptionRepository.save(adoption);

        adoption.getPetToAdopt().getInterest().addLast(adoption);
        citizen.getPendingAdoptions().addLast(adoption);
        adoption.getPetToAdopt().getOnShelter().getAdoptionRequests().addLast(adoption);

        return citizen;
    }

    @Transactional
    public void deleteAdoption(Integer adoptionId) { adoptionRepository.deleteById(adoptionId); }
}
