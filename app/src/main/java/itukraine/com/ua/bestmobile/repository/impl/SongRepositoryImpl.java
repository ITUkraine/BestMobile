package itukraine.com.ua.bestmobile.repository.impl;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;

import java.io.FileDescriptor;
import java.util.ArrayList;
import java.util.List;

import itukraine.com.ua.bestmobile.data.DatabaseManager;
import itukraine.com.ua.bestmobile.entity.Song;
import itukraine.com.ua.bestmobile.repository.SongRepository;

/**
 * Created by User on 05.01.2016.
 */
public class SongRepositoryImpl implements SongRepository {

    private static final String TAG = SongRepositoryImpl.class.getSimpleName();
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
    private Context context;

    public SongRepositoryImpl(Context context) {
        this.context = context;
    }

    @Override
    public List<Song> getAllSongs() {
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

    @Override
    public Bitmap getAlbumArt(Long album_id) {
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

    @Override
    public Song getSongByID(Long id) {
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

    @Override
    public List<Song> getSongsByID(List<Long> songsID) {
        List<Song> songs = new ArrayList<>();
        Song song = null;
        for (Long id : songsID) {
            song = getSongByID(id);
            if (song != null) {
                songs.add(song);
            }
        }
        return songs;
    }

    @Override
    public Long getCurrentSongId() {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getLong("currentSongId", -1);
    }

    @Override
    public void setCurrentSongId(Long currentSongId) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putLong("currentSongId", currentSongId).commit();
    }

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
