package de.grashof.taskwall.beans;

import de.grashof.taskwall.beans.persistence.TaskPersistence;
import de.grashof.taskwall.beans.persistence.UserPersistence;
import de.grashof.taskwall.beans.task.CreateTaskBean;
import de.grashof.taskwall.entity.Profile;
import de.grashof.taskwall.entity.Task;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
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
 * Loads a list with all Tasks assigned to the logged in user and redirects to
 * show the details of one of them.
 *
 * @author TaskWall-Team
 */
@RequestScoped
@Named("dashboardBean")
public class DashboardBean implements Serializable {

    /**
     * Indicates whether administrative navigation buttons are displayed.
     */
    private boolean showAdmin = false;

    /**
     * A list containing all Tasks assigned to the user.
     */
    private List<Task> tasks;

    /**
     * Used to get the user's login data.
     */
    @Inject
    private LoginBean loginBean;

    /**
     * Used to load the user object representation from the database to get his
     * assigned Tasks.
     */
    @EJB
    private UserPersistence userPersistence;

    /**
     * Used to load the Tasks assigned to the user.
     */
    @EJB
    private TaskPersistence taskPersistence;

    /**
     * Checks whether there was a successfull login and redirects to the login
     * page if not. Loads all Tasks assigned to the user if there was a
     * successfull login.
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
        } else {
            tasks = new ArrayList<>();
            Profile user = userPersistence.getUser(loginBean.getUsername());
            tasks = taskPersistence.getTasks(user);
            showAdmin = user.isAdministrator();
        }
    }

    /**
     * Gets whether administrative navigation buttons should be displayed.
     *
     * @return true if the navigation buttons should be displayed; false if they
     * should not be displayed
     */
    public boolean getShowAdmin() {
        return showAdmin;
    }

    /**
     * Gets the List containing all Tasks assigned to the user.
     *
     * @return a List containing all Tasks assigned to the user
     */
    public List<Task> getTasks() {
        return tasks;
    }
}
