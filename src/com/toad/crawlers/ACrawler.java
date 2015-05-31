package com.toad.crawlers;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Observable;
import java.util.Random;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Morta on 18-May-15.
 */
public abstract class ACrawler extends Observable {


    private static final String[] userAgents = {
            "[Mozilla/5.0 (Mobile; Windows Phone 8.1; Android 4.0; ARM; Trident/7.0; Touch; rv:11.0; IEMobile/11.0; NOKIA; Lumia 820) like iPhone OS 7_0_3 Mac OS X AppleWebKit/537 (KHTML, like Gecko) Mobile Safari/537]",
            "[Mozilla/5.0 (iPad; CPU OS 7_0_4 like Mac OS X) AppleWebKit/537.51.1 (KHTML, like Gecko) CriOS/42.0.2311.47 Mobile/11B554a Safari/9537.53]",
            "[Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2227.1 Safari/537.36]",
            "[Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.11 (KHTML, like Gecko) Ubuntu Chromium/23.0.1271.97 Chrome/23.0.1271.97 Safari/537.11]",
            "[Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.143 Safari/537.36]",
            "[Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.143 Safari/537.36]",
            "[Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; WOW64; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; MDDC)]",
            "[Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.65 Safari/537.36]",
            "[Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.11 (KHTML, like Gecko) Ubuntu Chromium/23.0.1271.97 Chrome/23.0.1271.97 Safari/537.11]",
            "[Opera/9.80 (Windows NT 6.1; WOW64; MRA 8.1 (build 6347)) Presto/2.12.388 Version/12.16]",
            "[Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0)]",
            "[Mozilla/5.0 (Windows; U; Windows NT 5.1; en; rv:1.9.0.13) Gecko/2009073022 Firefox/3.5.2 (.NET CLR 3.5.30729) SurveyBot/2.3 (DomainTools)]",
            "[Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.152 Safari/537.36]",
            "[Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0)]",
            "[Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.125 Safari/537.36]"};

    private int delay;
    private final boolean beRandom;
    private final Random rnd = new Random();
    private URL url;
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture scheduledFuture;

    /**
     * @param time     minutes between crawling actions
     * @param address  where to go
     * @param beRandom add some jitter to requests ;)
     * @throws MalformedURLException
     */
    ACrawler(int time, String address, boolean beRandom) {
        delay = time;
        this.beRandom = beRandom;
        try {
            url = new URL(address);
        } catch (MalformedURLException e) {
            logger.log(Level.SEVERE, "Error creating URL for " + address
            );
            e.printStackTrace();
        }
    }

    /**
     *
     * @param i seconds
     */
    public void setUpdateTime(int i) {
        scheduledFuture.cancel(true);
        logger.fine("update time set to " + i);
        delay = i;
        start();
    }


    private void crawl() throws IOException {
        URLConnection uc = url.openConnection();
        if (beRandom) {
            uc.setRequestProperty("User-Agent", getUserAgent());
        }

        try (InputStream is = uc.getInputStream()) {
            processInput(is);
        }

    }

    protected abstract void processInput(InputStream is);

    protected abstract void reportProblem(Exception e);

    public void start() {

        scheduledFuture = service.schedule(() -> {
            try {
                crawl();
                start();

            } catch (Exception e) {
                reportProblem(e);
                e.printStackTrace();
            }
        }, delay+getJitter(), TimeUnit.SECONDS);

    }

    String getUserAgent() {
        String ua = userAgents[rnd.nextInt(userAgents.length - 1)];
        logger.fine("Setting User-Agent " + ua);
        return ua;
    }


    public int getJitter() {
        int jitter =0;
        if(beRandom){
           jitter = rnd.nextInt(delay/2);
           logger.fine("jitter set to " + jitter);
        }
        return jitter;
    }
}
