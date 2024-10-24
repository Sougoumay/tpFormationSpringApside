package fr.apside.formation.repository;


import fr.apside.formation.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    public void testFindByUsername() {
        User user = new User("Bob", "Marley");
        entityManager.persistAndFlush(user);

        Page<User> found = userRepository.findAllByLastname(user.getLastname(), Pageable.unpaged());
        Assertions.assertEquals(1, found.getTotalElements());
        Assertions.assertEquals("Bob", found.getContent().get(0).getLastname());
    }
}