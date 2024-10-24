package fr.apside.formation.service;

import fr.apside.formation.entity.User;
import fr.apside.formation.exception.UserNotFoundException;
import fr.apside.formation.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository repository;

    UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<User> getUserPage(String lastname, Pageable pageable) {
        LOGGER.info("Get a paginated list of users");

        if (lastname != null && !lastname.isBlank()) {
            return repository.findAllByLastname(lastname, pageable);
        }
        return repository.findAll(pageable);
    }

    @Override
    public User getUserById(Long id) throws UserNotFoundException {
        LOGGER.info("Get a user by id");
        return repository.findById(id).orElseThrow(() -> new UserNotFoundException("Id: " + id));
    }

    @Override
    public User create(User user) {
        LOGGER.info("Create a user");
        return repository.save(user);
    }

    @Override
    public void update(User user) throws UserNotFoundException {
        LOGGER.info("Update a user");
        User userToUpdate = repository.findById(user.getId()).orElseThrow(() -> new UserNotFoundException(user));
        // Update data
        userToUpdate.setFirstname(user.getFirstname());
        userToUpdate.setLastname(user.getLastname());
        userToUpdate.setBirth(user.getBirth());
        // Save
        repository.save(userToUpdate);
    }

    @Override
    public void delete(Long userId) throws UserNotFoundException {
        LOGGER.info("Delete a user");
        repository.delete(this.getUserById(userId));
    }
}
