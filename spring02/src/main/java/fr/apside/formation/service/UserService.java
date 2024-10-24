package fr.apside.formation.service;

import fr.apside.formation.entity.User;
import fr.apside.formation.exception.UserGlobalException;
import fr.apside.formation.exception.UserNotFoundException;

import java.util.List;

/**
 * Interface dans laquelle on déclare les services nécessaires aux différents endpoints
 */

public interface UserService {
    List<User> getUserList(int start, int rowCount);

    List<User> getUserByLastname(String lastname, int start, int rowCount);

    User getUserById(Long userId) throws UserNotFoundException;

    User create(User user);

    void update(User user) throws UserGlobalException, UserNotFoundException;

    void delete(Long userId) throws UserGlobalException, UserNotFoundException;
}
