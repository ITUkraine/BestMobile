package itukraine.com.ua.bestmobile.util;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;

import java.io.FileDescriptor;

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

    /**
     * Scan sdcard for music files.
     *
     * @param context
     */
    public void scanSdcard(Context context) {
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM_ID
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
                    song.id = cursor.getLong(cursor
                            .getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                    song.title = cursor.getString(cursor
                            .getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                    song.artist = cursor.getString(cursor
                            .getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                    song.album = cursor.getString(cursor
                            .getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
                    song.path = cursor.getString(cursor
                            .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                    song.duration = cursor.getInt(cursor
                            .getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                    song.albumId = cursor.getLong(cursor
                            .getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));

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

    /**
     * Get album art by MediaStore.Audio.Media.ALBUM_ID.
     *
     * @param context
     * @param album_id ALBUM_ID of art
     * @return Bitmap related to ALBUM_ID
     */
    public Bitmap getAlbumart(Context context, Long album_id) {
        Bitmap bm = null;
        try {
            final Uri ALBUM_ART_URI = Uri.parse("content://media/external/audio/albumart");

            Uri uri = ContentUris.withAppendedId(ALBUM_ART_URI, album_id);

            ParcelFileDescriptor pfd = context.getContentResolver()
                    .openFileDescriptor(uri, "r");

            if (pfd != null) {
                FileDescriptor fd = pfd.getFileDescriptor();
                bm = BitmapFactory.decodeFileDescriptor(fd);
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString(), e);
        }
        return bm;
    }

}
