package gr.hua.dit.ds.ds_project_2024.controllers;

import gr.hua.dit.ds.ds_project_2024.entities.Role;
import gr.hua.dit.ds.ds_project_2024.entities.User;
import gr.hua.dit.ds.ds_project_2024.repositories.RoleRepository;
import gr.hua.dit.ds.ds_project_2024.service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    private RoleRepository roleRepository;

    public AuthController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void setup() {
        Role role_citizen = new Role("ROLE_CITIZEN");
        Role role_shelter = new Role("ROLE_SHELTER");
        Role role_veterinarian = new Role("ROLE_VETERINARIAN");
        Role role_admin = new Role("ROLE_ADMIN");

        roleRepository.updateOrInsert(role_citizen);
        roleRepository.updateOrInsert(role_shelter);
        roleRepository.updateOrInsert(role_veterinarian);
        roleRepository.updateOrInsert(role_admin);
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }
}
