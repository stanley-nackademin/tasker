package se.group.backendgruppuppgift.tasker.service;

import org.springframework.stereotype.Service;
import se.group.backendgruppuppgift.tasker.model.Issue;
import se.group.backendgruppuppgift.tasker.model.Task;
import se.group.backendgruppuppgift.tasker.model.TaskStatus;
import se.group.backendgruppuppgift.tasker.model.User;
import se.group.backendgruppuppgift.tasker.repository.IssueRepository;
import se.group.backendgruppuppgift.tasker.repository.TaskRepository;
import se.group.backendgruppuppgift.tasker.repository.UserRepository;
import se.group.backendgruppuppgift.tasker.service.exception.InvalidIssueException;
import se.group.backendgruppuppgift.tasker.service.exception.InvalidTaskException;

import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static se.group.backendgruppuppgift.tasker.model.TaskStatus.*;

@Service
public final class TaskService {

    private final TaskRepository taskRepository;
    private final IssueRepository issueRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, IssueRepository issueRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.issueRepository = issueRepository;
        this.userRepository = userRepository;
    }

    public Task createTask(Task task) {
        validateTask(task);

        return taskRepository.save(new Task(task.getDescription(), task.getStatus()));
    }

    public Optional<Task> findTask(Long id) {
        return taskRepository.findById(id);
    }

    public List<Task> findTasksByParams(String status, String team, String user, String text, String issue) {
        return taskRepository.findTasksByParams(convertToStatus(status), convertToTeamId(team), convertToUserId(user), text, issueExist(issue));
    }

    public Optional<Task> updateTask(Long id, Task task) {
        Optional<Task> taskResult = taskRepository.findById(id);

        if (taskResult.isPresent()) {
            Task updatedTask = taskResult.get();

            if (!isBlank(task.getDescription())) {
                updatedTask.setDescription(task.getDescription());
            }

            if (task.getStatus() != null) {
                if (!isBlank(task.getStatus().toString())) {
                    updatedTask.setStatus(task.getStatus());
                }
            } else {
                throw new InvalidTaskException("Invalid status");
            }

            return Optional.of(taskRepository.save(updatedTask));
        }

        return taskResult;
    }

    public Optional<Task> assignIssue(Long id, Issue issue) {
        validateIssue(issue);
        Optional<Task> task = taskRepository.findById(id);

        if (task.isPresent()) {
            Task updatedTask = task.get();
            validateTaskStatus(updatedTask);
            Issue issueResult = issueRepository.save(new Issue(issue.getDescription()));

            updatedTask.setIssue(issueResult);
            updatedTask.setStatus(UNSTARTED);
            updatedTask = taskRepository.save(updatedTask);

            return Optional.of(updatedTask);
        }

        return task;
    }

    public Optional<Task> deleteTask(Long id) {
        Optional<Task> task = taskRepository.findById(id);

        if (task.isPresent()) {
            taskRepository.deleteById(id);
        }

        return task;
    }

    private String prepareString(String string) {
        return string.trim().toLowerCase();
    }

    private void validateTask(Task task) {
        if (task.getStatus() == null || isBlank(task.getDescription())) {
            throw new InvalidTaskException("Missing/invalid values");
        }
    }

    private void validateTaskStatus(Task task) {
        if (task.getStatus() != DONE) {
            throw new InvalidTaskException("The current Task's status is not DONE");
        }
    }

    private void validateIssue(Issue issue) {
        if (isBlank(issue.getDescription())) {
            throw new InvalidIssueException("Description can not be empty");
        }
    }

    private Long convertToUserId(String userNumber) {
        Long result = null;

        if (!userNumber.isEmpty()) {
            if (userNumber.matches("[0-9]+")) {
                Optional<User> user = userRepository.findByUserNumber(Long.parseLong(userNumber));

                result = user.isPresent() ? user.get().getId() : 0L;
            } else {
                result = 0L;
            }
        }

        return result;
    }

    private Long convertToTeamId(String team) {
        Long result = null;

        if (!team.isEmpty()) {
            result = team.matches("[0-9]+") ? Long.parseLong(team) : 0L;
        }

        return result;
    }

    private TaskStatus convertToStatus(String status) {
        switch (prepareString(status)) {
            case "started":
                return STARTED;
            case "unstarted":
                return UNSTARTED;
            case "done":
                return DONE;
            default:
                return null;
        }
    }

    private Boolean issueExist(String issue) {
        switch (prepareString(issue)) {
            case "true":
                return true;
            case "false":
                return false;
            default:
                return null;
        }
    }
}
