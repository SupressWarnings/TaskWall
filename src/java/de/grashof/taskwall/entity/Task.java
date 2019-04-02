package de.grashof.taskwall.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Represents a Task in the database. Contains all necessary data to assign a
 * task to someone. At runtime the class is instantiated when a new Task is
 * created by a user or an old Task is loaded from the database.
 *
 * @author TaskWall-Team
 */
@Entity
@NamedQueries({
    @NamedQuery(
            name = "getTasks",
            query = "SELECT t FROM Task t WHERE t.receiverId = :receiverId"),
    @NamedQuery(
            name = "deleteTask",
            query = "DELETE FROM Task t WHERE t.id = :id")
})
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * An auto-generated id as the primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * The id-value of the Profile-object of the assigned person.
     */
    @NotNull
    private Long receiverId;

    /**
     * The id-value of the Profile-object of the author of the Task.
     */
    @NotNull
    private Long creatorId;

    /**
     * The title of the Task.
     */
    @NotNull
    private String title;

    /**
     * The description of the Task.
     */
    @NotNull
    private String description;

    /**
     * The Date until the Task has to be finished.
     */
    @NotNull
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date due;

    /**
     * The Date of creation of the Task. Automatically generated when creating
     * the Task.
     */
    @NotNull
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date created;

    /**
     * Standard empty constructor necessary for all Entity-classes.
     */
    public Task() {
    }

    /**
     * Constructor setting all fields. Except for the creation date all values
     * are passed. The creation date is automatically generated as the current
     * date.
     *
     * @param receiverId the id-value of the Profile-object of the assigned
     * person
     * @param creatorId the id-value of the Profile-object of the author
     * @param title the title of the Task
     * @param description the description of the Task
     * @param due the Date until the Task has to be finished
     */
    public Task(Long receiverId, Long creatorId, String title, String description, Date due) {
        this.receiverId = receiverId;
        this.creatorId = creatorId;
        this.title = title;
        this.description = description;
        this.due = due;
        created = new Date();
    }

    /**
     * Gets the receiverId.
     *
     * @return the receiverId
     */
    public Long getReceiverId() {
        return receiverId;
    }

    /**
     * Sets the receiverId.
     *
     * @param receiverId the new receiverId
     */
    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    /**
     * Gets the creatorId.
     *
     * @return the creatorId
     */
    public Long getCreatorId() {
        return creatorId;
    }

    /**
     * Sets the creatorId.
     *
     * @param creatorId the new creatorId
     */
    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    /**
     * Gets the title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title.
     *
     * @param title the new title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     *
     * @param description the new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the deadline.
     *
     * @return the deadline
     */
    public Date getDue() {
        return due;
    }

    /**
     * Sets the deadline.
     *
     * @param due the new deadline
     */
    public void setDue(Date due) {
        this.due = due;
    }

    /**
     * Gets the creation date.
     *
     * @return the creation date
     */
    public Date getCreated() {
        return created;
    }

    /**
     * Sets the creation date.
     *
     * @param created the new creation date
     */
    public void setCreated(Date created) {
        this.created = created;
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
        Task other = (Task) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    /**
     * Returns a string representation of the object.
     *
     * @return a string representation of the object
     */
    @Override
    public String toString() {
        return "de.grashof.taskwall.entity.Task[ id=" + id + " ]";
    }
}
