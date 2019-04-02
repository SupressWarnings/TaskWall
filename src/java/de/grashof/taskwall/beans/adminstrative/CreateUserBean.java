package de.grashof.taskwall.beans.adminstrative;

import de.grashof.taskwall.beans.LoginBean;
import de.grashof.taskwall.beans.persistence.UserPersistence;
import de.grashof.taskwall.beans.task.CreateTaskBean;
import de.grashof.taskwall.entity.Profile;

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
 * This bean is used when an administrator creates a new user. It validates that
 * the user does not exist yet and permanently stores the new user in the
 * database.
 *
 * @author TaskWall-Team
 */
@RequestScoped
@Named
public class CreateUserBean {

    /**
     * The username of the new user.
     */
    private String username;

    /**
     * A boolean indicating whether the new user has administrative rights or
     * not.
     */
    private boolean isAdmin = false;

    /**
     * Used to check whether the current user is allowed to create new users.
     */
    @Inject
    private LoginBean loginBean;

    /**
     * Used to retrieve data about the current user and to store the new user
     * permanently.
     */
    @EJB
    private UserPersistence userPersistence;

    /**
     * Checks whether the current user has the right to create new users and, if
     * not, reconnects to the login page/dashboard.
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
        }
    }

    /**
     * Validates the new username. If a user with the username exists already,
     * there is an error message displayed.
     *
     * @param context
     * @param toValidate
     * @param value
     */
    public void validate(FacesContext context, UIComponent toValidate, Object value) {
        if (userPersistence.userExists((String) value)) {
            ((UIInput) toValidate).setValid(false);
            FacesMessage msg = new FacesMessage("Benutzer existiert bereits.");
            context.addMessage(toValidate.getClientId(context), msg);
        }
    }

    /**
     * Creates the new user, stores him in the database and navigates back to
     * the administrative dashboard.
     *
     * @return the outcome navigating the administrator back
     */
    public String create() {
        Profile user = new Profile(username);
        user.setAdministrator(isAdmin);
        userPersistence.createUser(user);
        return "return";
    }

    /**
     * Gets the username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     *
     * @param username the new username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets whether the new user is an administrator.
     *
     * @return true if the new user is an administrator; false if not
     */
    public boolean getIsAdmin() {
        return isAdmin;
    }

    /**
     * Sets whether the new user is an administrator or not.
     *
     * @param isAdmin true if the new user is an administrator; false if not
     */
    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}
