package com.toad.server;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Created by toad on 5/27/15.
 */
public enum CookieProvider {
    INSTANCE;

    public static final String COOKIE = "Cookie", SET_COOKIE = "Set-Cookie", ID = "id";
    private static Logger logger = Logger.getLogger(CookieProvider.class.getName());

    public static final ConcurrentHashMap<UUID, Instant> cookiesMap = new ConcurrentHashMap<>();


    ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

     public void init() {

        service.scheduleWithFixedDelay(() -> {

            logger.info("Cookies map cleaned up");
            for (Map.Entry<UUID, Instant> cookie : cookiesMap.entrySet()) {
                if (cookie.getValue().until(Instant.now(), ChronoUnit.MINUTES) > 15) {
                    cookiesMap.remove(cookie.getKey());
                }
            }

        }, 0, 15, TimeUnit.MINUTES);

    }

    // I am not sure if broken synch. will harm anything here.
    // If cookie lifespan is long enough, say 5 sec, it makes no difference which thread put it into map
    private static UUID getUUID() {
        UUID uuid = UUID.randomUUID();// btw is this thread-safe?
        cookiesMap.put(uuid, Instant.now());
        return uuid;
    }

    public static String getCookieString() {
        StringBuffer sb = new StringBuffer(ID);
        sb.append("=");
        sb.append(getUUID());
        return sb.toString();

    }

    /*
     * this code may be a bottleneck if synchronized,
     * so maybe we better send here just uuid's and check them in a map
     */
    public static String cookieIsGood(String c) {
        StringBuilder sb = new StringBuilder(c);
        sb = sb.delete(0, 3);//id=
        UUID id = UUID.fromString(sb.toString());//TODO this is probably not thread safe
        Instant when = cookiesMap.remove(id);
        if (null == when) {
            return "";
        }

        if (when.until(Instant.now(), ChronoUnit.SECONDS) < 1) {
            return "too fast ";
        }
        return "ok";
    }

}
