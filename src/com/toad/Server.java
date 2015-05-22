package com.toad;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import static com.toad.SettingsManager.port;


/**
 * Created by Morta on 21-May-15.
 */
public class Server {


    private static HttpServer server;
    private final static Server INSTANCE = new Server();

    private Server() {
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/", new FormHandler());
            server.createContext("/hello", new HelloHandler());
            server.createContext("/style.css", new CSSHandler());
            server.setExecutor(null);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void start(){
        server.start();
    }
    static class FormHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            //Util.printFile(Crawler.accessLog, "------------------------" + new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date()) + "--------------------------------" + System.lineSeparator());
            //Util.printFile(Crawler.accessLog, "Request from " + t.getRemoteAddress() + System.lineSeparator());
            Headers h = t.getRequestHeaders();
            String response = form;
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();

           // Util.printFile(Crawler.accessLog, "Total free bikes: " + Crawler.FREE_BIKES + System.lineSeparator());
            //Util.printFile(Crawler.accessLog, "Processing time: " + time + System.lineSeparator());
            //Util.printFile(Crawler.accessLog, "--=====Headers=====--" + System.lineSeparator());


        }
    }

    private static final String form ="<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n"+
            "<style type=\"text/css\">\n" +
            "@media only screen and (max-device-width: 480px) {\n" +
            "select, input, button {\n" +
            "   display: block;\n" +
            "   margin-bottom: 1em;\n" +
            "   max-width: 100%;\n" +
            "   font-size: 220%;\n" +
            "}\n" +
            "\n" +
            "/* Follow the size of input elements */\n" +
            "label {\n" +
            "   font-size: 220%;\n" +
            "}\n" +
            "\n" +
            "/* Checkbox is still inline */\n" +
            "input[type=checkbox] {\n" +
            "    display: inline;\n" +
            "}"+
            "\t}"+

            "\t\t</style>"+
            "</head>\n" +
            "<body>\n" +
            "\n" +
            "<h2>Хочу велик!!</h2>\n" +
            "\n" +
            "<form action=\"http://rapucha.ru/hello\" method=\"post\" enctype=\"text/plain\">\n" +
            "Где:<br>\n" +
            "<input type=\"text\" name=\"name\" value=\"your name\"><br>\n" +
            "Когда:<br>\n" +
            "<input type=\"datetime-local\" value=\"Прямо щас\"/><br>\n"+
            "Моя почта:<br>\n" +
            "<input type=\"email\" name=\"mail\" value=\"your email\"><br>\n" +
            "Привет автору)<br>\n" +
            "<input type=\"text\" name=\"comment\" value=\"your comment\" size=\"50\"><br><br>\n" +
            "<input type=\"submit\" value=\"Send\" size=\"250\">\n" +
            "<input type=\"reset\" value=\"Reset\" size=\"250\">\n" +
            "</form>\n" +
            "\n" +
            "</body>\n" +
            "</html>";

    private class HelloHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            Headers h = t.getRequestHeaders();
            String response = test;
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();

        }
    }

    String test ="<!DOCTYPE html>\n" +
            "<html>\n" +
            "<body>\n" +
            "<body>\n" +
            "    <div id=\"container\">\n" +
            "        <form action=\"#\" method=\"post\">\n" +
            "            <label for=\"name\">Name and surname</label><br />\n" +
            "            <input type=\"text\" id=\"name\" name=\"name\">\n" +
            "            <br /><br />\n" +
            "            <label for=\"web\">Site</label><br />\n" +
            "            <input type=\"url\" id=\"web\" name=\"web\">\n" +
            "            <br /><br />\n" +
            "            <label for=\"email\">Email</label><br />\n" +
            "            <input type=\"email\" id=\"email\" name=\"email\">\n" +
            "            <br /><br />\n" +
            "            <label for=\"phone\">Phone nummber</label><br />\n" +
            "            <input type=\"tel\" id=\"phone\" name=\"phone\">\n" +
            "            <br /><br />\n" +
            "            <input type=\"submit\" value=\"submit\" id=\"submit\">\n" +
            "        </form>\n" +
            "    </div>\n" +
            "</body>"+
            "</body>\n" +
            "</html>";

    private class CSSHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            Headers h = t.getRequestHeaders();
            String response = css;
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();

        }
    }
String css = "body {\n" +
        "\t  background-color: #FF0000;\n" +
        "\t}\n" +
        "\t";
    ;
}
