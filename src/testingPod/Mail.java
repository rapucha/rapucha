package testingPod;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;


public class Mail {

    static ConcurrentLinkedQueue<Integer> list = new ConcurrentLinkedQueue<>();
    static Integer rnd = new Random().nextInt(99999);

    public static void main(String[] args) {

        for (int i = 0; i < 100000; i++) {
            list.add(new Integer(i));
        }
        long time = 0;
        for (int i = 0; i < 100; i++) {
            time = time + testList();

        }
        System.out.println("time==" + time);

    }

    private static long testList() {

        long time = System.currentTimeMillis();
        list.removeIf((number -> number.equals(rnd)));
        time = System.currentTimeMillis() - time;
        return time;
    }

    private static void removeClientListener(Integer clientListener) {
        list.remove(clientListener);
    }


}