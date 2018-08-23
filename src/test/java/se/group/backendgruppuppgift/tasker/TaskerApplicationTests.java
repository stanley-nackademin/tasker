package se.group.backendgruppuppgift.tasker;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import se.group.backendgruppuppgift.tasker.model.User;
import se.group.backendgruppuppgift.tasker.service.UserService;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TaskerApplicationTests {

    @Autowired
    UserService service;


    @Test
	public void contextLoads() {

//        service.createUser(new User(1001L, "olle", "OllesarriFörreallaandra", "Sarri", null));
//		User user = service.findLastUser();
//        System.out.println(user.toString());
//        assertEquals(service.findLastUser().getUsername(), "olle");
//        assertTrue(service.deleteUserByUserNumber(1001L).isPresent());
//        assertFalse(service.findUserByUserNumber(1001L).isPresent());

        assertTrue(service.updateUser(1001L, service.findUserByUserNumber(1001L).get()).isPresent());
        assertFalse(service.updateUser(1000L, service.findUserByUserNumber(1001L).get()).isPresent());

          User gammalUser = service.findUserByUserNumber(1001L).get();
        System.out.println(gammalUser);
        assertNotEquals(gammalUser, updateUserTest("nyttNamn", gammalUser.getUserNumber()));
        System.out.println(service.findUserByUserNumber(1001L));

//        assertFalse(service.findUserByUserNumber(1001L).isPresent());


	}

	User updateUserTest(String firstname, Long nr){
        User user = service.findUserByUserNumber(nr).get();
        user.setFirstName(firstname);
        service.updateUser(nr, user);
        return user;
    }

}
