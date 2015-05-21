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

public class TrafficCrawler extends ACrawler {

    private static final String address = "http://static-maps.yandex.ru/1.x/?ll=30.329246,59.943055&z=12&size=600,450&l=map";
    private static final int repeatMinutes = 20;

    private static ACrawler INSTANCE = new TrafficCrawler(repeatMinutes, address);

    private Logger logger = Logger.getLogger(this.getClass().getName());

    private  TrafficCrawler(int time, String address) {
        super(time, address, true);
    }
/*
    @Override
    protected void processInput(InputStream is) {

       // URL pic = new URL(place+",trf");
       // URLConnection uc = pic.openConnection();
       // uc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36");

        try{
            BufferedImage image0 = ImageIO.read(is);
        } catch (IOException ioe){
            logger.severe("Cannot reda image form stream "+ioe);
            ioe.printStackTrace();
        }
        try( URLConnection uc = new URL(address+",trf").openConnection();
        uc.setRequestProperty("User-Agent", getUserAgent());

        try (InputStream is = uc.getInputStream();) {
            processInput(is);
        }
        BufferedImage image1 = ImageIO.read(uc);


        pic = new URL(place);
        uc = pic.openConnection();
        uc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36");
        BufferedImage image1 = ImageIO.read(pic);
        ImageIO.write(image1, "png", new File("image10.png"));

        BufferedImage image6 = new BufferedImage(image1.getWidth(), image1.getHeight(), image1.getType());

        for(int x = 0; x < image1.getWidth(); x++)
            for(int y = 0; y < image1.getHeight(); y++) {


                int argb0 = image0.getRGB(x, y);
                int argb1 = image1.getRGB(x, y);

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
                image6.setRGB(x, y, diff);

            }
        ImageIO.write(image6, "png",  new File("image11.png"));

    }
}*/
