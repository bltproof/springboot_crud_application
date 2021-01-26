package ru.zaurbeck.springboot_crud_application.web.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.zaurbeck.springboot_crud_application.web.model.Role;
import ru.zaurbeck.springboot_crud_application.web.model.User;
import ru.zaurbeck.springboot_crud_application.web.service.RoleService;
import ru.zaurbeck.springboot_crud_application.web.service.UserService;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/rest")
public class AdminRestController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminRestController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/roles")
    public List<Role> getRoles() {
        return roleService.listRoles();
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable long id) {
        return userService.getUserById(id);
    }

    @PostMapping("/newUser")
    public List<User> newUser(@RequestBody User user) {
        setRoles(user);
        userService.add(user);
        return userService.getAllUsers();
    }

    @PutMapping("/updateUser")
    public List<User> updateUser(@RequestBody User user) {
        setRoles(user);
        userService.update(user);
        return userService.getAllUsers();
    }

    @DeleteMapping("/deleteUser/{id}")
    public List<User> deleteUser(@PathVariable long id) {
        userService.delete(userService.getUserById(id));
        return userService.getAllUsers();
    }

    private void setRoles(User user) {
        Collection<Role> receivedRoles = user.getRoles();
        Set<Role> roles = new HashSet<>();
        for (Role r : receivedRoles) {
            roles.add(roleService.getRoleByName(r.getName()));
        }
        user.setRoles(roles);
    }
}
