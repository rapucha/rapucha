package com.toad.server;


import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.toad.SettingsManager;
import com.toad.Util;
import com.toad.crawlers.StationCache;
import com.toad.subscription.Client;
import com.toad.subscription.Processor;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by toad on 5/26/15.
 */
class HelloHandler implements HttpHandler {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private String customMessage = "";
    private String verbalWhen = "";

    @Override
    public void handle(HttpExchange t) throws IOException {
        Headers h = t.getRequestHeaders();

        StringBuilder sb = new StringBuilder("Hello visited from host ");
        sb.append(t.getRemoteAddress());
        for (String s : h.keySet()) {
            sb.append("\nheader " + s + " : " + h.get(s));
        }
        logger.info(sb.toString());

        StringBuilder response = new StringBuilder();

        response.append("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n" +
                "</head>\n" +
                "<body>\n");

        if (requestIsValid(t)) {
            Map<String, List<String>> params = Util.parse(t.getRequestBody());
            List<String> stations = params.get(HtmlDocuments.WHERE);
            for (String k : params.keySet()) {
                logger.info(k + ": " + params.get(k));
            }

            int minutes = convertToMinutes(params.get(HtmlDocuments.WHEN).get(0));
            Client client = new Client(minutes, params.get(HtmlDocuments.EMAIL).get(0),
                    Integer.parseInt(params.get(HtmlDocuments.BIKES).get(0)),
                    params.get(HtmlDocuments.WHERE).stream().map(s -> StationCache.STATION_CACHE.getStationName(s)).collect(Collectors.toList()));
            Processor.INSTANCE.addClient(client);
            response.append("Вы хотите узнать \n");
            response.append(verbalWhen);
            response.append(client.getAtWhatStations().size() > 1 ? ", когда на станциях \n" : ", когда на станции \n");
            client.getAtWhatStations().stream().forEach(s -> response.append("\"" + s + "\"" + ", "));
            response.append(" будет ");
            String bikes;
            switch (client.getHowManyBikes()) {
                case 1:
                    bikes = "один свободный велосипед";
                    break;
                case 2:
                    bikes = "два свободных велосипеда";
                    break;
                case 3:
                    bikes = "три свободных велосипеда";
                    break;
                case 4:
                    bikes = "четыре свободных велосипеда";
                    break;
                case 5:
                    bikes = "пять свободных велосипедов";
                    break;
                case 6:
                    bikes = "шесть свободных велосипедов";
                    break;
                case 7:
                    bikes = "семь свободных велосипедов";
                    break;
                case 8:
                    bikes = "восемь свободных велосипедов.. очень много";
                    break;

                default:
                    bikes = "Что-то сломалось";
                    logger.severe("unexpected number of bikes in client " + client.getHowManyBikes());
            }
            response.append(bikes);
            response.append(".\n<br>На почту ");
            response.append(client.getEmail());
            response.append(" будет выслано письмо. \n<br> Удачи!<br>Кстати, во всём городе доступных велосипедов сейчас ");
            response.append(StationCache.STATION_CACHE.getTotalBikes());
        } else {
            response.append(" я не смог выполнить ваш запрос. Внутренняя ошибка: ");
            response.append(customMessage);
        }
        response.append("<br></body>\n </html>");

        byte[] reply = response.toString().getBytes("UTF-8");
        t.sendResponseHeaders(200, reply.length);
        OutputStream os = t.getResponseBody();
        os.write(reply);
        os.close();
    }

    private boolean requestIsValid(HttpExchange t) {
        List<String> cookies = t.getRequestHeaders().get(CookieProvider.COOKIE);
        if (null == cookies) {
            logger.fine("no cookie ");
            customMessage = "no cookie ";
            return false;
        }
        if (!"POST".equalsIgnoreCase(t.getRequestMethod())) {
            logger.fine("not a post request");
            customMessage = "not a post request";
            return false;
        }
//        if (somebodyIsTooFast(t) ){ // check the combination of address,user-agent and X-Forward
//            return false;
//        }
        List<String> referers = t.getRequestHeaders().get("Referer");
        if (!(referers.contains(SettingsManager.host + "/") || (referers.contains("http://localhost/")))) {
            logger.info("Wrong referer: ");
            t.getRequestHeaders().get("Referer").forEach(logger::fine);
            customMessage = "Wrong referer: ";
            return false;
        }

        List<String> result = cookies.stream().filter(cookie -> cookie.startsWith("id=")).collect(Collectors.toList());
        if (result.size() > 1) {
            logger.info("Cookies size is >1 : " + result.size());
        }
        customMessage = CookieProvider.cookieIsGood(result.get(0));
        return "ok".equals(customMessage);

    }

    private int convertToMinutes(String when) {
        int minutes = 0;
        switch (when) {
            case HtmlDocuments.NOW:
                minutes = 0;
                verbalWhen = "сразу";
                break;
            case HtmlDocuments.SOON:
                minutes = 30;
                verbalWhen = "через полчаса";
                break;
            case HtmlDocuments.LATER:
                minutes = 60;
                verbalWhen = "через час";
                break;
            default: {
                logger.severe("Unknown information time: " + when);
                break;
            }
        }
        return minutes;
    }
}
