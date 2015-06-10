package com.toad.subscription;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;
import java.util.logging.Logger;

import static com.toad.SettingsManager.*;

/**
 * Created by Seva Nechaev "Rapucha" on 5/26/15. All rights reserved ;)
 */
public enum YMailer {

    INSTANCE;

    private static final Logger logger = Logger.getLogger(YMailer.class.getName());
    private final Properties props = new Properties();


    YMailer() {
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", email_smtp);
        props.put("mail.smtp.port", "25");

    }

    public static void sendGenericMessage(String toWhom, String from, String subject, String body) throws MessagingException {

        Session session = Session.getInstance(INSTANCE.props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(email_uname, email_pwd);
                    }
                });

        logger.fine("Sending email to " + toWhom + " from " + from);
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(toWhom));
        message.setSubject(subject, "UTF-8");
        message.setText(body, "UTF-8");
        Transport.send(message);

    }


    public static void sendClientNotification(String email, List<SimpleStation> stations, int desiredNumber) {

        String from = "info@bikes.rapucha.ru";
        String subj = (desiredNumber == 1) ? "Ваш велосипед -)" : "Ваши велосипеды -)";
        StringBuilder sb;
        DateTimeFormatter hourFormatter = //DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
                DateTimeFormatter.ofPattern("HH:mm", new Locale("ru")).withZone(TimeZone.getTimeZone("Europe/Moscow").toZoneId());

        DateTimeFormatter dayFormatter = //DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
                DateTimeFormatter.ofPattern("d MMMM", new Locale("ru")).withZone(TimeZone.getTimeZone("Europe/Moscow").toZoneId());

        TemporalAccessor now = Instant.now();
        String nowHour = hourFormatter.format(now);
        String nowDay = hourFormatter.format(now);

        if (stations.size() == 1) {
            String name = stations.get(0).name;
            int bikes = stations.get(0).bikes;
            sb = new StringBuilder("На вашей станции №");
            if (name.substring(0, 1).intern() == "0") {

                sb.append(name.substring(1, 2));
            } else {
                sb.append(name.substring(0, 2));
            }
            sb.append("\"");
            sb.append(name.substring(4));
            sb.append("\" ");
            if (bikes == 1) {
                sb.append("\nпоявился велосипед!");
            } else {
                sb.append("\nпоявились велосипеды.");
            }

        } else {
            sb = new StringBuilder("Есть велосипеды на ваших станциях!");
            stations.stream().filter(station -> station.bikes >= 1)
                    .forEach(station -> {
                        sb.append("\n  №");

                        if (station.name.substring(0, 1).intern() =="0") {

                            sb.append(station.name.substring(1, 2));
                        } else {
                            sb.append(station.name.substring(0, 2));
                        }
                        sb.append(" \"");
                        sb.append(station.name.substring(4));
                        sb.append("\"");
                        sb.append(": ");
                        sb.append(station.bikes);
                        sb.append("шт.");

                    });

            if (stations.stream().filter(station -> station.bikes == 0).count() > 0) {
                sb.append("\n\nА тут пусто:");
                stations.stream().filter(station -> station.bikes == 0)
                        .forEach(station -> {
                            sb.append("\n  №");
                            if (station.name.substring(0, 1).intern() == "0") {
                                sb.append(station.name.substring(1, 2));
                            } else {
                                sb.append(station.name.substring(0, 2));
                            }
                            sb.append(" \"");
                            sb.append(station.name.substring(4));
                            sb.append("\"");
                        });
            }

        }
        sb.append("\nЭто было в ");
        sb.append(nowHour);
        sb.append(", ");
        sb.append(nowDay);
        sb.append(".");
        sb.append("Если вы получили это письмо по ошибке, просто проигнорируйте его -- кто-то случайно ввел вашу почту, когда искал свободный велосипед в городском велопрокате. Если  вы никогда не хотите получать от меня письмо -- дайте мне знать на rapucha@yandex.ru");
        String body = sb.toString();
        try {
            sendGenericMessage(email, from, subj, body);
        } catch (MessagingException e) {
            logger.severe("Cannot send mail: " + e);
            e.printStackTrace();
        }
    }

}
