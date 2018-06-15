package se.group.backendgruppuppgift.tasker.repository;

import org.springframework.stereotype.Repository;
import se.group.backendgruppuppgift.tasker.model.Task;
import se.group.backendgruppuppgift.tasker.model.TaskStatus;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class TaskRepositoryImpl implements TaskRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Task> findTasksByParams(TaskStatus status, Long teamId, Long userId, String text, Boolean issue) {
        return em.createQuery(
                "SELECT DISTINCT t FROM Task t LEFT JOIN User u ON t.user.id = u.id " +
                        "LEFT JOIN Team team ON u.team.id = team.id " +
                        "WHERE t.description LIKE :description " +
                        "AND (:id IS NULL OR t.user.id = :id) " +
                        "AND (:status IS NULL OR t.status = :status) " +
                        "AND (:team IS NULL OR team.id = :team) " +
                        "AND (:issue IS NULL " +
                        "OR (:issue IS NOT NULL AND :issue = true AND t.issue IS NOT NULL) " +
                        "OR (:issue IS NOT NULL AND :issue = false AND t.issue IS NULL))"
        )
                .setParameter("status", status)
                .setParameter("team", teamId)
                .setParameter("id", userId)
                .setParameter("description", "%" + text + "%")
                .setParameter("issue", issue)
                .getResultList();
    }
}
