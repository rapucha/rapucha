package com.toad;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;

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
            //server.createContext("/style.css", new CSSHandler());
            server.setExecutor(Executors.newFixedThreadPool(20));

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

    private static final String CSS =
            "<style type=\"text/css\">\n" +
            "@media only screen and (max-device-width: 480px) {\n" +
            "select, input, button {\n" +
            "   display: block;\n" +
            "   margin-bottom: 1em;\n" +
            "   max-width: 100%;\n" +
            "   font-size: 220%;\n" +
            "}\n" +
            "input[type=\"text\"] {\n" +
            "height: 80px;\n" +
            "width: 380px;\n" +
            "}"+
            "input[type=\"datetime-local\"] {\n" +
            "height: 80px;\n" +
            "width: 380px;\n" +
            "}"+
             "input[type=\"email\"] {\n" +
                    "height: 80px;\n" +
                    "width: 380px;\n" +
                    "}"+
              "input[type=\"submit\"] {\n" +
                    "height: 80px;\n" +
                    "width: 380px;\n" +
                    "}"+
             "input[type=\"reset\"] {\n" +
                    "height: 80px;\n" +
                    "width: 380px;\n" +
                    "}"+

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

            "\t\t</style>" ;

    private static final String form2 = "";

    private static final String form ="<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n"+
            CSS+
            "</head>\n" +
            "<body>\n" +
            "\n" +
            "<h2>Хочу велик!!</h2>\n" +
            "\n" +
            "<form action=\"http://rapucha.ru/hello\" method=\"post\" enctype=\"text/plain\">\n" +
            "Где:<br>\n" +
            "<select name=\"where\">\n" +
            "  <option selected value=\"Here\">Тут</option>\n" +
            "  <option value=\"Near\">Рядом</option>\n" +
            "  <option value=\"There\">Там</option>\n" +
            "</select><br>"+

            "Когда:<br>\n" +
            "<select name=\"when\">\n" +
            "  <option value=\"Now\">Сейчас</option>\n" +
            "  <option value=\"Soon\">Через 10 минут</option>\n" +
            "  <option value=\"Later\">Через полчаса</option>\n" +
            "</select><br>"+
            "Кому:<br>\n" +
            "<input type=\"email\" name=\"mail\" value=\"Почта\"><br>\n" +
            "<input type=\"submit\" value=\"Send\" size=\"250\">\n" +
            "<br><small>rapucha@mail.ru</small>"+
            "</form>\n" +
            "\n" +
            "</body>\n" +
            "</html>";

    private class HelloHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            Headers h = t.getRequestHeaders();

            String qq="";
            for (List<String> strings : h.values()) {
                qq+=strings.toString();
            }
            String response;
            if("POST".equalsIgnoreCase(t.getRequestMethod())){
                Properties prop = new Properties();

                prop.load(t.getRequestBody());//TODO catch every MTF thing here

                response = Util.isToString(t.getRequestBody());

                t.sendResponseHeaders(200, response.length());
            } else {
                response = "F. Off";
                t.sendResponseHeaders(406, response.length());
            }


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
