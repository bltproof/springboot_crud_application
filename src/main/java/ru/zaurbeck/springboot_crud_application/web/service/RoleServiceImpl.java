package ru.zaurbeck.springboot_crud_application.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.zaurbeck.springboot_crud_application.web.dao.RoleDao;
import ru.zaurbeck.springboot_crud_application.web.model.Role;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService{

    private final RoleDao roleDao;

    @Autowired
    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public List<Role> listRoles() {
        return roleDao.listRoles();
    }

}
