package ru.zaurbeck.springboot_crud_application.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.zaurbeck.springboot_crud_application.web.model.User;
import ru.zaurbeck.springboot_crud_application.web.service.RoleService;
import ru.zaurbeck.springboot_crud_application.web.service.UserService;

import java.security.Principal;

@Controller
public class UserController {

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public String showAllUsers(Model model, Principal principal) {
        long principalId = userService.getUserByName(principal.getName()).getId();
        model.addAttribute("newUser", new User());
        model.addAttribute("listRoles", roleService.listRoles());
        model.addAttribute("user", userService.getUserById(principalId));
        model.addAttribute("users", userService.getAllUsers());
        return "admin/index";
    }

    @GetMapping(value = "/user/userpage/{id}")
    public String showUser(@PathVariable("id") long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "/user/userpage";
    }

    @PostMapping("/admin/new")
    public String addUser(@ModelAttribute("user") User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.add(user);
        return "redirect:/";
    }

    @PostMapping("/admin/edit/{id}")
    public String updateUser(Model model, @ModelAttribute("user") User user) {
        if (!user.getPassword().equals(userService.getUserById(user.getId()).getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userService.update(user);
        model.addAttribute("users", userService.getAllUsers());
        return "redirect:/";
    }

    @RequestMapping("/delete/{id}")
    public String deleteUser(@PathVariable(name = "id") Long id) {
        userService.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/403")
    public String errorPage() {
        return "403";
    }
}