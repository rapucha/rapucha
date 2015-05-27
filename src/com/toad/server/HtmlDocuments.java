package com.toad.server;

/**
 * Created by toad on 5/26/15.
 */
public final class HtmlDocuments {
    public static final String WHEN = "when";
    public static final String WHERE = "where";
    public static final String EMAIL = "mail";
    public static final String NOW = "Now";
    public static final String SOON = "Soon";
    public static final String LATER = "Later";
    public static final String HERE = "Here";
    public static final String NEAR = "Near";
    public static final String THERE = "There";


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
            "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n" +
            CSS +
            "</head>\n" +
            "<body>\n" +
            "\n" +
            "<h2>Хочу велик!!</h2>\n" +
            "\n" +
            "<form action=\"http://rapucha.ru/hello\" method=\"post\" enctype=\"text/plain\">\n" +
            "Где:<br>\n" +
            "<select name=" + WHERE + ">\n" +
            "  <option selected value=" + HERE + ">Тут</option>\n" +
            "  <option value=" + NEAR + ">Рядом</option>\n" +
            "  <option value=" + THERE + ">Там</option>\n" +
            "</select>\n" +
            "<br>" +

            "Когда:<br>\n" +
            "<select name=" + WHEN + ">\n" +
            "  <option value=" + NOW + ">Сейчас</option>\n" +
            "  <option value=" + SOON + ">Через 10 минут</option>\n" +
            "  <option value=" + LATER + ">Через полчаса</option>\n" +
            "</select><br>" +
            "Кому:<br>\n" +
            "<input type=" + EMAIL + " name=\"mail\" value=\"Почта\"><br>\n" +
            "<input type=\"submit\" value=\"Send\" size=\"250\">\n" +
            "<br><small>rapucha@mail.ru</small>\n" +
            "</form>\n" +
            "\n" +
            "</body>\n" +
            "</html>";
}
