package de.grashof.taskwall.setup;

import de.grashof.taskwall.entity.MailProperties;
import de.grashof.taskwall.entity.Profile;

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Creates the initial entities of Profile and MailProperties at Startup of the
 * application.
 *
 * @author TaskWall-Team
 * @version 1.0
 */
@Singleton
@Startup
public class SetupBean {

    /**
     * The EntityManager is the connection to the database of the application.
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Creates an admin and an empty E-Mail configuration on startup of the
     * application if they don't exist yet.
     */
    @PostConstruct
    public void initializeDatabase() {
        if (entityManager.createNamedQuery("getUserByUsername").setParameter("username", "admin").getResultList().isEmpty()) {
            Profile admin = new Profile("admin");
            admin.setAdministrator(true);
            entityManager.persist(admin);
        }
        if (entityManager.createNamedQuery("getMailProperties").getResultList().isEmpty()) {
            Properties properties = new Properties();
            properties.put("mail.smtp.host", "");
            properties.put("mail.smtp.user", "");
            properties.put("mail.smtp.port", "");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.enabled", "false");
            properties.put("mail.password", "");
            MailProperties mailProps = new MailProperties(properties);
            entityManager.persist(mailProps);
        }
    }
}
