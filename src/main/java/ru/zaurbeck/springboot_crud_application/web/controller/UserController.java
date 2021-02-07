package ru.zaurbeck.springboot_crud_application.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.zaurbeck.springboot_crud_application.web.model.User;
import ru.zaurbeck.springboot_crud_application.web.service.RoleService;
import ru.zaurbeck.springboot_crud_application.web.service.UserService;

import java.security.Principal;

@Controller
@RequestMapping("/")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @GetMapping("/index")
    public String getUsers(Model model, Principal principal) {
        model.addAttribute("user", userService.getUserByName(principal.getName()));
//        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("listRoles", roleService.listRoles());
        return "index";
    }

    @GetMapping("/userpage/{id}")
    public String userspace(Model model, @PathVariable("id") long id, Principal principal) {
        User user = userService.getUserByName(principal.getName());
        if (user.getId() != id) {
            return "redirect:/403";
        }
        model.addAttribute("user", userService.getUserById(id));
        return "userpage";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "/login";
    }

    @GetMapping("/403")
    public String loginError() {
        return "403";
    }
}
