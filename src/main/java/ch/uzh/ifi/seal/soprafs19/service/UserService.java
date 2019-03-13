package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;


    @Autowired
    public UserService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    public Iterable<User> getUsers() {

        return this.userRepository.findAll(); //returns all users in this userRepository
    }

    public User createUser(User newUser) {
        newUser.setToken(UUID.randomUUID().toString());
        newUser.setStatus(UserStatus.ONLINE);
        //add creation date
        //String pattern = "dd/MM/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = simpleDateFormat.format(new Date());
        newUser.setCreationDate(date);
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }
    public User getUserById(Long id){

        return this.userRepository.findUserById(id); //finds user in the database based on user id
    }

    // add method for updating the user
    public User updateUser(User user) {
        // Step 1: get user's id
        Long id = user.getId();
        User new_user = this.userRepository.findUserById(id);
        // Step 2: find user in the database based on his id
        // Step 3: update his info using the new data (you need the setters) the info you update is DOB and USERNAME
        new_user.setDateOfBirth(user.getDateOfBirth());
        new_user.setUsername(user.getUsername());
        new_user.setName(user.getName());
        userRepository.save(new_user);
        return new_user;
    }

    // add method for login
    //1)receive a user from the person. in order to be able to see if he has the privilege you have to get the users username
    //2)you have to get the username and pass in the database that corresponds to this user with the provided username
    //3)then have to check if the user from the database match the one given to me
    public User loginUser(User user){
        String username = user.getUsername();
        String pass = user.getPassword();
        User valid_user = this.userRepository.findByUsername(username);
        if (username.equals(valid_user.getUsername()) && pass.equals(valid_user.getPassword())){
            return valid_user;
        }
        return null;
    }
}

