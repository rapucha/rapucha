package testingPod;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;


public class Mail {
    static List<String> list = new LinkedList<>();
    public static void main(String[] args){

        for (int i = 0; i < 10; i++) {
            list.add(""+i);
        }

        //send("rapucha@mail.ru");
        testList();
    }

    private static void testList() {
        list.stream().forEach(c -> System.out.println(c+">"));
        System.out.println("====");
        list.parallelStream().filter(string -> string.equalsIgnoreCase("5")).forEach(clientListener -> removeClientListener(clientListener));
        //list.parallelStream().filter(string -> string.equalsIgnoreCase("5")).forEach(clientListener -> System.out.println(clientListener));
        list.stream().forEach(c -> System.out.println("<"+c));
    }

    private static void removeClientListener(String clientListener) {
        list.remove(clientListener);

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