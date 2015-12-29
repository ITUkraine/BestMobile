package itukraine.com.ua.bestmobile.util;

import android.content.Context;

import java.util.List;

import itukraine.com.ua.bestmobile.dao.Song;

/**
 * Created by User on 29.12.2015.
 */
public class TimeUtil {

    private static final int SECOND = 1000;
    private static final int MINUTE = 60 * SECOND;
    private static final int HOUR = 60 * MINUTE;
    private static final int DAY = 24 * HOUR;
    private static TimeUtil instance;
    private Context context;

    private TimeUtil() {
    }

    public static TimeUtil getInstance() {
        if (instance == null) {
            instance = new TimeUtil();
        }
        return instance;
    }

    public StringBuilder convertMillis(int ms) {
        StringBuilder text = new StringBuilder("");
        if (ms > DAY) {
            text.append(ms / DAY).append("d ");
            ms %= DAY;
        }
        if (ms > HOUR) {
            text.append(ms / HOUR).append("h ");
            ms %= HOUR;
        }
        if (ms > MINUTE) {
            text.append(ms / MINUTE).append("m ");
            ms %= MINUTE;
        }
        if (ms > SECOND) {
            text.append(ms / SECOND).append("s");
        }
        return text;
    }

    public int calculateTotalTimeOfPlaylist(List<Song> songs) {
        int totalTime = 0;
        for (Song song : songs) {
            totalTime += song.duration;
        }
        return totalTime;
    }
}
