package se.group.backendgruppuppgift.tasker.repository;

import org.springframework.data.repository.CrudRepository;
import se.group.backendgruppuppgift.tasker.model.Issue;

public interface IssueRepository extends CrudRepository<Issue, Long> {
}
