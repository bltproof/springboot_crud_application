package ru.zaurbeck.springboot_crud_application.web.dao;

import org.springframework.stereotype.Repository;
import ru.zaurbeck.springboot_crud_application.web.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void add(User user) {
        entityManager.persist(user);
    }

    @Override
    public User getUserById(long id) {
        return entityManager.find(User.class, id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> getAllUsers() {
        return (List<User>) entityManager.createQuery("from User").getResultList();
    }

    @Override
    public void delete(User user) {
        entityManager.remove(user);
    }

    @Override
    public void deleteById(Long id) {
        entityManager.remove(getUserById(id));
    }

    @Override
    public void update(User user) {
        User obj = entityManager.find(User.class, user.getId());
        entityManager.detach(obj);
        obj.setUsername(user.getUsername());
        obj.setPassword(user.getPassword());
        obj.setAge(user.getAge());
        obj.setEmail(user.getEmail());
        obj.setRoles(user.getRoles());
        entityManager.merge(obj);
    }

    @Override
    public User getUserByName(String username) {
        Query query = entityManager.createQuery("FROM User u where u.username = :username");
        query.setParameter("username", username);

        return (User) query.getSingleResult();
    }
}