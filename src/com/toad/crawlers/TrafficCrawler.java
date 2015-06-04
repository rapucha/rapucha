package com.toad.crawlers;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Logger;

import static com.toad.SettingsManager.traffic_url;

/**
 * Created by Seva Nechaev "Rapucha" on 21-May-15. All rights reserved ;)
 */
public class TrafficCrawler extends ACrawler {


    private static final int REPEAT_SECONDS = 13 * 60;

    public static final ACrawler INSTANCE = new TrafficCrawler(REPEAT_SECONDS, traffic_url);

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private TrafficCrawler(int time, String traffic_url) {
        super(time, traffic_url, true);
    }

    @Override
    protected void processInput(InputStream is) {

        BufferedImage imageNoTraff, imageTraff;
        try {
            imageNoTraff = ImageIO.read(is);
        } catch (IOException ioe) {
            logger.severe("Cannot read NoTraff image mainForm stream " + ioe);
            ioe.printStackTrace();
            return;
        }

        try {
            URLConnection uc = new URL(traffic_url + ",trf").openConnection();
            uc.setRequestProperty("User-Agent", getUserAgent());
            try (InputStream is2 = uc.getInputStream()) {
                imageTraff = ImageIO.read(is2);
            }

        } catch (IOException e) {
            logger.severe("Cannot read Traff image mainForm stream " + e);
            e.printStackTrace();
            return;
        }

        BufferedImage delta = new BufferedImage(imageTraff.getWidth(), imageTraff.getHeight(), imageTraff.getType());

        for (int x = 0; x < imageTraff.getWidth(); x++)
            for (int y = 0; y < imageTraff.getHeight(); y++) {


                int argb0 = imageNoTraff.getRGB(x, y);
                int argb1 = imageTraff.getRGB(x, y);

                int a0 = (argb0 >> 24) & 0xFF;
                int r0 = (argb0 >> 16) & 0xFF;
                int g0 = (argb0 >> 8) & 0xFF;
                int b0 = (argb0) & 0xFF;

                int a1 = (argb1 >> 24) & 0xFF;
                int r1 = (argb1 >> 16) & 0xFF;
                int g1 = (argb1 >> 8) & 0xFF;
                int b1 = (argb1) & 0xFF;

                int aDiff = 255 - Math.abs(a1 - a0);
                int rDiff = 255 - Math.abs(r1 - r0);
                int gDiff = 255 - Math.abs(g1 - g0);
                int bDiff = 255 - Math.abs(b1 - b0);

                int diff =
                        (aDiff << 24) | (rDiff << 16) | (gDiff << 8) | bDiff;
                delta.setRGB(x, y, diff);

            }

        setChanged();
        notifyObservers(new BufferedImage[]{imageTraff, imageNoTraff, delta});

    }

    @Override
    protected void reportProblem(Exception e) {
        logger.severe("Error in traffic thread: " + e);
    }

    @Override
    protected void reportInfo(String s) {
        logger.info(s);
    }
}
