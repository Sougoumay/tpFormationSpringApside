package fr.apside.formation.exception;

import fr.apside.formation.entity.User;


public class UserNotFoundException extends Throwable {

    private User user;

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(User user) {
        super(String.format("La personne d'id %d n'existe pas en base", user.getId()));
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
