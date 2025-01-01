package gr.hua.dit.ds.ds_project_2024.service;

import gr.hua.dit.ds.ds_project_2024.entities.*;
import gr.hua.dit.ds.ds_project_2024.repositories.CitizenRepository;
import gr.hua.dit.ds.ds_project_2024.repositories.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CitizenService {

    private CitizenRepository citizenRepository;

    private BCryptPasswordEncoder passwordEncoder;

    private RoleRepository roleRepository;

    public CitizenService(CitizenRepository citizenRepository, BCryptPasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.citizenRepository = citizenRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public List<Citizen> getCitizens() {
        return citizenRepository.findAll();
    }

    @Transactional
    public Citizen getCitizen(Integer citizenId) { return citizenRepository.findById(citizenId).orElse(null); }

    @Transactional
    public Citizen getCitizenByUsername(String username) {
        if(citizenRepository.findCitizenByUsername(username).isPresent()) {
            return citizenRepository.findCitizenByUsername(username).get();
        }
        return null;
    }

    @Transactional
    public void saveCitizen(Citizen citizen) {
        String passwd = citizen.getPassword();
        String encodedPassword = passwordEncoder.encode(passwd);
        citizen.setPassword(encodedPassword);

        Role role = roleRepository.findByName("ROLE_CITIZEN")
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        citizen.setRoles(roles);

        citizenRepository.save(citizen);
    }

    @Transactional
    public void deleteCitizen(Integer citizenId) { citizenRepository.deleteById(citizenId); }

//    @Transactional
//    public List<Pet> searchPets() {
//        // Later we can add some filters, like search based on species, name, age, etc., but we will see
//        return petRepository.findAll();
//    }
}
