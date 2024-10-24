package fr.apside.formation.batch.processor;

import fr.apside.formation.batch.domain.User;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * Processor exemple qui passe juste les données d'un user en majuscule
 */
@Component
public class UserProcessor implements ItemProcessor<User, User> {

    @Override
    public User process(User user) throws Exception {
        user.setFirstName(user.getFirstName().toUpperCase());
        user.setLastName(user.getLastName().toUpperCase());
        return user;
    }
}
