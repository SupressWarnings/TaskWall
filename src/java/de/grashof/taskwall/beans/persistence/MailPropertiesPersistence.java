package de.grashof.taskwall.beans.persistence;

import de.grashof.taskwall.entity.MailProperties;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Represents the interface between the application and the persistence storage
 * of the MailProperties.
 *
 * @author TaskWall-Team
 */
@Stateless
public class MailPropertiesPersistence {

    /**
     * The EntityManager is the connection to the database of the application.
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Returns the Singleton MailProperties object stored in the database.
     *
     * @return the MailProperties object
     */
    public MailProperties getMailProperties() {
        return (MailProperties) entityManager.createNamedQuery("getMailProperties")
                .getSingleResult();
    }

    /**
     * Updates the Properties object of the MailProperties object in the
     * database.
     *
     * @param mailProperties the MailProperties object containing the new
     * Properties
     */
    public void setMailProperties(MailProperties mailProperties) {
        entityManager.createNamedQuery("updateProperties")
                .setParameter("properties", mailProperties.getProperties())
                .executeUpdate();
    }
}
