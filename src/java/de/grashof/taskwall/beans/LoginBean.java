package de.grashof.taskwall.beans;

import de.grashof.taskwall.beans.persistence.UserPersistence;
import de.grashof.taskwall.entity.ArchivedTask;
import de.grashof.taskwall.entity.Task;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 * Instantiated when a user opens the login page and kept for the whole session.
 * It contains the username and password of the user and whether the login was
 * successfull or not.
 *
 * @author TaskWall-Team
 */
@SessionScoped
@Named("loginBean")
public class LoginBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The username.
     */
    private String username;

    /**
     * The password.
     */
    private String password;

    /**
     * Indicating whether there was a successfull login.
     */
    private boolean loginSuccess = false;

    /**
     * Indicating whether there was a failed login attempt.
     */
    private boolean loginFail = false;

    /**
     * A pointer to the current Task used when a user wants to see the details
     * of this Task.
     */
    private Task current;

    /**
     * A pointer to the current finished Task used when the user wants to see
     * the details of a finished Task.
     */
    private ArchivedTask currentFinished;

    /**
     * Used to check the user's login data.
     */
    @EJB
    private UserPersistence userPersistence;

    /**
     * Checks whether the user is already logged in and reconnects to the
     * dashboard if he is.
     */
    public void init() {
        if (loginSuccess) {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            try {
                context.redirect("dashboard.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Checks the user's login data and redirects to the next page. Also checks
     * whether the user has to change his password.
     *
     * @return a navigation outcome suggesting where the user is navigated to
     */
    public String login() {
        if (userPersistence.userExists(username)) {
            if (userPersistence.getUser(username).getPassword().equals(password)) {
                loginFail = false;
                loginSuccess = true;
                if (userPersistence.getUser(username).getChangePassword()) {
                    return "changePassword";
                }
                return "dashboard";
            }
        }
        loginFail = true;
        return "loginFail";
    }

    /**
     * Resets the login data and redirects to the login page.
     *
     * @return a navigation outcome navigating to the login page
     */
    public String logout() {
        loginSuccess = false;
        loginFail = false;
        username = "";
        password = "";
        return "logout";
    }

    /**
     * Gets the currently selected Task.
     *
     * @return the currently selected Task
     */
    public Task getCurrent() {
        return current;
    }

    /**
     * Sets the currently selected Task.
     *
     * @param task the new currently selected Task
     */
    public void setCurrent(Task task) {
        this.current = task;
    }

    /**
     * Gets the current finished Task.
     *
     * @return the current finished Task
     */
    public ArchivedTask getCurrentFinished() {
        return currentFinished;
    }

    /**
     * Sets the current finished Task.
     *
     * @param currentFinished the new current finishedTask
     */
    public void setCurrentFinished(ArchivedTask currentFinished) {
        this.currentFinished = currentFinished;
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
     * Gets whether there has been a successfull login.
     *
     * @return true if there was a successfull login; false if there was no
     * successfull login
     */
    public boolean getLoginSuccess() {
        return loginSuccess;
    }

    /**
     * Gets whether there has been a failed login attempt.
     *
     * @return true if there was a failed login attempt; false if there was no
     * failed login attempt
     */
    public boolean isLoginFail() {
        return loginFail;
    }
}
