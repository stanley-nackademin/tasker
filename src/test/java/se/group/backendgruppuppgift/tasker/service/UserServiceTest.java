package se.group.backendgruppuppgift.tasker.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import se.group.backendgruppuppgift.tasker.model.Team;
import se.group.backendgruppuppgift.tasker.model.User;
import se.group.backendgruppuppgift.tasker.repository.TeamRepository;
import se.group.backendgruppuppgift.tasker.repository.UserRepository;
import se.group.backendgruppuppgift.tasker.service.exception.InvalidTeamException;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    TeamService teamService;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    UserRepository userRepository;

    /*
     * PASSES if InvalidTeamException is thrown
     * which happens when there are more than 9 users in the requested team.
     * FAILS if InvalidTeamException is not thrown
     * which happens when there are less than 10 users in the requested team*/
    @Test(expected = InvalidTeamException.class)
    public void maxUserLimitValidationTest() throws Exception {
        //Optional<Team> result = userService.teamRepository.findByName("theFullTeam");
        Optional<Team> result = teamRepository.findByName("theFullTeam");
        Team team = result.get();
        userService.maxUserLimitValidation(team);
    }

    @Test
    public void createUserTest() {
        Optional<Team> tt = teamService.findTeam(1L);
        Team teamResult = tt.get();

        User u = new User(1008L, "apaaaaahaaaaaaaaaaaabaa", "asdasd", "lastname", teamResult);
        userService.createUser(u);

        Optional<User> result = userRepository.findByUserNumber(u.getUserNumber());
        User user = result.get();

        assertEquals(u.toString(), user.toString());
    }

    @Test
    public void deleteUserTest() {
        assertTrue(userService.deleteUserByUserNumber(1021L).isPresent());
        assertFalse(userService.findUserByUserNumber(1021L).isPresent());
    }

    @Test
    public void updateUserTest() {
        User gammalUser = userService.findUserByUserNumber(1020L).get();
        System.out.println(gammalUser);
        assertNotEquals(gammalUser, updateUserTest("nyttNamn", gammalUser.getUserNumber()));
    }

    User updateUserTest(String firstname, Long nr) {
        User user = userService.findUserByUserNumber(nr).get();
        user.setFirstName(firstname);
        userService.updateUser(nr, user);
        return user;
    }
}
