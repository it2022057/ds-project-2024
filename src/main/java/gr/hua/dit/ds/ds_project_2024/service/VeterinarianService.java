package gr.hua.dit.ds.ds_project_2024.service;

import gr.hua.dit.ds.ds_project_2024.entities.*;
import gr.hua.dit.ds.ds_project_2024.repositories.HealthCheckRepository;
import gr.hua.dit.ds.ds_project_2024.repositories.RoleRepository;
import gr.hua.dit.ds.ds_project_2024.repositories.VeterinarianRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class VeterinarianService {

    private VeterinarianRepository veterinarianRepository;
    private HealthCheckRepository healthCheckRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;

    public VeterinarianService(VeterinarianRepository veterinarianRepository, HealthCheckRepository healthCheckRepository, BCryptPasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.veterinarianRepository = veterinarianRepository;
        this.healthCheckRepository = healthCheckRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public List<Veterinarian> getVeterinarians() {
        return veterinarianRepository.findAll();
    }

    @Transactional
    public Veterinarian getVeterinarian(Integer veterinarianId) { return veterinarianRepository.findById(veterinarianId).orElse(null); }

    @Transactional
    public Veterinarian getVeterinarianByUsername(String username) {
        Optional<Veterinarian> opt = veterinarianRepository.findVeterinarianByUsername(username);
        return opt.orElseThrow(() -> new UsernameNotFoundException("Veterinarian with username: " + username +" not found !"));
    }

    @Transactional
    public void saveVeterinarian(Veterinarian veterinarian) {
        String passwd= veterinarian.getPassword();
        String encodedPassword = passwordEncoder.encode(passwd);
        veterinarian.setPassword(encodedPassword);

        Role role = roleRepository.findByName("ROLE_VETERINARIAN")
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        veterinarian.setRoles(roles);

        veterinarianRepository.save(veterinarian);
    }

    @Transactional
    public void deleteVeterinarian(Integer veterinarianId) {
        veterinarianRepository.deleteById(veterinarianId);
    }

    @Transactional
    public void canExamine(List<Pet> allPets, Veterinarian veterinarian) {
        boolean canExamine;
        for (Pet pet : allPets) {
            canExamine = true;
            for (HealthCheck health : pet.getHealth()) {
                if (health.getByVeterinarian() == veterinarian) {
                    canExamine = false;
                    break;
                }
            }
            pet.setLoggedInVetHasNotExamined(canExamine);
        }
    }

    @Transactional
    public void examination(Pet pet, Status status, String details, Veterinarian veterinarian) {
        HealthCheck healthCheck = new HealthCheck();

        healthCheck.setStatus(status);
        healthCheck.setDetails(details);
        healthCheck.setPetExamined(pet);
        healthCheck.setByVeterinarian(veterinarian);

        healthCheckRepository.save(healthCheck);

        pet.getHealth().addLast(healthCheck);
        veterinarian.getHealthTests().addLast(healthCheck);
    }
}
