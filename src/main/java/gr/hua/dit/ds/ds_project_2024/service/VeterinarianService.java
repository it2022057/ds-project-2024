package gr.hua.dit.ds.ds_project_2024.service;

import gr.hua.dit.ds.ds_project_2024.entities.Citizen;
import gr.hua.dit.ds.ds_project_2024.entities.Veterinarian;
import gr.hua.dit.ds.ds_project_2024.repositories.VeterinarianRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VeterinarianService {

    private VeterinarianRepository veterinarianRepository;

    public VeterinarianService(VeterinarianRepository veterinarianRepository) {
        this.veterinarianRepository = veterinarianRepository;
    }

    @Transactional
    public List<Veterinarian> getVeterinarians() {
        return veterinarianRepository.findAll();
    }

    @Transactional
    public Veterinarian getVeterinarian(Integer veterinarianId) { return veterinarianRepository.findById(veterinarianId).get(); }

    @Transactional
    public void saveVeterinarian(Veterinarian veterinarian) { veterinarianRepository.save(veterinarian); }
}
