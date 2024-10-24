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

/**
 * Classe dans laquelle on implémente les services nécessaires aux différents endpoints
 */
@Service
public class UserServiceImpl implements UserService {// TODO implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    /**
     * Méthode d'accès au formation.service DAO pour la persistance
     *
     * @return l'instance du DAO
     */
    private UserDao getUserDao() {
        return UserDao.getInstance();
    }

    /**
     * Méthode de simulation de pagination à utiliser dans les getList*
     */
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
    public List<User> getUserList(int start, int rowCount) {
        List<User> userList = getUserDao().getUserList();
        return paginate(start, rowCount, userList);
    }

    @Override
    public List<User> getUserByLastname(String lastname, int start, int rowCount) {
        return paginate(start, rowCount, getUserDao().getUserByLastname(lastname));
    }

    @Override
    public User getUserById(Long userId) throws UserNotFoundException {
        User user = getUserDao().retrieveUser(userId);
        if (user == null) {
            throw new UserNotFoundException("Cet utilisateur n'existe pas");
        }

        return user;
    }

    @Override
    public User create(User user) {
        return getUserDao().createUser(user);
    }

    @Override
    public void update(User user) throws UserGlobalException, UserNotFoundException {
        getUserDao().updateUser(user);
    }

    @Override
    public void delete(Long userId) throws UserGlobalException, UserNotFoundException {
        getUserDao().deleteUser(userId);
    }
}
