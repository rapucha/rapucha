package weather;

import com.sun.net.httpserver.HttpServer;
import org.json.JSONArray;
import org.json.JSONML;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Morta on 18-May-15.
 */
public class Main {
    public static void main(String[] args) throws Exception {

        URL weather = new URL("http://api.wunderground.com/weatherstation/WXDailyHistory.asp?ID=I966&format=XML");
        URLConnection uc = weather.openConnection();
        uc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36");
        String inputLine, text = "";
        try(BufferedReader in =new BufferedReader(new InputStreamReader(uc.getInputStream()));) {

            while ((inputLine = in.readLine()) != null) {
                text = text + inputLine;
            }
        }
        JSONArray jarr = JSONML.toJSONArray(text);

        JSONArray last = jarr.getJSONArray(jarr.length() - 1);
        JSONArray pure = new JSONArray();
        pure.put(last.getJSONArray(8));
        pure.put(last.getJSONArray(12));
        pure.put(last.getJSONArray(13));
        pure.put(last.getJSONArray(14));
        pure.put(last.getJSONArray(15));
        pure.put(last.getJSONArray(16));
        pure.put(last.getJSONArray(20));
        pure.put(last.getJSONArray(24));

        System.out.println(last.toString());

    }
}
