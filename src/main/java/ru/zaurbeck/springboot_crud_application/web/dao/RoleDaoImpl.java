package ru.zaurbeck.springboot_crud_application.web.dao;

import org.springframework.stereotype.Repository;
import ru.zaurbeck.springboot_crud_application.web.model.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class RoleDaoImpl implements RoleDao{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Role> listRoles() {
        Query query = entityManager.createQuery("SELECT role FROM Role role ");
        return query.getResultList();
    }

    @Override
    public Role getRole(long id) {
        return entityManager.find(Role.class, id);

    }
}