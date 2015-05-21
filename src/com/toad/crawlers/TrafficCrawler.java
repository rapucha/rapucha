package com.toad.crawlers;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Logger;

/**
 * Created by Morta on 21-May-15.
*/
public class TrafficCrawler extends ACrawler {

    private static final String address = "http://static-maps.yandex.ru/1.x/?ll=30.329246,59.943055&z=12&size=600,450&l=map";
    private static final int repeatMinutes = 20;

    public static ACrawler INSTANCE = new TrafficCrawler(repeatMinutes, address);

    private Logger logger = Logger.getLogger(this.getClass().getName());

    private  TrafficCrawler(int time, String address) {
        super(time, address, true);
    }

    @Override
    protected void processInput(InputStream is) {

       // URL pic = new URL(place+",trf");
       // URLConnection uc = pic.openConnection();
       // uc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36");
        BufferedImage imageNoTraff = null, imageTraff = null;
        try{
            imageNoTraff = ImageIO.read(is);
        } catch (IOException ioe){
            logger.severe("Cannot read image form stream "+ioe);
            ioe.printStackTrace();
        }

        try{
            URLConnection uc = new URL(address+",trf").openConnection();
            uc.setRequestProperty("User-Agent", getUserAgent());
            try (InputStream is2 = uc.getInputStream();) {
                imageTraff = ImageIO.read(is2);
            }

        }catch (IOException e){
            e.printStackTrace();
        }

        BufferedImage delta = new BufferedImage(imageTraff.getWidth(), imageTraff.getHeight(), imageTraff.getType());

        for(int x = 0; x < imageTraff.getWidth(); x++)
            for(int y = 0; y < imageTraff.getHeight(); y++) {


                int argb0 = imageNoTraff.getRGB(x, y);
                int argb1 = imageTraff.getRGB(x, y);

                int a0 = (argb0 >> 24) & 0xFF;
                int r0 = (argb0 >> 16) & 0xFF;
                int g0 = (argb0 >> 8) & 0xFF;
                int b0 = (argb0      ) & 0xFF;

                int a1 = (argb1 >> 24) & 0xFF;
                int r1 = (argb1 >> 16) & 0xFF;
                int g1 = (argb1 >>  8) & 0xFF;
                int b1 = (argb1      ) & 0xFF;

                int aDiff = 255-Math.abs(a1 - a0);
                int rDiff = 255-Math.abs(r1 - r0);
                int gDiff = 255-Math.abs(g1 - g0);
                int bDiff = 255-Math.abs(b1 - b0);

                int diff =
                        (aDiff << 24) | (rDiff << 16) | (gDiff << 8) | bDiff;
                delta.setRGB(x, y, diff);

            }

        setChanged();
        notifyObservers(new BufferedImage[]{imageTraff,imageNoTraff,delta});

    }
}
