package com.toad;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by Seva Nechaev "Rapucha" on 21-May-15. All rights reserved ;)
 */
public class Util {
    private static final Logger logger = Logger.getLogger(Util.class.getName());

    public static int safeInt(JSONObject jo, String s) {
        int safe = Short.MAX_VALUE;
        try {
            safe = jo.getInt(s);
        } catch (JSONException j) {
            try {
                logger.info(j.getMessage() + " : " + jo.get(s));
            } catch (JSONException j2) {
                logger.info(j.getMessage());
            }
        }

        return safe;
    }

    public static String safeString(JSONObject jo, String s) {
        String r = "ERROR";
        try {
            r = jo.getString(s);
        } catch (JSONException j) {
            try {
                logger.info(j.getMessage() + " : " + jo.get(s));
            } catch (JSONException j2) {
                logger.info(j.getMessage());
            }
        }

        return r;
    }

    public static double safeDouble(JSONObject jo, String s) {
        double safe = 0.;
        try {
            safe = jo.getDouble(s);
        } catch (JSONException j) {
            try {
                logger.info(j.getMessage() + " : " + jo.get(s));
            } catch (JSONException j2) {
                logger.info(j.getMessage());
            }
        }

        return safe;
    }

    public static String isToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";

    }

    static void printFile(File f, String s) {

        try (PrintStream ps = new PrintStream(new FileOutputStream(f, true), true, "UTF-8")) {
            ps.append(s);
            ps.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, List<String>> parse(InputStream is) {
        Map<String, List<String>> map = new HashMap<>();
        Scanner s = new Scanner(is);
        while (s.hasNext()) {
            String[] kv = s.next().split("=");

            List<String> l = map.get(kv[0]);
            if (l == null)
                map.put(kv[0], l = new ArrayList<>());
            l.add(kv[1]);
        }

        return map;
    }

}
