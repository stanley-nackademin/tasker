package se.group.backendgruppuppgift.tasker.repository;

import se.group.backendgruppuppgift.tasker.model.Task;
import se.group.backendgruppuppgift.tasker.model.TaskStatus;

import java.util.List;

public interface TaskRepositoryCustom {

    List<Task> findTasksByParams(TaskStatus status, Long teamId, Long userId, String text, Boolean issue);
}
