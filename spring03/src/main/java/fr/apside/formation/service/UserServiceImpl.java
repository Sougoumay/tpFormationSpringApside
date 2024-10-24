package fr.apside.formation.service;

import fr.apside.formation.dao.UserDao;
import fr.apside.formation.entity.User;
import fr.apside.formation.exception.UserGlobalException;
import fr.apside.formation.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserDao getUserDao() {
        return UserDao.getInstance();
    }

    @Override
    public List<User> getUserList(int start, int rowCount) {
        LOGGER.info("Get a paginated list of users");
        List<User> personList = getUserDao().getUserList();
        return paginate(start, rowCount, personList);
    }

    @Override
    public List<User> getUserByLastname(String lastname, int start, int rowCount) {
        LOGGER.info("Get users by lastname");
        return paginate(start, rowCount,
                getUserDao()
                        .getUserList().stream()
                        .filter(person -> person.getLastname().equals(lastname))
                        .collect(Collectors.toList())
        );
    }

    private List<User> paginate(int start, int rowCount, List<User> userList) {
        if (userList.isEmpty()) {
            return new ArrayList<>();
        }
        if (start > userList.size()) {
            return new ArrayList<>();
        }
        if ((start + rowCount) > userList.size()) {
            return userList.subList(start, userList.size());
        }
        return userList.subList(start, start + rowCount);
    }

    @Override
    public User getUserById(Long id) throws UserNotFoundException {
        LOGGER.info("Get a user by id");
        User user = getUserDao().retrieveUser(id);
        if (user == null) {
            throw new UserNotFoundException("Id: " + id);
        }
        return user;
    }

    @Override
    public User create(User user) {
        LOGGER.info("Create a user");
        return getUserDao().createUser(user);
    }

    @Override
    public void update(User user) throws UserGlobalException, UserNotFoundException {
        LOGGER.info("Update a user");
        getUserDao().updateUser(user);
    }

    @Override
    public void delete(Long userId) throws UserGlobalException, UserNotFoundException {
        LOGGER.info("Delete a user");
        getUserDao().deleteUser(getUserDao().retrieveUser(userId));
    }
}
