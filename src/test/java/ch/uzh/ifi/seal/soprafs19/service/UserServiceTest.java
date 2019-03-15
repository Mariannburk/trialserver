package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.Application;
import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;

/**
 * Test class for the UserResource REST resource.
 *
 * @see UserService
 */
@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class)
public class UserServiceTest {


    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;
    @After
    public void deleteOutputFile() {
        userRepository.deleteAll();
    }

    @Test
    //tests createUser() method at UserService
    public void createUser() {
        //this test will show if the username is not null
        Assert.assertNull(userRepository.findByUsername("testUsername"));

        User testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");

        User createdUser = userService.createUser(testUser);

        Assert.assertNotNull(createdUser.getToken());
        Assert.assertNotNull(createdUser.getCreationDate());
        Assert.assertEquals(createdUser.getStatus(),UserStatus.OFFLINE); //when the user is created his is assigned an offline status
        Assert.assertEquals(createdUser, userRepository.findByToken(createdUser.getToken()));
    }


    @Test
    //tests getUserById() method at UserService
    public void getUserById() {

        User testUser = new User();
        //just for the future...testUser.setId(1L), L refers to long
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        userService.createUser(testUser);

        User createdUser = userService.getUserById(testUser.getId());
        Assert.assertEquals(testUser, createdUser); //can find user by id

    }


    @Test
    //tests loginUser() method at UserService
    public void loginUser() {

        User testUser = new User();
        testUser.setPassword("Mariann");
        testUser.setUsername("Mariann");

        User createdUser = userService.createUser(testUser);

        userService.loginUser(createdUser);
        Assert.assertEquals(userService.loginUser(createdUser).getStatus(),UserStatus.ONLINE);

    }

    @Test
    //tests getUsers() method at userService
    public void getUsers(){
        User testUserOne = new User();
        testUserOne.setPassword("testPassword1");
        testUserOne.setUsername("testUsername1");

        User testUserTwo = new User();
        testUserTwo.setPassword("testPassword2");
        testUserTwo.setUsername("testUsername2");

        User testUserThree = new User();
        testUserThree.setPassword("testPassword3");
        testUserThree.setUsername("testUsername3");

        User createdUserOne = userService.createUser(testUserOne);
        User createdUserTwo = userService.createUser(testUserTwo);
        User createdUserThree = userService.createUser(testUserThree);

        Iterable<User> users = userService.getUsers();
        ArrayList<User> list = new ArrayList<>();

        for (User user: users){
            list.add(user);
        }

        Assert.assertEquals(createdUserOne, list.get(0));
        Assert.assertEquals(createdUserTwo, list.get(1));
        Assert.assertEquals(createdUserThree, list.get(2));
    }

    @Test
    //tests updateuser() method at userService
    public void updateUser(){
        //firstly I create 3 test users
        User testUserOne = new User();
        testUserOne.setUsername("Mariann");
        testUserOne.setPassword("testPassword1");
        userService.createUser(testUserOne);

        User testUserTwo = new User();
        testUserTwo.setUsername("Burk");
        testUserTwo.setPassword("testPassword2");
        userService.createUser(testUserTwo);

        User testUserThree = new User();
        testUserThree.setUsername("Birgit");
        testUserThree.setPassword("testPassword3");
        userService.createUser(testUserThree);

        //this is where I update the username of the testUserOne
        User updateUserOne = new User();
        updateUserOne.setId(testUserOne.getId());
        // gets the Id to ensure updating is possible
        updateUserOne.setUsername("mariann");
        userService.updateUser(updateUserOne);

        //this is where I update the username and date of birth of testUserTwo
        User updateUserTwo = new User();
        updateUserTwo.setId(testUserTwo.getId());
        updateUserTwo.setUsername("burk");
        updateUserTwo.setDateOfBirth("12.18.1994");
        userService.updateUser(updateUserTwo);

        //this is where I update birth date of the testUserThree
        User updateUserThree = new User();
        updateUserThree.setId(testUserThree.getId());
        updateUserThree.setUsername(testUserThree.getUsername());
        updateUserThree.setDateOfBirth("19.12.1995");
        userService.updateUser(updateUserThree);

        //username has been updated
        Assert.assertEquals("mariann", userRepository.findUserById(testUserOne.getId()).getUsername());
        Assert.assertNull(userRepository.findUserById(testUserOne.getId()).getDateOfBirth());

        //username and birthday have been updated
        Assert.assertEquals("burk", userRepository.findUserById(testUserTwo.getId()).getUsername());
        Assert.assertEquals("12.18.1994", userRepository.findUserById(testUserTwo.getId()).getDateOfBirth());

        //birthday has been updated
        Assert.assertEquals("Birgit", userRepository.findUserById(testUserThree.getId()).getUsername());
        Assert.assertEquals("19.12.1995", userRepository.findUserById(testUserThree.getId()).getDateOfBirth());
    }

}