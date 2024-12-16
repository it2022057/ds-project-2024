package gr.hua.dit.ds.ds_project_2024.service;

import gr.hua.dit.ds.ds_project_2024.entities.Pet;
import gr.hua.dit.ds.ds_project_2024.entities.Shelter;
import gr.hua.dit.ds.ds_project_2024.repositories.ShelterRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShelterService {

    private ShelterRepository shelterRepository;

    public ShelterService(ShelterRepository shelterRepository) {
        this.shelterRepository = shelterRepository;
    }

    @Transactional
    public List<Shelter> getShelters() {
        return shelterRepository.findAll();
    }

    @Transactional
    public Shelter getShelter(Integer shelterId) { return shelterRepository.findById(shelterId).get(); }

    @Transactional
    public void savePet(Shelter shelter) { shelterRepository.save(shelter); }
}
