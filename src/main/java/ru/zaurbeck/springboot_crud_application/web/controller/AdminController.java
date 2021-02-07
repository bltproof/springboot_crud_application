package ru.zaurbeck.springboot_crud_application.web.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminController(UserService userService,
                           RoleService roleService,
                           PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/roles")
    public ResponseEntity<?> getRoles() {
        return new ResponseEntity<>(roleService.listRoles(), HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {
        List<User> users = userService.getAllUsers();

        return users != null &&  !users.isEmpty()
                ? new ResponseEntity<>(users, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUser(@PathVariable long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/newUser")
    public ResponseEntity<?> newUser(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        setRoles(user);

        try {
            userService.add(user);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.CREATED);
    }

    @PutMapping("/updateUser")
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        if(!user.getPassword().equals(userService.getUserById(user.getId()).getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        setRoles(user);
        try {
            userService.update(user);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable long id) {
        userService.delete(userService.getUserById(id));
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
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
