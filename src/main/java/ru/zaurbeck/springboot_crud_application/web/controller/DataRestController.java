package ru.zaurbeck.springboot_crud_application.web.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.zaurbeck.springboot_crud_application.web.model.Role;
import ru.zaurbeck.springboot_crud_application.web.model.User;
import ru.zaurbeck.springboot_crud_application.web.service.RoleService;
import ru.zaurbeck.springboot_crud_application.web.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class DataRestController {

    final private UserService userService;
    final private RoleService roleService;

    @Autowired
    public DataRestController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
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
        Set<Role> receivedRoles = (Set<Role>) user.getRoles();
        Set<Role> roles = new HashSet<>();
        for (Role r : receivedRoles) {
            roles.add(roleService.getRoleByName(r.getName()));
        }
        user.setRoles(roles);
    }
}
