package com.toad.server;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Created by toad on 5/27/15.
 */
public class CookieProvider {

    public static final String COOKIE = "Cookie", SET_COOKIE = "Set-Cookie";
    private static Logger logger = Logger.getLogger(CookieProvider.class.getName());

    public static final Set<String> cookies = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());

    public static String getCookie() {
        String id = UUID.randomUUID().toString();
        if (!cookies.add(id)) {
            logger.info("UUID collision ~))");
            cookies.add(getCookie());
        }
        return id.toString();
    }

    public static boolean removeCookie(String c) {
        return cookies.remove(c);
    }

}
