package com.toad.subscription;

import com.toad.crawlers.StationSnapshot;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import static com.toad.SettingsManager.*;

/**
 * Created by toad on 5/26/15.
 */
public class YMailer {

    private final Properties props = new Properties();
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public void send(String email, List<StationSnapshot> stations, int desiredNumber) {
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
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("info@bikes.rapucha.ru"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(email));
            if(desiredNumber>1){
                message.setSubject("Ваши велосипеды -)","UTF-8");
            } else {
                message.setSubject("Ваш велосипед -)","UTF-8");
            }

            StringBuilder sb = new StringBuilder("Велосипед(ы) на ваших станциях: \n");
            stations.stream().filter(station -> station.getBikes() >= 1)
                    .forEach(station -> {
                        sb.append(station.getName().substring(4));
                        sb.append(": ");
                        sb.append(station.getBikes());
                        sb.append("\n");
                    });

            sb.append("\nНа этих станциях пусто: \n");
            stations.stream().filter(station -> station.getBikes() == 0)
                    .forEach(station -> {
                        sb.append(station.getName().substring(4));
                        sb.append("\n");
                    });

            message.setText(sb.toString(),"UTF-8");
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
