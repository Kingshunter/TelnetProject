package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetworkUtils {

    private static final String regex = "^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$";

    public static String getValidIp(String ip) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(ip);
        if(!matcher.matches()){
            return "10.16.42.183";
        }
        return ip;
    }

    public static Integer getValidPort(String port) {
        return Integer.valueOf(port);
    }

}
