package ru.zaurbeck.springboot_crud_application.web.dao;

import ru.zaurbeck.springboot_crud_application.web.model.Role;

import java.util.List;

public interface RoleDao {
    List<Role> listRoles();
    Role getRole(long id);
}