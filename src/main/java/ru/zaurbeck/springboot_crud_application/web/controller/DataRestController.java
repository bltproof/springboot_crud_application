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
@RequestMapping("/api")
public class DataRestController {

    private final UserService userService;
    private final RoleService roleService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public DataRestController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/roles")
    public List<Role> getRoles(){
        return roleService.listRoles();
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable long id) {
        return userService.getUserById(id);
    }

    @PostMapping("/users")
    public List<User> newUser(@RequestBody User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        setRoles(user);
        userService.add(user);
        return userService.getAllUsers();
    }

    @PutMapping("/users")
    public List<User> updateUser(@RequestBody User user){
        setRoles(user);
        userService.update(user);
        return userService.getAllUsers();
    }

    @DeleteMapping("/users/{id}")
    public List<User> deleteUser(@PathVariable long id){
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
