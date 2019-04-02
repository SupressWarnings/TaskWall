package de.grashof.taskwall.beans.task;

import de.grashof.taskwall.beans.LoginBean;
import de.grashof.taskwall.beans.persistence.TaskPersistence;
import de.grashof.taskwall.beans.persistence.UserPersistence;
import de.grashof.taskwall.entity.Task;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * This bean is requested when a user views the details of a Task. It is view
 * scoped so the bean "remembers" the task the user is looking at.
 *
 * @author TaskWall-Team
 */
@ViewScoped
@Named("viewTaskBean")
public class ViewTaskBean implements Serializable {

    /**
     * The task the user is looking at.
     */
    private Task task;

    /**
     * The title of the task and value of an outputText-field.
     */
    private String title;

    /**
     * The description of the task and value of an outputText-field.
     */
    private String description;

    /**
     * The username of the creator of the Task and value of an outputText-field.
     */
    private String creator;

    /**
     * The deadline of the task and value of an outputText-field.
     */
    private Date due;

    /**
     * The date the task was created and value of an outputText-field.
     */
    private Date created;

    /**
     * Checks whether the bean is already initialized. If it is not the values
     * for all fields are loaded from the DashboardBean.
     */
    private boolean initialized = false;

    /**
     * The LoginBean containing the Task the user wants to see.
     */
    @Inject
    private LoginBean loginBean;

    /**
     * The UserPersistence used to retrieve the username of the creator.
     */
    @EJB
    private UserPersistence userPersistence;

    /**
     * The TaskPersistence used to archive the Task.
     */
    @EJB
    private TaskPersistence taskPersistence;

    /**
     * Initializes the bean if it is not already. Retrieves all field values
     * from the current Task in the DashbaordBean.
     */
    @PostConstruct
    public void init() {
        if (!initialized) {
            task = loginBean.getCurrent();
            title = task.getTitle();
            description = task.getDescription();
            creator = userPersistence.getUser(task.getCreatorId()).getUsername();
            due = task.getDue();
            created = task.getCreated();
            initialized = true;
        }
    }

    /**
     * Archives the Task and navigates to the dashboard.
     *
     * @return the navigation outcome redirecting to the dashboard
     */
    public String finish() {
        taskPersistence.archiveTask(task);
        return "return";
    }

    /**
     * Gets the title of the Task.
     *
     * @return the title of the Task
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the description of the Task
     *
     * @return the description of the Task
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the creator of the Task.
     *
     * @return the creator of the Task
     */
    public String getCreator() {
        return creator;
    }

    /**
     * Gets the deadline of the Task.
     *
     * @return the deadline of the Task
     */
    public Date getDue() {
        return due;
    }

    /**
     * Gets the creation date of the Task.
     *
     * @return the creation date of Task
     */
    public Date getCreated() {
        return created;
    }
}
