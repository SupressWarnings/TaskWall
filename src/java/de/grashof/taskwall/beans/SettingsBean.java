package de.grashof.taskwall.beans;

import de.grashof.taskwall.beans.persistence.UserPersistence;
import de.grashof.taskwall.beans.task.CreateTaskBean;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Used when a user edits his personal settings. Displays his email and password
 * settings.
 *
 * @author TaskWall-Team
 */
@RequestScoped
@Named("settingsBean")
public class SettingsBean {

    /**
     * Indicates whether the user has enabled mail notifications.
     */
    private boolean emailEnabled;

    /**
     * The email adress of the user. Needs to be set only when notifications are
     * enabled.
     */
    private String email;

    /**
     * The old password of the user. Needs to be entered only when the user
     * wants to change his password.
     */
    private String oldPassword;

    /**
     * The new password of the user. Needs to be entered only when the user
     * wants to change his password.
     */
    private String newPassword;

    /**
     * A repetition of the new password. Needs to be entered only when the user
     * wants to change his password.
     */
    private String newPassword2;

    /**
     * Used to retrieve the user's username to get his current settings.
     */
    @Inject
    private LoginBean loginBean;

    /**
     * Used to retrieve and alter the user's settings in the database.
     */
    @EJB
    private UserPersistence userPersistence;

    /**
     * Checks whether there is a logged in user and loads his settings if there
     * is one.
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
            emailEnabled = userPersistence.getUser(loginBean.getUsername()).getNotificationsEnabled();
            email = userPersistence.getUser(loginBean.getUsername()).getEmail();
        }
    }

    /**
     * Saves the changed email settings of the user. Does not true for
     * notifications enabled if the email is null.
     */
    public void saveEmail() {
        if (emailEnabled != userPersistence.getUser(loginBean.getUsername()).getNotificationsEnabled() && email != null) {
            userPersistence.updateEmailEnabled(loginBean.getUsername(), emailEnabled);
        }
        if (email != null && !email.equals(userPersistence.getUser(loginBean.getUsername()).getEmail())) {
            userPersistence.updateEmail(loginBean.getUsername(), email);
        }
    }

    /**
     * Validates the email adress. If email notifications are enabled and there
     * is no email adress set, an error message is displayed.
     *
     * @param context
     * @param toValidate
     * @param value
     */
    public void validateEmail(FacesContext context, UIComponent toValidate, Object value) {
        if (emailEnabled && value == null) {
            ((UIInput) toValidate).setValid(false);
            FacesMessage message = new FacesMessage("Bitte geben Sie Ihre E-Mail-Adresse an.");
            context.addMessage(toValidate.getClientId(context), message);
        }
    }

    /**
     * Saves the new password of the user.
     */
    public void savePassword() {
        if (newPassword != null) {
            userPersistence.updatePassword(loginBean.getUsername(), newPassword);
        }
    }

    /**
     * Validates whether the old password of the user is correct.
     *
     * @param context
     * @param toValidate
     * @param value
     */
    public void validateOldPassword(FacesContext context, UIComponent toValidate, Object value) {
        if (!userPersistence.getUser(loginBean.getUsername()).getPassword().equals((String) value)) {
            ((UIInput) toValidate).setValid(false);
            FacesMessage message = new FacesMessage("Altes Passwort falsch.");
            context.addMessage(toValidate.getClientId(context), message);
        } else {
            oldPassword = (String) value;
        }
    }

    /**
     * Checks whether the new password equals the old password and displays an
     * error message if it does.
     *
     * @param context
     * @param toValidate
     * @param value
     */
    public void validateNewPassword(FacesContext context, UIComponent toValidate, Object value) {
        if (((String) value).equals(oldPassword)) {
            ((UIInput) toValidate).setValid(false);
            FacesMessage message = new FacesMessage("Bitte wählen Sie ein neues Passwort.");
            context.addMessage(toValidate.getClientId(context), message);
        } else {
            newPassword = (String) value;
        }
    }

    /**
     * Validates whether the two new passwords are equal and displays an error
     * message if the do not.
     *
     * @param context
     * @param toValidate
     * @param value
     */
    public void validateNewPassword2(FacesContext context, UIComponent toValidate, Object value) {
        if (!((String) value).equals(newPassword)) {
            ((UIInput) toValidate).setValid(false);
            FacesMessage message = new FacesMessage("Die Passwörter stimmen nicht überein.");
            context.addMessage(toValidate.getClientId(context), message);
        }
    }

    /**
     * Gets whether email notifications are enabled.
     *
     * @return true if notifications are enabled; false if not
     */
    public boolean getEmailEnabled() {
        return emailEnabled;
    }

    /**
     * Sets whether email notifications are enabled.
     *
     * @param emailEnabled true if notifications are enabled; false if not
     */
    public void setEmailEnabled(boolean emailEnabled) {
        this.emailEnabled = emailEnabled;
    }

    /**
     * Gets the email adress.
     *
     * @return the email adress
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email adress.
     *
     * @param email the new email adress
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the old password.
     *
     * @return the old password
     */
    public String getOldPassword() {
        return oldPassword;
    }

    /**
     * Sets the old password.
     *
     * @param oldPassword the new value for the old password
     */
    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    /**
     * Gets the new password.
     *
     * @return the new password
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * Sets the new password.
     *
     * @param newPassword the new value for the new password.
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    /**
     * Gets the repetition of the new password.
     *
     * @return the repetition of the new password
     */
    public String getNewPassword2() {
        return newPassword2;
    }

    /**
     * Sets the repetition of the new password.
     *
     * @param newPassword2 the new value for the repetition of the new password
     */
    public void setNewPassword2(String newPassword2) {
        this.newPassword2 = newPassword2;
    }
}
