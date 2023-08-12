package Models;

public class BaseRegex {
    public static final String digit = "\\d";
    public static final String port = "(1|[1-9][0-9]{0,3}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])";
    public static final String size = digit + "{1,10}";
    public static final String line = "(\\x0d\\x0a){0,1}";
    public static final String visibleChar = "[\\x20-\\xff]";
    public static final String passChar = "[\\x22-\\xff]";
    public static final String binary = "[\\x00-\\xff]";
    public static final String password = "(" + passChar + "{5,50})";
    public static final String bl = "[\\x20]";
    public static final String letter = "[a-z]|[A-Z]";
    public static final String digit_letter = "\\w";
    public static final String filename = "[" + digit_letter + " |[\\.]]" + "{1,20}";
    public static final String domain = "([" + digit_letter + " |[\\.]]" + "{5,20})";
    public static final String hash_filename = digit_letter + "{50,200}";
    public static final String hash_filecontent = digit_letter + "{50,200}";
    public static final String file_info = "(" + hash_filename + ")" + bl + "(" + size + ")" + bl + "(" + hash_filecontent + ")";
    public static final String login = "(" + digit_letter + "{5,20})";
    public static final String only_digits = size;
    public static final String key2FA = "([A-Z0-9]{16})";
    public static final String digits2FA = "([0-9]{6})";
}
