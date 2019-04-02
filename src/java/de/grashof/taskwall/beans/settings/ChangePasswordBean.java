package de.grashof.taskwall.beans.settings;

import de.grashof.taskwall.beans.LoginBean;
import de.grashof.taskwall.beans.persistence.UserPersistence;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Used when a user has to change his password after the next login. Validates
 * his input and stores it in the database.
 *
 * @author TaskWall-Team
 */
@RequestScoped
@Named
public class ChangePasswordBean {

    /**
     * The new password.
     */
    private String newPassword;

    /**
     * A repetition of the new password to minimize typing errors.
     */
    private String newPassword2;

    /**
     * Used to retrieve the user's username.
     */
    @Inject
    private LoginBean loginBean;

    /**
     * Used to alter the user's password.
     */
    @EJB
    private UserPersistence userPersistence;

    /**
     * Alters the password of the user to its new value and navigates forward to
     * the Dashbaord.
     *
     * @return the String navigating to the Dashboard
     */
    public String change() {
        userPersistence.updatePassword(loginBean.getUsername(), newPassword);
        return "dashboard";
    }

    /**
     * Validates whether the new password is different from the old one. This is
     * done for safety reasons.
     *
     * @param context
     * @param toValidate
     * @param value
     */
    public void validateNewPassword(FacesContext context, UIComponent toValidate, Object value) {
        if (((String) value).equals(loginBean.getPassword())) {
            ((UIInput) toValidate).setValid(false);
            FacesMessage message = new FacesMessage("Bitte wählen Sie ein neues Passwort.");
            context.addMessage(toValidate.getClientId(context), message);
        } else {
            newPassword = (String) value;
        }
    }

    /**
     * Validates whether the two new passwords are equal to minimize typing
     * errors.
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
     * @param newPassword the new password
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
     * @param newPassword2 the repetition of the new password
     */
    public void setNewPassword2(String newPassword2) {
        this.newPassword2 = newPassword2;
    }
}
