package com.toad.server;

//import com.toad.crawlers.StationCache;

import com.toad.SettingsManager;

/**
 * Created by Seva Nechaev "Rapucha" on 5/26/15. All rights reserved ;)
 */
final class HtmlDocuments {
    public static final String WHEN = "when";
    public static final String WHERE = "where";
    public static final String EMAIL = "email";
    public static final String NOW = "Now";
    public static final String SOON = "Soon";
    public static final String LATER = "Later";
    public static final String BIKES = "bikes";
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

    public static final String CSS =
            "<style type=\"text/css\">\n" +
                    "@media only screen and (max-device-width: 480px) {\n" +
                    "select, input, button {\n" +
                    "   display: block;\n" +
                    "   margin-bottom: 1em;\n" +
                    //"   max-width: 100%;\n" +
                    "   font-size: 320%;\n" +
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
                    "body {\n" +
                    " background-image: url(http://3.bp.blogspot.com/-hhkEW0JeS08/TpIcJTbNKiI/AAAAAAAAAjQ/nNcf96rMgIY/s1600/Suzy%2BTrack%2BRace%2BBike.png);\n" +
                    "    background-size: 200%;" +
                    //"    background-repeat: no-repeat;"+
                    //"opacity : 0.2;"+
                    "}"+
                    "\t\t</style>";
    public static final String part1 = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "\n" +
            "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n" +
            CSS +
            "</head>\n" +
            "<body onload=\"loadEmail()\">\n" +
            "\n" +
            "<h1>Хочу велик!!</h1>\n" +
            "\n" +
            "<form action=" + SettingsManager.host + "/hello method=\"post\" enctype=\"text/plain\" onsubmit=\"saveEmail()\">\n" +
            "<label for=stations>Где:</label><br>"+
            "<select id=stations multiple size=\"4\" required name=" + WHERE + ">\n"+
            "<option value=\"\" disabled selected>Выберите станцию</option>";

    public static final String part3 = "</select><br>\n" +
            //"Когда:<br>\n" +
            "<br><label for=time_when>Когда:</label><br>"+
            "<select id=time_when name=" + WHEN + ">\n" +
            "  <option selected value=" + NOW + ">Сейчас</option>\n" +
            "  <option value=" + SOON + ">Через полчаса</option>\n" +
            "  <option value=" + LATER + ">Через час</option>\n" +
            "</select>  " +
           // "Сколько:\n" +
            "<label for=num>Сколько:</label>"+
            "  <input type=\"range\" name=" + BIKES + " value=1 id=\"num\" min=1 max=8 required onchange=\"performRangeAction(value)\">" +
            "<output for=\"num\" id=\"volume\">1</output><br>" +
            "<br>" +
            "<script>" +
                "function performRangeAction(n) {" +
                "document.getElementById('volume').innerHTML = n"+
                "}" +
            "        function saveEmail(){\n" +
            "            var inputEmail= document.getElementById(\"id_email\");\n" +
            "            localStorage.setItem(\"userMail\", inputEmail.value);\n" +
            "        }\n" +
            "        function loadEmail(){\n" +
            "            var inputEmail=localStorage.getItem(\"userMail\");\n" +
            "            document.getElementById(\"id_email\").value = inputEmail;\n" +
            "        }\n"+
            "</script>"+
            //"Кому:<br>\n" +
            "<br><label for=id_email>Кому:</label>"+
            "<input id=\"id_email\" type=" + EMAIL + " name=" + EMAIL + " placeholder=Почта autocomplete=on required> <br>\n" +
            "<input type=\"submit\" value=\"Уведомить\" size=\"250\">\n" +
            "<br><br><small>rapucha@yandex.ru</small>\n" +
            "</form>\n" +
            "</body>\n" +
            "</html>";


}
