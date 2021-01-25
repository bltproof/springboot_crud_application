package ru.zaurbeck.springboot_crud_application.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.zaurbeck.springboot_crud_application.web.model.Role;
import ru.zaurbeck.springboot_crud_application.web.model.User;
import ru.zaurbeck.springboot_crud_application.web.service.RoleService;
import ru.zaurbeck.springboot_crud_application.web.service.UserService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "/login";
    }

    @GetMapping("/index")
    public String getUsers(Model model, Principal principal) {
        User newUser = new User();
        List<Role> showRoles = roleService.listRoles();
        model.addAttribute("newUser", newUser);
        model.addAttribute("showRoles", showRoles);
        model.addAttribute("user", userService.getUserByName(principal.getName()));
        model.addAttribute("users", userService.getAllUsers());
        return "index";
    }

    @GetMapping("/userpage/{id}")
    public String userspace(Model model, @PathVariable("id") long id, Principal principal) {
        User user = userService.getUserByName(principal.getName());
        if(user.getId() != id) {
            return "redirect:/403";
        }
        model.addAttribute("user", userService.getUserById(id));
        return "userpage";
    }

    @GetMapping("/403")
    public String loginError() {
        return "403";
    }
}
