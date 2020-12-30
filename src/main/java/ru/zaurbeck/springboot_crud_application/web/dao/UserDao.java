package ru.zaurbeck.springboot_crud_application.web.dao;

import ru.zaurbeck.springboot_crud_application.web.model.User;

import java.util.List;

public interface UserDao {

    void add(User user);

    User getUserById(long id);

    List<User> getAllUsers();

    void delete(User user);

    void deleteById(Long id);

    void update(User user);

    User getUserByName(String username);
}