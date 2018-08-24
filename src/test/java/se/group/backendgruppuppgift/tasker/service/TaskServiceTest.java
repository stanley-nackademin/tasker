package se.group.backendgruppuppgift.tasker.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import se.group.backendgruppuppgift.tasker.model.Task;
import se.group.backendgruppuppgift.tasker.model.TaskStatus;
import se.group.backendgruppuppgift.tasker.service.exception.InvalidTaskException;
import se.group.backendgruppuppgift.tasker.service.exception.InvalidUserException;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TaskServiceTest {

    Long userNumber, taskId;
    Task testTask;

    @Autowired
    UserService userService;

    @Autowired
    TaskService taskService;

    @Test
    public void updateTaskTest() {
        Task task = new Task("hej", TaskStatus.UNSTARTED);
        Task createdTask = taskService.createTask(task);

        Task updatedTask = taskService.updateTask(createdTask.getId(), new Task("", TaskStatus.STARTED)).get();
        assertEquals(TaskStatus.STARTED, updatedTask.getStatus());
    }

    @Test
    public void shouldDelegateFreeTaskToFreeUserTest() {

        userNumber = 1005L;
        taskId = 7L;

        userService.assignTaskToUser(userNumber, taskId);

        testTask = taskService.findTask(taskId).get();

        assertEquals(userNumber, testTask.getUser().getUserNumber());
        assertFalse(testTask.getUser().getUserNumber() == 1010L);

    }

    @Test(expected = InvalidUserException.class)
    public void shouldNotDelegateFreeTaskToInactiveUserTest() {

        userNumber = 1001L;
        taskId = 10L;

        userService.assignTaskToUser(userNumber, taskId);

        testTask = taskService.findTask(taskId).get();


    }

    @Test(expected = InvalidUserException.class)
    public void shouldNotDelegateFreeTaskToUserWithMoreThanFiveTasksTest() {

        userNumber = 1004L;
        taskId = 28L;

        userService.assignTaskToUser(userNumber, taskId);

        testTask = taskService.findTask(taskId).get();


    }

    @Test(expected = InvalidTaskException.class)
    public void shouldNotDelegateDelegatedTaskToUserTest() {

        userNumber = 1007L;
        taskId = 5L;

        userService.assignTaskToUser(userNumber, taskId);

        testTask = taskService.findTask(taskId).get();


    }
}