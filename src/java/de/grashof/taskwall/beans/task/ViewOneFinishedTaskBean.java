package de.grashof.taskwall.beans.task;

import de.grashof.taskwall.beans.LoginBean;
import de.grashof.taskwall.beans.persistence.UserPersistence;
import de.grashof.taskwall.entity.ArchivedTask;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * This bean is used when a user looks at the details of a single finished Task.
 *
 * @author TaskWall-Team
 */
@RequestScoped
@Named("viewOneFinishedTaskBean")
public class ViewOneFinishedTaskBean {

    /**
     * The title of the Task.
     */
    private String title;

    /**
     * The description of the Task.
     */
    private String description;

    /**
     * The username of the creator of the Task.
     */
    private String creator;

    /**
     * The creation date of the Task.
     */
    private Date created;

    /**
     * The date the Task was finished.
     */
    private Date finished;

    /**
     * The deadline of the Task.
     */
    private Date due;

    /**
     * The bean containing the information which bean the user wants to see.
     */
    @Inject
    private LoginBean loginBean;

    /**
     * Used to retrieve the creator's username.
     */
    @EJB
    private UserPersistence userPersistence;

    /**
     * Sets all field values according to the data retrieved from the
     * viewFinishedTasksBean.
     */
    @PostConstruct
    public void init() {
        ArchivedTask current = loginBean.getCurrentFinished();
        title = current.getTitle();
        description = current.getDescription();
        creator = userPersistence.getUser(current.getCreatorId())
                .getUsername();
        created = current.getCreated();
        finished = current.getFinished();
        due = current.getDue();
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
     * Gets the description of the Task.
     *
     * @return the description of the Task
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the username of the creator of the Task.
     *
     * @return the username of the creator of the Task.
     */
    public String getCreator() {
        return creator;
    }

    /**
     * Gets the creation date of the Task.
     *
     * @return the creation date of the Task
     */
    public Date getCreated() {
        return created;
    }

    /**
     * Gets the date the Task was finished.
     *
     * @return the date the Task was finished
     */
    public Date getFinished() {
        return finished;
    }

    /**
     * Gets the deadline of the Task.
     *
     * @return the deadline of the Task
     */
    public Date getDue() {
        return due;
    }
}
