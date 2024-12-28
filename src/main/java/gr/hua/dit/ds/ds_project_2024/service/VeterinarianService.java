package gr.hua.dit.ds.ds_project_2024.service;

import gr.hua.dit.ds.ds_project_2024.entities.Citizen;
import gr.hua.dit.ds.ds_project_2024.entities.Role;
import gr.hua.dit.ds.ds_project_2024.entities.Veterinarian;
import gr.hua.dit.ds.ds_project_2024.repositories.RoleRepository;
import gr.hua.dit.ds.ds_project_2024.repositories.VeterinarianRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class VeterinarianService {

    private VeterinarianRepository veterinarianRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;

    public VeterinarianService(VeterinarianRepository veterinarianRepository, BCryptPasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.veterinarianRepository = veterinarianRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public List<Veterinarian> getVeterinarians() {
        return veterinarianRepository.findAll();
    }

    @Transactional
    public Veterinarian getVeterinarian(Integer veterinarianId) { return veterinarianRepository.findById(veterinarianId).get(); }

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
    public void deleteVeterinarian(Integer veterinarianId) { veterinarianRepository.deleteById(veterinarianId); }
}
