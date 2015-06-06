package com.toad.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.toad.SettingsManager;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;

/**
 * Created by Seva Nechaev "Rapucha" on 5/28/15. All rights reserved ;)
 */
class BGImageHandler implements HttpHandler {
    private static BufferedImage bg;
    private static byte[] imageBytes;
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final String strInFile = SettingsManager.bgimage;

    public BGImageHandler() {
        try {
            bg = ImageIO.read(new File(strInFile));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bg, "png", baos);
            imageBytes = baos.toByteArray();

        } catch (IOException e) {
            logger.warning("Cannot load bg image");
            e.printStackTrace();
        }

    }

    @Override
    public void handle(HttpExchange t) throws IOException {

        t.sendResponseHeaders(200, 0);
        OutputStream os = t.getResponseBody();

        os.write(imageBytes);
        os.close();
    }
}
