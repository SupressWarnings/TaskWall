package de.grashof.taskwall.beans.adminstrative;

import de.grashof.taskwall.beans.LoginBean;
import de.grashof.taskwall.beans.persistence.MailPropertiesPersistence;
import de.grashof.taskwall.beans.persistence.UserPersistence;
import de.grashof.taskwall.beans.task.CreateTaskBean;
import de.grashof.taskwall.entity.MailProperties;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * This bean is used when an administrator wants to change the SMTP-settings for
 * the application.
 *
 * @author TaskWall-Team
 */
@ViewScoped
@Named("configureMailBean")
public class ConfigureMailBean implements Serializable {

    /**
     * Indicates whether the SMTP-service is enabled or not.
     */
    private boolean enabled;

    /**
     * The host adress of the SMTP-server.
     */
    private String host;

    /**
     * The username used on the SMTP-server.
     */
    private String user;

    /**
     * The password used on the SMTP-server.
     */
    private String password;

    /**
     * The port on the SMTP-server used to enter.
     */
    private String port;

    /**
     * The object containing all configuration information.
     */
    private MailProperties mailProperties;

    /**
     * Used to get the username of the current user.
     */
    @Inject
    private LoginBean loginBean;

    /**
     * Used to check whether the current user is allowed to configure the
     * SMTP-service.
     */
    @EJB
    private UserPersistence userPersistence;

    /**
     * Used to load and store the SMTP-settings in the database.
     */
    @EJB
    private MailPropertiesPersistence mailPropertiesPersistence;

    /**
     * Checks whether the current user is allowed to alter the SMTP-settings and
     * loads all saved configuration data from the database.
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
            mailProperties = mailPropertiesPersistence.getMailProperties();
            enabled = Boolean.parseBoolean(mailProperties.getProperties().getProperty("mail.enabled"));
            host = mailProperties.getProperties().getProperty("mail.smtp.host");
            user = mailProperties.getProperties().getProperty("mail.smtp.user");
            password = mailProperties.getProperties().getProperty("mail.password");
            port = mailProperties.getProperties().getProperty("mail.smtp.port");
        }
    }

    /**
     * Checks whether settings have changed through user input, and if so, saves
     * the changes to the database.
     */
    public void save() {
        boolean changed = false;
        if (!mailProperties.getProperties().getProperty("mail.enabled").equals(enabled)) {
            mailProperties.getProperties().setProperty("mail.enabled", "" + enabled);
            changed = true;
        }
        if (!mailProperties.getProperties().getProperty("mail.smtp.host").equals(host)) {
            mailProperties.getProperties().setProperty("mail.smtp.host", host);
            changed = true;
        }
        if (!mailProperties.getProperties().getProperty("mail.smtp.user").equals(user)) {
            mailProperties.getProperties().setProperty("mail.smtp.user", user);
            changed = true;
        }
        if (!mailProperties.getProperties().getProperty("mail.password").equals(password)) {
            mailProperties.getProperties().setProperty("mail.password", password);
            changed = true;
        }
        if (!mailProperties.getProperties().getProperty("mail.smtp.port").equals(port)) {
            mailProperties.getProperties().setProperty("mail.smtp.port", port);
            changed = true;
        }
        if (changed) {
            mailPropertiesPersistence.setMailProperties(mailProperties);
        }
    }

    /**
     * Gets whether the SMTP-service is enabled.
     *
     * @return true is the service is enabled; false if not
     */
    public boolean getEnabled() {
        return enabled;
    }

    /**
     * Sets whether SMTP-service is enabled.
     *
     * @param enabled true if the service is enabled; false if it is not
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Gets the host.
     *
     * @return the host adress
     */
    public String getHost() {
        return host;
    }

    /**
     * Sets the host.
     *
     * @param host the new host
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Gets the username
     *
     * @return the username
     */
    public String getUser() {
        return user;
    }

    /**
     * Sets the username.
     *
     * @param user the new username
     */
    public void setUser(String user) {
        this.user = user;
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
     * Gets the port.
     *
     * @return the port
     */
    public String getPort() {
        return port;
    }

    /**
     * Sets the port.
     *
     * @param port the new port
     */
    public void setPort(String port) {
        this.port = port;
    }
}
