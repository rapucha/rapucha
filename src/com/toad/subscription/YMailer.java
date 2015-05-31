package com.toad.subscription;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.logging.Logger;

import static com.toad.SettingsManager.*;

/**
 * Created by toad on 5/26/15.
 */
public class YMailer {

    private final Properties props = new Properties();
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public void send(String email, String[] station, int bikes) {
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", email_smtp);
        props.put("mail.smtp.port", "25");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(email_uname, email_pwd);
                    }
                });

        logger.fine("Sending email to " + email);
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email_uname));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(email));
            message.setSubject("Ваш велосипед -)");
            message.setText("На станции " + station
                    + "\n\n свободных велосипедов: " + bikes);
            long time = System.currentTimeMillis();
            Transport.send(message);
            time = System.currentTimeMillis() - time;
            logger.info("message sent in " + time);
        } catch (MessagingException e) {
            logger.severe("Cannot send email " + e);
            e.printStackTrace();
        }
    }


}
