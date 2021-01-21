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

    @GetMapping(value = "/")
    public String showAllUsers(@ModelAttribute("user") User user, Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "/admin/index";
    }

    @GetMapping(value = "/user/show/{id}")
    public String showUser(@PathVariable("id") long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "/user/show";
    }

    @DeleteMapping(value = "/delete/{id}")
    public String deleteUser(@ModelAttribute("user") User user) {
        userService.delete(user);
        return "redirect:/";
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

    @PostMapping(value = "/admin/new")
    public String addUser(Model model, @ModelAttribute("user") User user, @ModelAttribute("role") Role role) {
        Role newRole = roleService.listRoles()
                .stream()
                .filter(x -> x.getName().equals(role.getName()))
                .findAny().get();

        User obj = new User(
                user.getUsername(),
                passwordEncoder.encode(user.getPassword()),
                user.getAge(),
                user.getEmail());

        obj.setRole(newRole);
        userService.add(obj);
        model.addAttribute("users", userService.getAllUsers());
        return "redirect:/";
    }

    @GetMapping(value = "/admin/edit/{id}")
    public String editUser(@PathVariable("id") long id, Model model) {
        User user = userService.getUserById(id);

        Role role = user.getRoles().stream().findFirst().orElse(new Role());

        model.addAttribute("user", user);
        model.addAttribute("role", role);
        model.addAttribute("roles", roleService.listRoles());
        return "/admin/edit";
    }

    @PostMapping(value = "/admin/edit/{id}")
    public String updateUser(@ModelAttribute("user") User user, @ModelAttribute Role role) {
        Role newRole = roleService.listRoles()
                .stream()
                .filter(r -> r.getName().equals(role.getName()))
                .findAny().orElse(null);


        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setRole(newRole);

        userService.update(user);

        return "redirect:/";
    }
    @GetMapping("/login")
    public String loginPage(ModelMap model) {
        return "login";
    }
}