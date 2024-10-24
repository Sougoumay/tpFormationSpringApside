package fr.apside.formation.web;

import fr.apside.formation.entity.User;
import fr.apside.formation.exception.UserGlobalException;
import fr.apside.formation.exception.UserNotFoundException;
import fr.apside.formation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;

/**
 * Classe dans laquelle on implémente les différents endpoints de CRUD
 * Create a user (body/form), Get paginated users, Get users by lastname, Get user by id, Update a user, Delete a user)
 */

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getUsers(
            @RequestParam(name = "lastname", required = false) String lastname,
            @RequestParam(name = "start", defaultValue = "1", required = false) int start,
            @RequestParam(name = "userPerPage") int userPerPage) {
        List<User> users = lastname != null ? userService.getUserByLastname(lastname,start,userPerPage) : userService.getUserList(start, userPerPage);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user;
        try {
            user = userService.getUserById(id);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user);
    }

//    @PostMapping(consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
//    public ResponseEntity<User> addUser(@ModelAttribute User user, UriComponentsBuilder uriBuilder) {
//        User createdUser = userService.create(user);
//        if (createdUser != null) {
//            HttpHeaders headers = new HttpHeaders();
//            headers.setLocation(uriBuilder.path("/users/{id}").buildAndExpand(createdUser.getId()).toUri());
//
//            return new ResponseEntity<>(headers,HttpStatus.CREATED);
//        }
//
//        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
//    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<User> addUser(@RequestBody User user, UriComponentsBuilder uriBuilder) {
        User createdUser = userService.create(user);
        if (createdUser != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(uriBuilder.path("/users/{id}").buildAndExpand(createdUser.getId()).toUri());

            return new ResponseEntity<>(headers,HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    @PutMapping(path = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable Long id) {
        if (user != null && user.getId() != null && user.getId().equals(id)) {
            try {
                userService.update(user);
                return ResponseEntity.ok(user);
            } catch (UserGlobalException e) {
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            } catch (UserNotFoundException e) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

//    @PutMapping(path = "/{id}", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
//    public ResponseEntity<User> updateUser(@ModelAttribute User user, @PathVariable Long id) {
//        if (user != null && user.getId() != null && user.getId().equals(id)) {
//            try {
//                userService.update(user);
//            } catch (UserGlobalException e) {
//                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
//            } catch (UserNotFoundException e) {
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            }
//        }
//
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Long id) {
        try {
            userService.delete(id);
        } catch (UserNotFoundException | UserGlobalException e) {
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }



}
