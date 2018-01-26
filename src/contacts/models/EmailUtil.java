package contacts.models;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс - утилита для проверки на валидность вводимого email
 */
public class EmailUtil {
    private static final Pattern EMAIL_PATTERN_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validate(String emailStr) {
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
