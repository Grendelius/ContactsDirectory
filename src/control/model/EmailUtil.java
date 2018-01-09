package control.model;

import control.MainApp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailUtil {
    private static final Pattern EMAIL_PATTERN_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private MainApp mainApp;

    private static boolean validate(String emailStr) {
        Matcher matcher = EMAIL_PATTERN_REGEX.matcher(emailStr);
        return matcher.find();
    }

    public static String format(String emailStr) {
        if (emailStr == null || !(validate(emailStr))) {
            return null;
        }
        return emailStr;
    }


}
