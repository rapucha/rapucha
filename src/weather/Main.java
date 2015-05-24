package weather;

import com.sun.net.httpserver.HttpServer;
import org.json.JSONArray;
import org.json.JSONML;
import org.json.JSONObject;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.time.Instant;
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

        System.out.println(Instant.now().toString());

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
