package com.toad.server;

/**
 * Created by toad on 5/26/15.
 */
public class HtmlDocuments {
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
            "<select name=\"where\">\n" +
            "  <option selected value=\"Here\">Тут</option>\n" +
            "  <option value=\"Near\">Рядом</option>\n" +
            "  <option value=\"There\">Там</option>\n" +
            "</select>\n" +
            "<br>" +

            "Когда:<br>\n" +
            "<select name=\"when\">\n" +
            "  <option value=\"Now\">Сейчас</option>\n" +
            "  <option value=\"Soon\">Через 10 минут</option>\n" +
            "  <option value=\"Later\">Через полчаса</option>\n" +
            "</select><br>" +
            "Кому:<br>\n" +
            "<input type=\"email\" name=\"mail\" value=\"Почта\"><br>\n" +
            "<input type=\"submit\" value=\"Send\" size=\"250\">\n" +
            "<br><small>rapucha@mail.ru</small>\n" +
            "</form>\n" +
            "\n" +
            "</body>\n" +
            "</html>";
}
