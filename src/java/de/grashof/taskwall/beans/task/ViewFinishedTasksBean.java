package de.grashof.taskwall.beans.task;

import de.grashof.taskwall.beans.LoginBean;
import de.grashof.taskwall.beans.persistence.ArchivedTasksPersistence;
import de.grashof.taskwall.beans.persistence.UserPersistence;
import de.grashof.taskwall.entity.ArchivedTask;
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
 * This bean is used to display a list of the Tasks the user already finished.
 *
 * @author TaskWall-Team
 */
@Named("viewFinishedTasksBean")
@RequestScoped
public class ViewFinishedTasksBean {

    /**
     * A list containing all finished Tasks that were assigned to the user.
     */
    private List<ArchivedTask> tasks;

    /**
     * Used to check whether the user is allowed to see this site and to
     * retrieve his username to get the list of Tasks.
     */
    @Inject
    private LoginBean loginBean;

    /**
     * The UserPersistence used to retrieve the id of the user.
     */
    @EJB
    private UserPersistence userPersistence;

    /**
     * Used to get the List with the Tasks the user finished.
     */
    @EJB
    private ArchivedTasksPersistence archivedTasksPersistence;

    /**
     * Redirects to the login page if the user is not logged in and sets the
     * tasks list if he is.
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
            tasks = archivedTasksPersistence.getFinishedTasks(
                    userPersistence.getUser(loginBean.getUsername()).getId());

        }
    }

    /**
     * Gets the List containing the finished Tasks
     *
     * @return the Tasks list
     */
    public List<ArchivedTask> getTasks() {
        return tasks;
    }
}
