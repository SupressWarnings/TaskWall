package de.grashof.taskwall.beans.persistence;

import de.grashof.taskwall.entity.ArchivedTask;
import de.grashof.taskwall.entity.Profile;
import de.grashof.taskwall.entity.Task;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Represents the interface between the appliation and the persistence storage
 * of users.
 *
 * @author TaskWall-Team
 */
@Stateless
public class TaskPersistence {

    /**
     * The EntityManager is the connection to the database of the application.
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Gives a List containing all Tasks assigned to the user.
     *
     * @param user the user which tasks are requested
     * @return a List containing all Tasks assigned to the user
     */
    public List<Task> getTasks(Profile user) {
        return entityManager.createNamedQuery("getTasks")
                .setParameter("receiverId", user.getId())
                .getResultList();
    }

    /**
     * Stores the new Task to the database.
     *
     * @param task the new Task
     */
    public void storeTask(Task task) {
        entityManager.persist(task);
    }

    /**
     * Removes a Task from the database and creates a new ArchivedTask and
     * stores it in the database.
     *
     * @param task the task that should be archived
     */
    public void archiveTask(Task task) {
        ArchivedTask oldTask = new ArchivedTask(task, new Date());
        entityManager.persist(oldTask);
        entityManager.createNamedQuery("deleteTask")
                .setParameter("id", task.getId())
                .executeUpdate();
    }
}
