package common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс - утилита для проверки на валидность вводимого phoneNumber
 */
public class PhoneNumberUtil {
    private static final Pattern PHONE_NUMBER_PATTERN =
            Pattern.compile("7\\([0-9]{3}\\)[0-9]{3}-[0-9]{2}-[0-9]{2}", Pattern.CASE_INSENSITIVE);

    public static boolean validate(String phoneStr) {
        Matcher matcher = PHONE_NUMBER_PATTERN.matcher(phoneStr);
        return matcher.find();
    }

    public static String format(String phoneStr) {
        if (phoneStr == null || !(validate(phoneStr))) {
            return null;
        }
        return phoneStr;
    }
}
