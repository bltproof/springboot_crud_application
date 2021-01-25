package ru.zaurbeck.springboot_crud_application.web.service;

import ru.zaurbeck.springboot_crud_application.web.model.Role;

import java.util.List;

public interface RoleService {
    List<Role> listRoles();
    Role getRoleByName(String name);
}