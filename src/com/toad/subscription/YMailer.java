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

    Properties props = new Properties();
    private Logger logger = Logger.getLogger(this.getClass().getName());

    void send(String email, String station, int bikes) {
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", email_smtp);
        props.put("mail.smtp.port", "25");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(email_addr, email_pwd);
                    }
                });

        logger.fine("Sending email to " + email);
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email_addr));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(email));
            message.setSubject("Ваш велосипед -)");
            message.setText("На станции "
                    + "\n\n свободных велосипедов: " + bikes);
            long time = System.nanoTime();
            Transport.send(message);
            time = System.nanoTime() - time;
            logger.info("message sent in " + time);
        } catch (MessagingException e) {
            logger.severe("Cannot send email " + e);
            e.printStackTrace();
        }
    }


}
