package de.grashof.taskwall.beans.task;

import de.grashof.taskwall.beans.LoginBean;
import de.grashof.taskwall.beans.persistence.TaskPersistence;
import de.grashof.taskwall.beans.persistence.UserPersistence;
import de.grashof.taskwall.entity.Task;
import de.grashof.taskwall.mail.MailUtils;

import java.util.Date;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * This bean is used when a user creates a new task. It validates his inputs,
 * sends an email to the recipient and stores the new Task in the database.
 *
 * @author TaskWall-Team
 */
@RequestScoped
@Named("createTaskBean")
public class CreateTaskBean {

    /**
     * The title of the new Task.
     */
    private String title;

    /**
     * The description of the new Task.
     */
    private String description;

    /**
     * The recipient's username or email adress.
     */
    private String recipient;

    /**
     * The deadline of the new Task. Initialized with the current date.
     */
    private Date due = new Date();

    /**
     * Used to get the creator's username.
     */
    @Inject
    private LoginBean loginBean;

    /**
     * Used to check whether the recipient exists and to retrieve his and the
     * creator's id.
     */
    @EJB
    private UserPersistence userPersistence;

    /**
     * Used to store the new Task.
     */
    @EJB
    private TaskPersistence service;

    /**
     * Used to send an email to the recipient.
     */
    @EJB
    private MailUtils mailUtils;

    /**
     * Creates the new Task, stores it in the database, sends an email to the
     * recipient and redirects back to the dashboard.
     *
     * @return the navigation outcome redirecting to the dashboard
     */
    public String create() {
        long creatorId = userPersistence.getUser(loginBean.getUsername()).getId();
        long recipientId;
        if (userPersistence.userExists(recipient)) {
            recipientId = userPersistence.getUser(recipient).getId();
        } else {
            recipientId = userPersistence.getUserByEmail(recipient).getId();
        }
        Task newTask = new Task(recipientId, creatorId, title, description, due);
        service.storeTask(newTask);
        if (!recipient.equals(loginBean.getUsername())) {
            mailUtils.sendMail(newTask);
        }
        return "return";
    }

    /**
     * Validates whether the recipient of the Task exists and displays an error
     * message if not.
     *
     * @param context
     * @param toValidate
     * @param value
     */
    public void validateRecipient(FacesContext context, UIComponent toValidate, Object value) {
        if (!userPersistence.userExists((String) value) && !userPersistence.userExistsEmail((String) value)) {
            ((UIInput) toValidate).setValid(false);
            FacesMessage message = new FacesMessage("Benutzer existiert nicht.");
            context.addMessage(toValidate.getClientId(context), message);
        }
    }

    /**
     * Gets the title of the new Task.
     *
     * @return the title of the new Task
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the new Task.
     *
     * @param title the title of the new Task
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the description of the new Task.
     *
     * @return the description of the new Task.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the new Task.
     *
     * @param description the description of the new Task
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the recipient of the new Task.
     *
     * @return the recipient of the new Task
     */
    public String getRecipient() {
        return recipient;
    }

    /**
     * Sets the recipient of the new Task.
     *
     * @param recipient the recipient of the new Task
     */
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    /**
     * Gets the deadline of the new Task.
     *
     * @return the deadline of the new Task
     */
    public Date getDue() {
        return due;
    }

    /**
     * Sets the deadline of the new Task.
     *
     * @param due the deadline of the new Task
     */
    public void setDue(Date due) {
        this.due = due;
    }
}
