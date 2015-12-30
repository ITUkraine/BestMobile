package itukraine.com.ua.bestmobile.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by User on 30.12.2015.
 */
public class PrefUtil {

    private static SharedPreferences getPrefs(Context paramContext) {
        return PreferenceManager.getDefaultSharedPreferences(paramContext);
    }

    public static String getCurrentPlaylistName(Context paramContext) {
        return getPrefs(paramContext).getString("currentPlaylistName", null);
    }

    public static void setCurrentPlaylistName(Context paramContext, String currentPlaylistName) {
        getPrefs(paramContext).edit().putString("currentPlaylistName", currentPlaylistName).commit();
    }

    public static Long getCurrentSongId(Context paramContext) {
        return getPrefs(paramContext).getLong("currentSongId", -1);
    }

    public static void setCurrentSongId(Context paramContext, Long currentSongId) {
        getPrefs(paramContext).edit().putLong("currentSongId", currentSongId).commit();
    }

    public static int getShuffleType(Context paramContext) {
        return getPrefs(paramContext).getInt("shuffleType", 0);
    }

    public static void setShuffleType(Context paramContext, int shuffleType) {
        getPrefs(paramContext).edit().putInt("shuffleType", shuffleType).commit();
    }

    public static int getRevertType(Context paramContext) {
        return getPrefs(paramContext).getInt("revertType", 0);
    }

    public static void setRevertType(Context paramContext, int revertType) {
        getPrefs(paramContext).edit().putInt("revertType", revertType).commit();
    }
}
