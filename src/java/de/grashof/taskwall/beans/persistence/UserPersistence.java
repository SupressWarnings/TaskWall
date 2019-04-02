package de.grashof.taskwall.beans.persistence;

import de.grashof.taskwall.entity.Profile;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Represents the interface between the application and the persistence storage
 * of users
 *
 * @author TaskWall Team
 */
@Stateless
public class UserPersistence {

    /**
     * The EntityManager is the connection to the database of the application.
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Gives a List with all users.
     *
     * @return a List containing all users
     */
    public List<Profile> getUsers() {
        return entityManager.createNamedQuery("getAllUsers")
                .getResultList();
    }

    /**
     * Gives the user with the passed username.
     *
     * @param username the username of the user that is requested
     * @return the Profile of the user
     */
    public Profile getUser(String username) {
        return (Profile) entityManager.createNamedQuery("getUserByUsername")
                .setParameter("username", username)
                .getSingleResult();
    }

    /**
     * Gives the user with the passed email.
     *
     * @param email the email of the user that is requested
     * @return the Profile of the user
     */
    public Profile getUserByEmail(String email) {
        return (Profile) entityManager.createNamedQuery("getUserByEmail")
                .setParameter("email", email)
                .getSingleResult();
    }

    /**
     * Gives the user with the passed id.
     *
     * @param id the id of the user that is requested
     * @return the Profile of the user
     */
    public Profile getUser(long id) {
        return (Profile) entityManager.createNamedQuery("getUserById")
                .setParameter("id", id)
                .getSingleResult();
    }

    /**
     * Checks whether the user with the passed username exists.
     *
     * @param username the username to be checked
     * @return true if the user exists; false if the user does not exist
     */
    public boolean userExists(String username) {
        return !entityManager.createNamedQuery("getUserByUsername")
                .setParameter("username", username)
                .getResultList().isEmpty();
    }

    /**
     * Checks whether the user with the passed email exists.
     *
     * @param email the email to be checked
     * @return true if the user exists; false if the user does not exist
     */
    public boolean userExistsEmail(String email) {
        return !entityManager.createNamedQuery("getUserByEmail")
                .setParameter("email", email)
                .getResultList().isEmpty();
    }

    /**
     * Saves the new user to the database.
     *
     * @param user the user that should be stored
     */
    public void createUser(Profile user) {
        entityManager.persist(user);
    }

    /**
     * Updates the password of the user.
     *
     * @param username the username of the user thats password should be updated
     * @param newPassword the new password of the user
     */
    public void updatePassword(String username, String newPassword) {
        entityManager.createNamedQuery("updatePw")
                .setParameter("username", username).setParameter("password", newPassword)
                .executeUpdate();
        entityManager.createNamedQuery("updateChangePw")
                .setParameter("username", username).setParameter("changePassword", false)
                .executeUpdate();
    }

    /**
     * Updates the email of the user.
     *
     * @param username the username of the user thats email should be updated
     * @param newEmail the new email of the user
     */
    public void updateEmail(String username, String newEmail) {
        entityManager.createNamedQuery("updateEmail")
                .setParameter("username", username).setParameter("email", newEmail)
                .executeUpdate();
    }

    /**
     * Updates whether a user receives email notifications about new Tasks.
     *
     * @param username the username
     * @param enabled true if the user wants to receive notifications; false if
     * he does not
     */
    public void updateEmailEnabled(String username, boolean enabled) {
        entityManager.createNamedQuery("updateEmailEnabled")
                .setParameter("username", username).setParameter("emailEnabled", enabled)
                .executeUpdate();
    }

    /**
     * Sets the password of the user to start and forces him to change his
     * password on the next login.
     *
     * @param username the username of the user thats password should be reset
     */
    public void resetPassword(String username) {
        entityManager.createNamedQuery("resetPassword")
                .setParameter("username", username)
                .executeUpdate();
    }
}
