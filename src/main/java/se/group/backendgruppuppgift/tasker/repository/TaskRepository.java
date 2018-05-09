package se.group.backendgruppuppgift.tasker.repository;

import org.springframework.data.repository.CrudRepository;
import se.group.backendgruppuppgift.tasker.model.Task;
import se.group.backendgruppuppgift.tasker.model.TaskStatus;

import java.util.Collection;

public interface TaskRepository extends CrudRepository<Task, Long> {
        Collection<Task> findByStatus(TaskStatus status);
    }
