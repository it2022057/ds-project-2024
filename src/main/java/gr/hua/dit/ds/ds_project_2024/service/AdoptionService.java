package gr.hua.dit.ds.ds_project_2024.service;

import gr.hua.dit.ds.ds_project_2024.entities.Adoption;
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
    public Adoption getCitizen(Integer adoptionId) { return adoptionRepository.findById(adoptionId).get(); }

    @Transactional
    public void saveCitizen(Adoption adoption) { adoptionRepository.save(adoption); }
}
