package gr.hua.dit.ds.ds_project_2024.controllers;

import gr.hua.dit.ds.ds_project_2024.entities.Pet;
import gr.hua.dit.ds.ds_project_2024.entities.Role;
import gr.hua.dit.ds.ds_project_2024.entities.User;
import gr.hua.dit.ds.ds_project_2024.repositories.RoleRepository;
import gr.hua.dit.ds.ds_project_2024.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    private UserService userService;
    private RoleRepository roleRepository;

    public UserController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/users")
    public String showUsers(Model model) {
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("roles", roleRepository.findAll());
        return "auth/users";
    }

    @GetMapping("/user/{user_id}")
    public String showUser(@PathVariable Integer user_id, Model model) {
        model.addAttribute("user", userService.getUser(user_id));
        return "auth/user";
    }

    @GetMapping("/register")
    public String register(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "auth/register";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute User user, Model model) {
        System.out.println("Roles: " + user.getRoles());
        Integer id = userService.saveUser(user);
        String message = "User " + id + " saved successfully!";
        model.addAttribute("msg", message);
        return "index";
    }

//    @GetMapping("/user/new")
//    public String addPet(Model model) {
//        Pet pet = new Pet();
//        model.addAttribute("pet", pet);
//        return "pet/pet";
//    }

//    @GetMapping("/delete/{id}")
//    public String deletePet(@PathVariable Integer id, Model model) {
//        petService.deletePet(id);
//        model.addAttribute("pets", petService.getPets());
//        return "pet/pets";
//    }


    @GetMapping("/user/role/delete/{user_id}/{role_id}")
    public String deleteRoleFromUser(@PathVariable Integer user_id, @PathVariable Integer role_id, Model model){
        User user = userService.getUser(user_id);
        Role role = roleRepository.findById(role_id).get();
        user.getRoles().remove(role);
        System.out.println("Roles: " + user.getRoles());
        userService.updateUser(user);
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("roles", roleRepository.findAll());
        return "auth/users";
    }

    @GetMapping("/user/role/add/{user_id}/{role_id}")
    public String addRoleToUser(@PathVariable Integer user_id, @PathVariable Integer role_id, Model model){
        User user = (User) userService.getUser(user_id);
        Role role = roleRepository.findById(role_id).get();
        user.getRoles().add(role);
        System.out.println("Roles: " + user.getRoles());
        userService.updateUser(user);
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("roles", roleRepository.findAll());
        return "auth/users";
    }
}
