package model;

import java.util.regex.Pattern;

public class MyRegex {
    static String email_pattern = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$";
    static String phone_pattern = "^[1][3,4,5,7,8][0-9]{9}$";

    public MyRegex() {

    }

    public static boolean isValidPhoneNumber(String str) {
        return Pattern.matches(phone_pattern, str);
    }

    public static boolean isValidEmail(String str) {
        return Pattern.matches(email_pattern, str);
    }

}
