package itukraine.com.ua.bestmobile.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import itukraine.com.ua.bestmobile.dao.Song;

public class MusicUtil {

    private static final String TAG = MusicUtil.class.getSimpleName();
    private static MusicUtil instance;

    private MusicUtil() {
    }

    public static MusicUtil getInstance() {
        if (instance == null) {
            instance = new MusicUtil();
        }
        return instance;
    }

    public void scanSdcard(Context context) {
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION
        };
        final String sortOrder = MediaStore.Audio.AudioColumns.TITLE + " COLLATE LOCALIZED ASC";

        Cursor cursor = null;
        try {
            Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            cursor = context.getContentResolver().query(uri, projection, selection, null, sortOrder);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Song song = new Song();
                    song.title = cursor.getString(0);
                    song.artist = cursor.getString(1);
                    song.album = cursor.getString(2);
                    song.path = cursor.getString(3);
                    song.duration = cursor.getString(4);

                    Log.i(TAG, song.toString());

                    cursor.moveToNext();
                }
            }

        } catch (Exception e) {
            Log.e(TAG, e.toString(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

}
