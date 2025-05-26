package gr.hua.dit.ds.ds_project_2024.controllers;

import gr.hua.dit.ds.ds_project_2024.entities.Role;
import gr.hua.dit.ds.ds_project_2024.entities.User;
import gr.hua.dit.ds.ds_project_2024.repositories.RoleRepository;
import gr.hua.dit.ds.ds_project_2024.service.MailService;
import gr.hua.dit.ds.ds_project_2024.service.UserService;
import jakarta.validation.constraints.Email;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    private UserService userService;
    private RoleRepository roleRepository;
    private MailService mailService;

    public UserController(UserService userService, RoleRepository roleRepository, MailService mailService) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.mailService = mailService;
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
        model.addAttribute("roles", roleRepository.findAll());
        return "auth/user";
    }

    @PostMapping("/user/{user_id}")
    public String editUser(@PathVariable Integer user_id, @ModelAttribute("user") User user, Model model) {
        User edit_user = userService.getUser(user_id);
        edit_user.setUsername(user.getUsername());
        edit_user.setEmail(user.getEmail());
        edit_user.setPhone(user.getPhone());
        edit_user.setRoles(user.getRoles());
        userService.updateUser(edit_user);
        mailService.sendMail(edit_user.getEmail(), "Personal information change", "Hi, you or the admin just changed some of your personal information. Keep your eyes open!");

        model.addAttribute("users", userService.getUsers());
        model.addAttribute("roles", roleRepository.findAll());
        return "auth/users";
    }

    @GetMapping("/register")
    public String register(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("foundAdmin", userService.adminExists());
        return "auth/register";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute User user, Model model) {
        Integer id = userService.saveUser(user);
        String message = "User " + id + " saved successfully!";
        mailService.sendMail(user.getEmail(), "Admin registration", "Successfully registered as our new admin, so let's get started!");
        model.addAttribute("msg", message);
        return "home";
    }

    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable Integer id, Model model) {
        mailService.sendMail(userService.getUser(id).getEmail(), "Account Delete", "Hi, we just want to inform you that your account got deleted from our webpage");
        userService.deleteUser(id);
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("roles", roleRepository.findAll());
        return "auth/users";
    }

    @GetMapping("/user/role/delete/{user_id}/{role_id}")
    public String deleteRoleFromUser(@PathVariable Integer user_id, @PathVariable Integer role_id, Model model){
        User user = userService.getUser(user_id);
        Role role = roleRepository.findById(role_id).get();
        user.getRoles().remove(role);
        userService.updateUser(user);
        mailService.sendMail(user.getEmail(), "Role change", "Hi, we just want to inform you that your account no longer has the role: " + role.getName());
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("roles", roleRepository.findAll());
        return "auth/users";
    }

    @GetMapping("/user/role/add/{user_id}/{role_id}")
    public String addRoleToUser(@PathVariable Integer user_id, @PathVariable Integer role_id, Model model){
        User user = userService.getUser(user_id);
        Role role = roleRepository.findById(role_id).get();
        user.getRoles().add(role);
        userService.updateUser(user);
        mailService.sendMail(user.getEmail(), "Role change", "Hi, we just want to inform you that your account has a new role: " + role.getName());
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("roles", roleRepository.findAll());
        return "auth/users";
    }
}
