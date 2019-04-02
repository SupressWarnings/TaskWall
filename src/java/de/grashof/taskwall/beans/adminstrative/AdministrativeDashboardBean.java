package de.grashof.taskwall.beans.adminstrative;

import de.grashof.taskwall.beans.LoginBean;
import de.grashof.taskwall.beans.persistence.UserPersistence;
import de.grashof.taskwall.beans.task.CreateTaskBean;
import de.grashof.taskwall.entity.Profile;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Used for the first page an administrator sees when opening the administration
 * pages. Displays a list with all users and information on them.
 *
 * @author TaskWall-Team
 */
@RequestScoped
@Named
public class AdministrativeDashboardBean {

    /**
     * A List containing all users.
     */
    private List<Profile> users;

    /**
     * The currently selected user when the administrator uses a button in the
     * table.
     */
    private Profile current;

    /**
     * Used to check whether the user is allowed to see the administrative pages
     * or not.
     */
    @Inject
    private LoginBean loginBean;

    /**
     * Used to load the user list and check the current user.
     */
    @EJB
    private UserPersistence userPersistence;

    /**
     * Checks whether the current user is allowed to view the administrative
     * pages and, if not, redirects him to the login page/dashboard. Loads the
     * user List from the database.
     */
    @PostConstruct
    public void init() {
        if (!loginBean.getLoginSuccess()) {
            try {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.redirect(context.getApplicationContextPath() + "/login.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(CreateTaskBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (!userPersistence.getUser(loginBean.getUsername()).isAdministrator()) {
            try {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.redirect(context.getApplicationContextPath() + "/dashboard.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(CreateTaskBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            users = userPersistence.getUsers();
        }
    }

    /**
     * Resets the password of the currently selected user.
     */
    public void resetPassword() {
        if (current != null) {
            userPersistence.resetPassword(current.getUsername());
        }
    }

    /**
     * Gets the List of all users.
     *
     * @return a List containing all users
     */
    public List<Profile> getUsers() {
        return users;
    }

    /**
     * Gets the currently selected user.
     *
     * @return the currently selected user
     */
    public Profile getCurrent() {
        return current;
    }

    /**
     * Sets the currently selected user.
     *
     * @param current the new currently selected user
     */
    public void setCurrent(Profile current) {
        this.current = current;
    }
}
