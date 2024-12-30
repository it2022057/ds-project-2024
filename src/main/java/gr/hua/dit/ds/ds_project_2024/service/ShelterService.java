package gr.hua.dit.ds.ds_project_2024.service;

import gr.hua.dit.ds.ds_project_2024.entities.Pet;
import gr.hua.dit.ds.ds_project_2024.entities.Role;
import gr.hua.dit.ds.ds_project_2024.entities.Shelter;
import gr.hua.dit.ds.ds_project_2024.entities.Status;
import gr.hua.dit.ds.ds_project_2024.repositories.RoleRepository;
import gr.hua.dit.ds.ds_project_2024.repositories.ShelterRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ShelterService {

    private ShelterRepository shelterRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;

    public ShelterService(ShelterRepository shelterRepository, BCryptPasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.shelterRepository = shelterRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public List<Shelter> getShelters() {
        return shelterRepository.findAll();
    }

    @Transactional
    public Shelter getShelter(Integer shelterId) { return shelterRepository.findById(shelterId).get(); }

    @Transactional
    public void saveShelter(Shelter shelter) {
        shelter.setApprovalStatus(Status.PENDING);
        String passwd = shelter.getPassword();
        String encodedPassword = passwordEncoder.encode(passwd);
        shelter.setPassword(encodedPassword);

        // I have to show all the available roles
        Role role = roleRepository.findByName("ROLE_SHELTER")
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        shelter.setRoles(roles);

        shelterRepository.save(shelter);
    }

    @Transactional
    public void deleteShelter(Integer shelterId) { shelterRepository.deleteById(shelterId); }
}
