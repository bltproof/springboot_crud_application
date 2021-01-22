package ru.zaurbeck.springboot_crud_application.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.zaurbeck.springboot_crud_application.web.model.Role;
import ru.zaurbeck.springboot_crud_application.web.model.User;
import ru.zaurbeck.springboot_crud_application.web.service.RoleService;
import ru.zaurbeck.springboot_crud_application.web.service.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String showAllUsers(Model model, Principal principal) {
        long principalId = userService.getUserByName(principal.getName()).getId();
        User newUser = new User();
        List<Role> roles = roleService.listRoles();
        model.addAttribute("newUser", newUser);
        model.addAttribute("listRoles", roles);
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

    @RequestMapping("/delete/{id}")
    public String deleteUser(@PathVariable(name = "id") Long id) {
        userService.deleteById(id);
        return "redirect:/";
    }

    @GetMapping(value = "/admin/new")
    public String add(Model model) {
        User user = new User();
        Role role = new Role();
        model.addAttribute("user", user);
        model.addAttribute("role", role);
        model.addAttribute("roles", roleService.listRoles());
        return "/admin/new";
    }


    @GetMapping("/admin/{id}")
    public String getUser(@PathVariable("id") long id, Model model) {
        List<User> list = new ArrayList<>();
        list.add(userService.getUserById(id));
        model.addAttribute(list);
        return "admin/index";
    }

    @GetMapping("/admin/edit/{id}")
    public String editUser(Model model, @PathVariable("id") long id) {
        model.addAttribute("editUser", userService.getUserById(id));
        model.addAttribute("showRoles", roleService.listRoles());
        return "redirect:/";
    }

    @PostMapping("/admin/new")
    public String addUser(Model model, @ModelAttribute("user") User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.add(user);
        model.addAttribute("users", userService.getAllUsers());
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

    @GetMapping("/login")
    public String loginPage(ModelMap model) {
        return "login";
    }
}