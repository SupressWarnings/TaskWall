package de.grashof.taskwall.entity;

import java.io.Serializable;
import java.util.Properties;
import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;

/**
 * This Entity saves the application-wide mail settings in a Properties object.
 * There should only be one of these saved at all times.
 *
 * @author TaskWall-Team
 */
@Entity
@NamedQueries({
    @NamedQuery(
            name = "getMailProperties",
            query = "SELECT mailSettings FROM MailProperties mailSettings"),
    @NamedQuery(
            name = "updateProperties",
            query = "UPDATE MailProperties mailSettings SET mailSettings.properties = :properties")

})
public class MailProperties implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * An auto-generated id as the primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * The Properties containing the mail settings.
     */
    @NotNull
    @Column(length = 10000)
    private Properties properties;

    /**
     * Standard constructor needed for Entities.
     */
    public MailProperties() {
    }

    /**
     * Constructor setting the Properties to the passed Properties.
     *
     * @param properties the Properties containing the mail settings.
     */
    public MailProperties(Properties properties) {
        this.properties = properties;
    }

    /**
     * Gets the Properties.
     *
     * @return the Properties containing the mail settings.
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * Sets the Properties.
     *
     * @param properties the new Properties containing the mail settings.
     */
    public void setProperties(Properties properties) {
        this.properties = properties;
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
        if (!(object instanceof MailProperties)) {
            return false;
        }
        MailProperties other = (MailProperties) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    /**
     * Returns a string representation of the object.
     *
     * @return a string representation of the object
     */
    @Override
    public String toString() {
        return "de.grashof.taskwall.entity.MailProperties[ id=" + id + " ]";
    }
}
