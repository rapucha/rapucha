package testingPod;



import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class Mail {

    public enum clientRequestTimes{
        Now("Now"),Soon("Soon"),Later("Later");
        String value;
        clientRequestTimes(String s) {
            value = s;
        }
    }

    public static void main(String[] args) {

    System.out.println(clientRequestTimes.Now+" "+ clientRequestTimes.Now.name());


    }
}