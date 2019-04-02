package de.grashof.taskwall.entity;

import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Represents a user in the database. Contains all necessary data to store the
 * user data. At runtime the class is instantiated when a new user is created by
 * an administrator or a user tries to login.
 *
 * @author TaskWall-Team
 */
@Entity
@NamedQueries({
    @NamedQuery(
            name = "getAllUsers",
            query = "SELECT user FROM Profile user"),
    @NamedQuery(
            name = "getUserByUsername",
            query = "SELECT user FROM Profile user WHERE user.username = :username"),
    @NamedQuery(
            name = "getUserByEmail",
            query = "SELECT user FROM Profile user WHERE user.email = :email"),
    @NamedQuery(
            name = "getUserById",
            query = "SELECT user FROM Profile user WHERE user.id = :id"),
    @NamedQuery(
            name = "updatePw",
            query = "UPDATE Profile user SET user.password = :password WHERE user.username = :username"),
    @NamedQuery(
            name = "updateChangePw",
            query = "UPDATE Profile user SET user.changePassword = :changePassword WHERE user.username = :username"),
    @NamedQuery(
            name = "updateEmail",
            query = "UPDATE Profile user SET user.email = :email WHERE user.username = :username"),
    @NamedQuery(
            name = "updateEmailEnabled",
            query = "UPDATE Profile user SET user.notificationsEnabled = :emailEnabled WHERE user.username = :username"),
    @NamedQuery(
            name = "resetPassword",
            query = "UPDATE Profile user SET user.password = 'start', user.changePassword = true WHERE user.username = :username")
})
public class Profile implements Serializable {

    private static final long serialVersionUID = 3L;

    /**
     * An auto-generated id as the primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * The username.
     */
    @NotNull
    private String username;

    /**
     * The password needed for login.
     */
    @NotNull
    private String password;

    /**
     * The user's e-mail adress. It is used to send e-mails to the user when he
     * has notifications enabled. Only required when notifications are enabled.
     */
    @Pattern(regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\."
            + "[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@"
            + "(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9]"
            + "(?:[a-z0-9-]*[a-z0-9])?",
            message = "{invalid.email}")
    private String email;

    /**
     * Decides whether a user has to change his password after the next login.
     * true if the user is newly created or the password was reset.
     */
    private boolean changePassword = true;

    /**
     * If true, the email has to be set and the user gets notifications about
     * new Tasks.
     */
    private boolean notificationsEnabled = false;

    /**
     * If true the user is an administrator and allowed to configure the server
     * and create users.
     */
    private boolean administrator = false;

    /**
     * Standard empty constructor necessary for all Entity-classes.
     */
    public Profile() {
    }

    /**
     * Constructor used when a new user is created. The username is set to the
     * value of the parameter and the password is set as 'start'.
     *
     * @param username the username of the user
     */
    public Profile(String username) {
        this.username = username;
        password = "start";
    }

    /**
     * Gets the username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     *
     * @param username the new username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password the new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the email adress.
     *
     * @return the email adress
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email adress.
     *
     * @param email the new email adress
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the value of changePassword.
     *
     * @return true when the user has to change his password; false if he has
     * not
     */
    public boolean getChangePassword() {
        return changePassword;
    }

    /**
     * Sets the value of changePassword.
     *
     * @param changePassword the new value of changePassword
     */
    public void setChangePassword(boolean changePassword) {
        this.changePassword = changePassword;
    }

    /**
     * Gets the value of notificationsEnabled.
     *
     * @return true when an email is sent for new tasks; false if not
     */
    public boolean getNotificationsEnabled() {
        return notificationsEnabled;
    }

    /**
     * Sets the value of notificationsEnabled. As an email adress is required
     * for enabled notifications, if there is no assigned email adress, the
     * value of notificationsEnabled stays the same.
     *
     * @param notificationsEnabled the new value of notificationsEnabled
     */
    public void setNotificationsEnabled(boolean notificationsEnabled) {
        if (email != null && !email.equals("")) {
            this.notificationsEnabled = notificationsEnabled;
        }
    }

    /**
     * Gets whether the user has administrative rights.
     *
     * @return true when the user is an administrator; false if he is not
     */
    public boolean isAdministrator() {
        return administrator;
    }

    /**
     * Sets whether the user is an administrator.
     *
     * @param administrator the new value of the boolean administrator
     */
    public void setAdministrator(boolean administrator) {
        this.administrator = administrator;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id the new id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns a hash code value for the object.
     *
     * @return a hash code value
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param object the reference object with which to compare
     * @return true if this object is the same as the passed object argument;
     * false otherwise
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Task)) {
            return false;
        }
        Profile other = (Profile) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    /**
     * Returns a string representation of the object.
     *
     * @return a string representation of the object
     */
    @Override
    public String toString() {
        return "de.grashof.taskwall.entity.Profile[ id=" + id + " ]";
    }
}
