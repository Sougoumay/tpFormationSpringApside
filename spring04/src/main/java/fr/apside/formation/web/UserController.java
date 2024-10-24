package fr.apside.formation.web;

import fr.apside.formation.entity.User;
import fr.apside.formation.exception.UserGlobalException;
import fr.apside.formation.exception.UserNotFoundException;
import fr.apside.formation.service.UserService;
import fr.apside.formation.web.model.UserForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping(path = "/users", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    /**
     * Injection par constructeur
     */
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /************************
     *  Opérations de CRUD  *
     ************************/

    @GetMapping
    public Page<User> getUserList(@RequestParam(name = "lastname", required = false) String lastname,
                                  @PageableDefault(size = 5) Pageable pageable) {
        return userService.getUserPage(lastname, pageable);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable(value = "id") Long userId) throws UserNotFoundException {
        return userService.getUserById(userId);
    }

    /**
     * Sauvegarde par body
     */
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> createUserWithBody(
            @RequestBody User user,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        LOGGER.info("Creation avec le body");
        User userCreated = userService.create(user);
        if (userCreated != null) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(
                    uriComponentsBuilder.path("/users/{id}")
                            .buildAndExpand(userCreated.getId()).toUri()
            );
            return new ResponseEntity<User>(httpHeaders, HttpStatus.CREATED);
        }
        return new ResponseEntity<User>(HttpStatus.NOT_ACCEPTABLE);
    }

    /**
     * Sauvegarde par formulaire
     * Les dates peuvent poser problème, DONC LA DESCRIPTION L'API DOIT IMPOSER UN STANDARD !!!
     */
    @PostMapping(path = "/form", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<User> createUserWithForm(
            @ModelAttribute UserForm userForm,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        LOGGER.info("Creation avec un formulaire");
        if (userForm.getFirstname() != null && userForm.getLastname() != null) {
            User userToCreate = new User(userForm.getFirstname(), userForm.getLastname());
            User userCreated = userService.create(userToCreate);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(
                    uriComponentsBuilder
                            .path("/users/{id}")
                            .buildAndExpand(userCreated.getId()).toUri()
            );
            return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    /**
     * Sauvegarde par body
     */
    @PutMapping(value = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> updateUser(@PathVariable(value = "id") Long userId,
                                        @RequestBody User user) throws UserGlobalException, UserNotFoundException {
        if (user != null
                && user.getId() != null
                && user.getId().equals(userId)) {
            userService.update(user);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable(value = "id") Long userId) {
        try {
            if (userService.getUserById(userId) != null) {
                userService.delete(userId);
            }
        } catch (UserNotFoundException e) {
            LOGGER.warn("Tentative de suppression d'un user avec l'Id {}", userId);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Exception : ", e);
            }
        }
        return new ResponseEntity(HttpStatus.OK);
    }

}
