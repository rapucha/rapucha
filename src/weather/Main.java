package weather;

import com.sun.net.httpserver.HttpServer;
import org.json.JSONArray;
import org.json.JSONML;
import org.json.JSONObject;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Morta on 18-May-15.
 */



public class Main {
    static int port;
    static String  dbuser, dbpass, dbschema, dburl, WeatherAPIkey;



    public static void main(String[] args) throws Exception {

        Properties prop = new Properties();
        try(InputStream input  = new FileInputStream("config.properties");) {
            prop.load(input);
            dbuser = prop.getProperty("dbuser");
            dbpass = prop.getProperty("dbpass");
            dbschema = prop.getProperty("dbschema");
            dburl = prop.getProperty("dburl");
            port = Integer.parseInt(prop.getProperty("serverPort"));
            WeatherAPIkey = prop.getProperty("WeatherAPIkey");
        }
        catch (IOException | NumberFormatException e){
                e.printStackTrace();
        }


        URL weather = new URL("http://api.wunderground.com/api/"+Main.WeatherAPIkey+"/conditions/q/59.935571,30.308397.json");
        URLConnection uc = weather.openConnection();
        String text = "";
        try(BufferedReader in =new BufferedReader(new InputStreamReader(uc.getInputStream()));) {
            String line;
            while ((line = in.readLine()) != null) {
                text = text + line;;
            }
        }
        JSONObject jo = new JSONObject(text);
        //jo.get("weather");

        System.out.println(jo.get("current_observation"));

    }
}
