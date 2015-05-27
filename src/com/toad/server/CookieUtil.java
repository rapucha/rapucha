package com.toad.server;

import org.json.Cookie;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by toad on 5/27/15.
 */
public class CookieUtil {

    public List<Cookie> parseCookieString(String cookies) {
        List<Cookie> cookieList = new ArrayList<Cookie>();
        Pattern cookiePattern = Pattern.compile("([^=]+)=([^\\;]*);?\\s?");
        Matcher matcher = cookiePattern.matcher(cookies);
        while (matcher.find()) {
            int groupCount = matcher.groupCount();
            System.out.println("matched: " + matcher.group(0));
            for (int groupIndex = 0; groupIndex <= groupCount; ++groupIndex) {
                System.out.println("group[" + groupIndex + "]=" + matcher.group(groupIndex));
            }
            String cookieKey = matcher.group(1);
            String cookieValue = matcher.group(2);
            //Cookie cookie = new BasicClientCookie(cookieKey, cookieValue);
            //cookieList.add(cookie);
        }
        return cookieList;
    }
}