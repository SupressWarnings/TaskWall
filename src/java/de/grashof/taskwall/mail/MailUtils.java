package de.grashof.taskwall.mail;

import de.grashof.taskwall.beans.persistence.MailPropertiesPersistence;
import de.grashof.taskwall.beans.persistence.UserPersistence;
import de.grashof.taskwall.entity.Profile;
import de.grashof.taskwall.entity.Task;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * Provides methods for the SMTP-service.
 *
 * @author TaskWall-Team
 */
@Stateless
public class MailUtils {

    /**
     * The Mail-properties used to determine the SMTP-settings.
     */
    private Properties properties;

    /**
     * The UserPersistence used to get the username of the creator of a task.
     */
    @EJB
    private UserPersistence userPersistence;

    /**
     * Used to get the Properties containing the SMTP-settings.
     */
    @EJB
    private MailPropertiesPersistence mailPropertiesPersistence;

    /**
     * The Mail-session.
     */
    private Session session;

    /**
     * Creates a new session. A new session is created even if an old one exists
     * so that a change in Properties of the SMTP-service is factored in as fast
     * as possible.
     */
    public void createSession() {
        properties = mailPropertiesPersistence.getMailProperties().getProperties();
        session = Session.getInstance(properties, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(properties.getProperty("mail.smtp.user"),
                        properties.getProperty("mail.password"));
            }
        });
    }

    /**
     * Sends an E-Mail to the recipient of the task if he enabled notifications.
     *
     * @param task the task containing the details used for the E-Mail
     */
    public void sendMail(Task task) {
        createSession();
        if (Boolean.parseBoolean(properties.getProperty("mail.enabled"))) {
            try {
                Message msg = new MimeMessage(session);
                Profile receiver = userPersistence.getUser(task.getReceiverId());
                if (receiver.getNotificationsEnabled()) {
                    InternetAddress addressTo = new InternetAddress(receiver.getEmail());
                    msg.setRecipient(Message.RecipientType.TO, addressTo);

                    InternetAddress addressFrom = new InternetAddress(properties.getProperty("mail.smtp.user"), "TaskWall");
                    msg.setFrom(addressFrom);

                    msg.setSubject("Neue Aufgabe bei TaskWall");
                    String message = "Sie haben eine neue Aufgabe von ";
                    message += userPersistence.getUser(task.getCreatorId()).getUsername();
                    message += " erhalten.\n\n";
                    message += "Titel: " + task.getTitle();
                    message += "\nFrist: " + new SimpleDateFormat("dd.MM.yyyy").format(task.getDue());
                    msg.setContent(message, "text/plain");
                    Transport.send(msg);
                }
            } catch (MessagingException | UnsupportedEncodingException ex) {
                Logger.getLogger(MailUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Sets the session.
     *
     * @param session the new session object
     */
    public void setSession(Session session) {
        this.session = session;
    }
}
