package com.toad.server;

//import com.toad.crawlers.StationCache;
import static com.toad.crawlers.StationCache.STATION_CACHE;

/**
 * Created by toad on 5/26/15.
 */
final class HtmlDocuments {
    public static final String WHEN = "when";
    public static final String WHERE = "where";
    public static final String EMAIL = "email";
    public static final String NOW = "Now";
    public static final String SOON = "Soon";
    public static final String LATER = "Later";
    static String map = "<p id=\"demo\">Click the button to get your position.</p>\n" +
            "\n" +
            "<button onclick=\"getLocation()\">Try It</button>\n" +
            "\n" +
            "<div id=\"mapholder\"></div>\n" +
            "\n" +
            "<script>\n" +
            "var x = document.getElementById(\"demo\");\n" +
            "\n" +
            "function getLocation() {\n" +
            "    if (navigator.geolocation) {\n" +
            "        navigator.geolocation.getCurrentPosition(showPosition, showError);\n" +
            "    } else {\n" +
            "        x.innerHTML = \"Geolocation is not supported by this browser.\";\n" +
            "    }\n" +
            "}\n" +
            "\n" +
            "function showPosition(position) {\n" +
            "    var latlon = position.coords.latitude + \",\" + position.coords.longitude;\n" +
            "\n" +
            "    var img_url = \"http://maps.googleapis.com/maps/api/staticmap?center=\"\n" +
            "    +latlon+\"&zoom=14&size=400x300&sensor=false\";\n" +
            "    document.getElementById(\"mapholder\").innerHTML = \"<img src='\"+img_url+\"'>\";\n" +
            "}\n" +
            "\n" +
            "function showError(error) {\n" +
            "    switch(error.code) {\n" +
            "        case error.PERMISSION_DENIED:\n" +
            "            x.innerHTML = \"User denied the request for Geolocation.\"\n" +
            "            break;\n" +
            "        case error.POSITION_UNAVAILABLE:\n" +
            "            x.innerHTML = \"Location information is unavailable.\"\n" +
            "            break;\n" +
            "        case error.TIMEOUT:\n" +
            "            x.innerHTML = \"The request to get user location timed out.\"\n" +
            "            break;\n" +
            "        case error.UNKNOWN_ERROR:\n" +
            "            x.innerHTML = \"An unknown error occurred.\"\n" +
            "            break;\n" +
            "    }\n" +
            "}\n" +
            "</script>";

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
                    "}" +
                    "input[type=\"datetime-local\"] {\n" +
                    "height: 80px;\n" +
                    "width: 380px;\n" +
                    "}" +
                    "input[type=\"email\"] {\n" +
                    "height: 80px;\n" +
                    "width: 380px;\n" +
                    "}" +
                    "input[type=\"submit\"] {\n" +
                    "height: 80px;\n" +
                    "width: 380px;\n" +
                    "}" +
                    "input[type=\"reset\"] {\n" +
                    "height: 80px;\n" +
                    "width: 380px;\n" +
                    "}" +

                    "\n" +
                    "/* Follow the size of input elements */\n" +
                    "label {\n" +
                    "   font-size: 220%;\n" +
                    "}\n" +
                    "\n" +
                    "/* Checkbox is still inline */\n" +
                    "input[type=checkbox] {\n" +
                    "    display: inline;\n" +
                    "}" +
                    "\t}" +

                    "\t\t</style>";
    public static final String mainForm = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "\n" +
            "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n" +
            CSS +
            "</head>\n" +
            "<body>\n" +
            "\n" +
            "<h2>Хочу велик!!</h2>\n" +
            "\n" +
            "<form action=\"http://rapucha.ru/hello\" method=\"post\" enctype=\"text/plain\">\n" +
            "Где:<br>\n" +
            getStationsOptionsList()+
            "<br>" +
            "Когда:<br>\n" +
            "<select name=" + WHEN + ">\n" +
            "  <option selected value=" + NOW + ">Сейчас</option>\n" +
            "  <option value=" + SOON + ">Через 10 минут</option>\n" +
            "  <option value=" + LATER + ">Через полчаса</option>\n" +
            "</select><br>" +
            "Кому:<br>\n" +
            "<input type=" + EMAIL + " name="+EMAIL+ " placeholder=\"Почта\" autocomplete = \"on\" required><br>\n" +
            "<input type=\"submit\" value=\"Уведомить\" size=\"250\">\n" +
            "<br><small>rapucha@yandex.ru</small>\n" +
            "</form>\n" +
 //           "\n" +map+
            "</body>\n" +
            "</html>";

        private static String getStationsOptionsList(){
                StringBuilder sb = new StringBuilder("<select name=" + WHERE + ">\n");
                STATION_CACHE.getStationNames();
                for (int i = 0; i < STATION_CACHE.getStationNames().length; i++) {
                        String station = STATION_CACHE.getStationNames()[i];
                        sb.append("  <option value="+STATION_CACHE.getStationNumber(station)+">");
                        sb.append(station);
                        sb.append(" свободно: ");
                        sb.append(STATION_CACHE.getFreeBikes(station)+"/"+STATION_CACHE.getLocks(station));
                        sb.append("</option>\n");
                }
                sb.append("</select>\n");
                return sb.toString();
        }

//            "<input type=time name="+WHEN+ " step=\"60\"  value =\"13:00\" required><br>\n" +

}
