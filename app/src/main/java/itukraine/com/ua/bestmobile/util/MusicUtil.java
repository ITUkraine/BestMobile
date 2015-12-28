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
import java.util.ArrayList;
import java.util.List;

import itukraine.com.ua.bestmobile.dao.Song;

public class MusicUtil {

    private static final String TAG = MusicUtil.class.getSimpleName();
    private static MusicUtil instance;

    private final String[] PROJECTIONS = {
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ALBUM_ID
    };
    private final Uri MEDIA_CONTENT_URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

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
     * @return return list of all songs which was found on device
     */
    public List<Song> getAllSongs(Context context) {
        List<Song> allSongs = new ArrayList<>();
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        final String sortOrder = MediaStore.Audio.AudioColumns.TITLE + " COLLATE LOCALIZED ASC";

        Cursor cursor = null;
        try {
            cursor = context.getContentResolver()
                    .query(MEDIA_CONTENT_URI, PROJECTIONS, selection, null, sortOrder);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    allSongs.add(convertCursorToSong(cursor));

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

        return allSongs;
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
            Log.e(TAG, e.toString());
        }
        return bm;
    }

    /**
     * Get song by it's id.
     *
     * @param context
     * @param id      ID of the song from MediaStore
     * @return Instance of song
     */
    public Song getSongByID(Context context, Long id) {
        Cursor cursor = null;
        Song song = null;
        try {
            String selection = MediaStore.Audio.Media._ID + "=?";
            String[] selectionArgs = new String[]{"" + id}; //This is the id you are looking for

            cursor = context.getContentResolver()
                    .query(MEDIA_CONTENT_URI, PROJECTIONS, selection, selectionArgs, null);

            if (cursor != null && cursor.getCount() >= 0) {
                cursor.moveToPosition(0);

                song = convertCursorToSong(cursor);
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return song;
    }

    /**
     * Get list of songs.
     *
     * @param context
     * @param songsID List of songs IDs from MediaStore
     * @return List of songs.
     */
    public List<Song> getSongsByID(Context context, List<Long> songsID) {
        List<Song> songs = new ArrayList<>();
        Song song = null;
        for (Long id : songsID) {
            song = getSongByID(context, id);
            if (song != null) {
                songs.add(song);
            }
        }
        return songs;
    }

    /**
     * Convert data from cursor to instance of Song.
     *
     * @param cursor Cursor with data to convert
     * @return Converted instance
     */
    private Song convertCursorToSong(Cursor cursor) {
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
        return song;
    }

}
