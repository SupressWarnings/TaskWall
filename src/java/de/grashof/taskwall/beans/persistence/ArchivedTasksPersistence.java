package de.grashof.taskwall.beans.persistence;

import de.grashof.taskwall.entity.ArchivedTask;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Represents the interface between the application and the storage of Archived
 * Tasks.
 *
 * @author TaskWall-Team
 */
@Stateless
public class ArchivedTasksPersistence {

    /**
     * The EntityManager is the connection to the database of the application.
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Returns a List containing all ArchivedTasks assigned to a user.
     *
     * @param receiverId the id of the user that was assigned the Tasks
     * @return a List containing all finished Tasks that were assigned to the
     * user
     */
    public List<ArchivedTask> getFinishedTasks(long receiverId) {
        return entityManager.createNamedQuery("getFinishedTasks")
                .setParameter("receiverId", receiverId)
                .getResultList();
    }
}
