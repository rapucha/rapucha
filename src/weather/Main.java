package weather;

import com.sun.net.httpserver.HttpServer;
import org.json.JSONArray;
import org.json.JSONML;
import org.json.JSONObject;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Morta on 18-May-15.
 */



public class Main {
    static HashMap<String,TestObj> hma;


    public static void main(String[] args) throws Exception {


        hma = new HashMap<>();
        int[] hma2 = new int[100];
        for(int i = 0; i<100; i++){
            int j = new Random().nextInt();
            TestObj to = new TestObj(i,j);
            hma.put(Integer.toString(i),to);
            hma2[i] = j;
        }
        int i = 0;
        long time = System.nanoTime();
        for (String s : hma.keySet()) {
            i +=hma.get(s).getB();
        }
        time=System.nanoTime()-time;
        System.out.println("Time is "+time+" Sum = "+i);
       // System.out.println(jo.get("current_observation"));

        time = System.nanoTime();
        int j = 0;
        for (int i1 = 0; i1<100; i1++) {
            j += hma2[i1];
        }
        time=System.nanoTime()-time;
        System.out.println("Time is "+time+" Sum = "+j);


    }

    static void update(String s, int val){
        TestObj to = hma.get(s);
        to.setA(val);
        to.setB(val);
    }

     static class TestObj{
        private int a,b;

        public TestObj(int a, int b) {
            this.a = a;
            this.b = b;
        }

        public int getA() {
            return a;
        }

        public void setA(int a) {
            this.a = a;
        }

        public int getB() {
            return b;
        }

        public void setB(int b) {
            this.b = b;
        }

        @Override
        public String toString() {
            return "TestObj{" +
                    "a=" + a +
                    ", b=" + b +
                    '}';
        }
    }
}
