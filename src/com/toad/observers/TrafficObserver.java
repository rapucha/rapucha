package com.toad.observers;

import com.toad.DBManager;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

/**
 * Created by Morta on 21-May-15.
 */
public class TrafficObserver implements Observer {

    private  final Logger logger = Logger.getLogger(this.getClass().getName());
    private Connection conn;


    @Override
    public void update(Observable o, Object arg) {

        conn = DBManager.getConn();
        BufferedImage[] imgs = (BufferedImage[])arg;

        Timestamp ts = new Timestamp(new java.util.Date().getTime());

        String addObservation = "INSERT INTO traffic_history_Y (timestamp, station_number, no_traffic, traffic, delta )" +
                "  VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(addObservation);) {
            ps.setTimestamp(1, ts);
            ps.setInt(2, 1);

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            try {
                ImageIO.write(imgs[0], "png", os);
            } catch (IOException e) {
                e.printStackTrace();
            }
            InputStream is = new ByteArrayInputStream(os.toByteArray());

            ps.setBlob(3, is);

            os = new ByteArrayOutputStream();
            try {
                ImageIO.write(imgs[1], "png", os);
            } catch (IOException e) {
                e.printStackTrace();
            }
            is = new ByteArrayInputStream(os.toByteArray());


            ps.setBlob(4, is);

            os = new ByteArrayOutputStream();
            try {
                ImageIO.write(imgs[2], "png", os);
            } catch (IOException e) {
                e.printStackTrace();
            }
            is = new ByteArrayInputStream(os.toByteArray());

            ps.setBlob(5, is);

            ps.executeUpdate();
        } catch (SQLException e) {
            logger.severe("cannot paste "+e.getMessage());
            e.printStackTrace();
        }

        try {
            conn.close();
        } catch (SQLException e) {
            logger.severe("Cannot close connection "+e);
            e.printStackTrace();
        }

    }



}

