package testingPod;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;


public class Mail {

    public static void main(String[] args){
        send("rapucha@mail.ru");
    }

    private static final Properties props = new Properties();
    public static void send(String email) {
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.mandrillapp.com");
        props.put("mail.smtp.port", "25");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("vsevolod.nechaev@gmail.com", "Gi0qsvN4xPEq9n1Z9YpmLQ");
                    }
                });


        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("vsevolod.nechaev@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(email));
            message.setSubject("Ваш велосипед -)");
            message.setText("На ваших станциях "
                    + "\n\n свободных велосипедов: " + 44);
            //iterate stations:
            long time = System.currentTimeMillis();
            Transport.send(message);
            time = System.currentTimeMillis() - time;

        } catch (MessagingException e) {

            e.printStackTrace();
        }
    }
}