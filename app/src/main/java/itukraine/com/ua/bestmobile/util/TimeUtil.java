package itukraine.com.ua.bestmobile.util;

import java.util.concurrent.TimeUnit;

public class TimeUtil {
    private static TimeUtil instance;

    private TimeUtil() {
    }

    public static TimeUtil getInstance() {
        if (instance == null) {
            instance = new TimeUtil();
        }
        return instance;
    }

    public String formatTime(int ms) {
        long hours = TimeUnit.MILLISECONDS.toHours(ms);
        long min = TimeUnit.MILLISECONDS.toMinutes(ms) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(ms));
        long sec = TimeUnit.MILLISECONDS.toSeconds(ms) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(ms));
        String formatString;
        String result;
        if (hours == 0) {
            formatString = "%02d:%02d";
            result = String.format(formatString, min, sec);
        } else {
            formatString = "%02d:%02d:%02d";
            result = String.format(formatString, hours, min, sec);
        }
        return result;
    }

}
