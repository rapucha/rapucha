package com.toad.server;

import com.sun.deploy.net.cookie.CookieUnavailableException;
import org.json.Cookie;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Created by toad on 5/27/15.
 */
public class CookieProvider {

    public static final String COOKIE = "Cookie", SET_COOKIE = "Set-Cookie", ID="id";
    private static Logger logger = Logger.getLogger(CookieProvider.class.getName());

    public static final Set<UUID> cookies = Collections.newSetFromMap(new ConcurrentHashMap<UUID, Boolean>());

    private static UUID getUUID() {
        UUID uuid = UUID.randomUUID();
        if (!cookies.add(uuid)) {
            logger.info("UUID collision ~))");
            cookies.add(getUUID());
        }
        return uuid;
    }

    public static String getCookieString(){

        Cookie.toString(new JSONObject().put(ID,getUUID()));
        return  ID+"="+getUUID().toString();

    }


    private static boolean removeUUID(UUID id) {
        return cookies.remove(id);
    }

    private static boolean removeCookie(String c){
      //     UUID id = UUID.fromString(Cookie.toJSONObject(c).get(ID));
        return false;
    }
}
