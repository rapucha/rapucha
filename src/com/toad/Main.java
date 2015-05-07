package com.toad;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.*;

/*    /\[([^\]]+)]/ */
class Main {

    public static final String PATTERN = "\\[([^\\]]+)];";

    public static void main(String[] args) throws Exception {
        Pattern pattern = Pattern.compile("var stationsData = "+ PATTERN);

        URL velo = new URL("http://spb.velogorod.org/");
        URLConnection uc = velo.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                uc.getInputStream()));
        String inputLine;
        int total=0;
        while ((inputLine = in.readLine()) != null){
            Matcher m = pattern.matcher(inputLine);
            if(m.find()){
                JSONArray jarr = new JSONArray("["+m.group(1)+"];");
                for (int i = 0; i < jarr.length(); i++) {
                    JSONObject jsonobject = jarr.getJSONObject(i);
                    String name = jsonobject.getString("Name");
                    int subTotal = jsonobject.getInt("TotalAvailableBikes");
                    total = total + subTotal;
                    System.out.println(name +" "+subTotal);
                }

            }
        }
        System.out.println(total);
        in.close();

    }
}