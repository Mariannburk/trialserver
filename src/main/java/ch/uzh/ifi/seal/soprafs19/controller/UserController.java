package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController

public class UserController {

    private final UserService service;

    UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/users")
    Iterable<User> all() {

        return service.getUsers();
    }

    @PostMapping("/users") // has to modified to allow registration
    public ResponseEntity<User> createUser(@RequestBody User newUser) {
        try {
            this.service.createUser(newUser);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED); //returns status code 201, add user
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT); //returns the Status code 409, add user failed because username already exists
        }
    }


    @PostMapping("/login") // create a method like the previous one that allows login
    public ResponseEntity<User> loginUser(@RequestBody User user) {
        try {
            User loginUser = this.service.loginUser(user);
            if (loginUser != null) {
                return new ResponseEntity<>(loginUser, HttpStatus.OK); //returns status code 200, valid user
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND); //returns the status code 404, not found, not valid details
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); //returns the status code 404, not found
        }
    }


    @PutMapping("/users/{id}") // create a method in order to update the user info
   // @CrossOrigin(origins = "http://localhost:3000")  //for updating the username
    //check if the credentials are ok
    public ResponseEntity<String> updateUser(@RequestBody User user) {
        try {
            this.service.updateUser(user);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); //returns status code 204
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); //returns status code 404
        }
    }


    @GetMapping("/users/{id}") // method that finds a user by his id (use the method in UserService)
    public @ResponseBody
    ResponseEntity<User> getPersonById(@PathVariable Long id) {
        User user = this.service.getUserById(id);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK); //status code 200
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); //status code 404
        }
    }
}
