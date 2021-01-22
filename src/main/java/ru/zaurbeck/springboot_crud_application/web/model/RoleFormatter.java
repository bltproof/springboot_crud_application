package ru.zaurbeck.springboot_crud_application.web.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;
import ru.zaurbeck.springboot_crud_application.web.dao.RoleDao;

import java.text.ParseException;
import java.util.Locale;

@Component
public class RoleFormatter implements Formatter<Role> {

    private RoleDao roleDao;

    @Autowired
    public RoleFormatter(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public Role parse(String s, Locale locale) {
        return roleDao.getRole(Long.parseLong(s));
    }

    @Override
    public String print(Role role, Locale locale) {

        return String.valueOf(role.getId());
    }
}
