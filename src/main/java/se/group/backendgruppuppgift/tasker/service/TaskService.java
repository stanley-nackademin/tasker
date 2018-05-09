package se.group.backendgruppuppgift.tasker.service;

import org.springframework.stereotype.Service;
import se.group.backendgruppuppgift.tasker.model.Task;
import se.group.backendgruppuppgift.tasker.model.TaskStatus;
import se.group.backendgruppuppgift.tasker.repository.TaskRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public final class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public Task createTask(Task task){
        return repository.save(task);
    }

    public Optional<Task> findTask(Long id) {
        return repository.findById(id);
    }

    public void deleteTask(Long id){

        repository.deleteById(id);
    }

    public Collection<Task> getTaskByStatus(TaskStatus status){
        return repository.findByStatus(status);
    }
}
