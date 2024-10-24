package fr.apside.formation.service;

import fr.apside.formation.entity.User;
import fr.apside.formation.exception.UserNotFoundException;
import fr.apside.formation.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class UserServiceImplTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    public void testFindAllByLastnameWhenThenReturnSomeOne() {

        User user = new User("Bob","Marley");

        Mockito.when(userRepository.findAllByLastname("Bob", null))
                .thenReturn(new PageImpl<User>(List.of(user)));

        Page<User> users = userService.getUserPage("Bob", null);

        Assertions.assertEquals(1, users.getTotalElements());
    }

    @Test
    public void testFindAllByLastnameWhenLastnameNullThenReturnAllUsers() {
        User user = new User("Bob","Marley");
        User user2 = new User("Jack","Grealish");

        Pageable pageable = null;

        Mockito.when(userRepository.findAll(pageable))
                .thenReturn(new PageImpl<>(List.of(user,user2)));

        Page<User> users = userService.getUserPage(null, null);

        Assertions.assertEquals(2, users.getTotalElements());
        Assertions.assertEquals("Jack", users.getContent().get(1).getLastname());
    }

    @Test
    public void testGetUserByIdWhenThenReturnUser() throws UserNotFoundException {
        User user = new User("Bob","Marley");
        Mockito.when(userRepository.findById(0L)).thenReturn(Optional.of(user));

        User bob = userService.getUserById(0L);
        Assertions.assertEquals("Bob", bob.getLastname());
        Assertions.assertEquals("Marley", bob.getFirstname());
        Assertions.assertEquals(null, bob.getId());
    }

    @Test
    public void testGetUserByIdWhenId5ThenExpectError() throws UserNotFoundException {
        Mockito.when(userRepository.findById(5L)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> userService.getUserById(5L));
    }

    @Test
    public void testCreateUserThenReturnTheSameUser() {
        User user = new User("Bob","Marley");
        Mockito.when(userRepository.save(user)).thenReturn(user);

        User user1 = userService.create(user);
        Assertions.assertEquals(user, user1);

    }

    @Test
    public void testUpdateUserWhenThenReturnUpdatedUser() throws UserNotFoundException {
        User userToUpdate = new User("Bob","Marley", LocalDate.of(2002,10,03));
        Mockito.when(userRepository.findById(userToUpdate.getId())).thenReturn(Optional.of(userToUpdate));


        Mockito.when(userRepository.save(userToUpdate)).thenReturn(userToUpdate);
        userService.update(userToUpdate);

        User updatedUser = userService.getUserById(userToUpdate.getId());
        Assertions.assertEquals(LocalDate.of(2002,10,03), updatedUser.getBirth());

    }

    @Test
    public void testUpdateUserWhenThenThrowException() throws UserNotFoundException {
        User userToUpdate = new User("Bob","Marley", LocalDate.of(2002,10,03));
        Mockito.when(userRepository.findById(userToUpdate.getId())).thenReturn(Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.update(userToUpdate));

    }

    @Test
    public void testDeleteUserSuccess() throws UserNotFoundException {
        User userToDelete = new User("Bob","Marley", LocalDate.of(2002,10,03));
        Mockito.when(userRepository.findById(userToDelete.getId())).thenReturn(Optional.of(userToDelete));

        userService.delete(userToDelete.getId());

        Mockito.verify(userRepository, Mockito.times(1)).delete(userToDelete);
        Mockito.verify(userRepository, Mockito.times(1)).findById(userToDelete.getId());

    }

    @Test
    public void testDeleteUserWhenId5ThenExpectError() throws UserNotFoundException {
        Mockito.when(userRepository.findById(5L)).thenReturn(Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.delete(5L));
    }
}