package fr.apside.formation.dao;

import fr.apside.formation.entity.User;
import fr.apside.formation.exception.UserGlobalException;
import fr.apside.formation.exception.UserNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class UserDao {

    private static final UserDao DAO = new UserDao();
    private final ConcurrentHashMap<Long, User> userMap;
    private final AtomicLong userSequence;

    public UserDao() {
        userMap = new ConcurrentHashMap<>();
        userSequence = new AtomicLong(0);
    }

    public static synchronized UserDao getInstance() {
        return DAO;
    }

    public List<User> getUserList() {
        return new ArrayList<>(userMap.values());
    }

    public List<User> getUserByLastname(String lastname) {

        List<User> users = userMap.values().stream().filter(u -> u.getLastname().contains(lastname)).toList();

        return users;
    }

    public User createUser(User user) {
        user.setId(nextVal());
        userMap.put(user.getId(), user);
        return user;
    }

    private Long nextVal() {
        return userSequence.getAndIncrement();
    }

    public User retrieveUser(Long id) {
        return userMap.get(id);
    }

    public void updateUser(User user) throws UserGlobalException, UserNotFoundException {
        if (user == null) {
            throw new UserGlobalException("Ne peut sauvegarder un utilisateur null");
        }
        if (!userMap.containsKey(user.getId())) {
            throw new UserNotFoundException(user);
        }
        userMap.put(user.getId(), user);
    }

    public User deleteUser(Long id) throws UserGlobalException, UserNotFoundException {

        if (id == null) {
            throw new UserGlobalException("Ne peut supprimer un id null");
        }
        if (!userMap.containsKey(id)) {
            throw new UserNotFoundException("L'id " + id + " est inconnu");
        }
        User personToDelete = retrieveUser(id);
        userMap.remove(personToDelete.getId());
        return personToDelete;
    }

}
