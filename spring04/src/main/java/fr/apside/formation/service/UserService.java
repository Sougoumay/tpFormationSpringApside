package fr.apside.formation.service;

import fr.apside.formation.entity.User;
import fr.apside.formation.exception.UserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    Page<User> getUserPage(String lastname, Pageable pageable);

    User getUserById(Long userId) throws UserNotFoundException;

    User create(User user);

    void update(User user) throws UserNotFoundException;

    void delete(Long userId) throws UserNotFoundException;
}
