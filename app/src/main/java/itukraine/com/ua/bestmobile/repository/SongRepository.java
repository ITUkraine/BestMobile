package itukraine.com.ua.bestmobile.repository;

import android.graphics.Bitmap;

import java.util.List;

import itukraine.com.ua.bestmobile.entity.Song;

/**
 * Created by User on 05.01.2016.
 */
public interface SongRepository {

    /**
     * Scan sdcard for music files.
     *
     * @return return list of all songs which was found on device
     */
    List<Song> getAllSongs();

    /**
     * Get album art by MediaStore.Audio.Media.ALBUM_ID.
     *
     * @param album_id ALBUM_ID of art
     * @return Bitmap related to ALBUM_ID
     */
    Bitmap getAlbumArt(Long album_id);

    /**
     * Get song by it's id.
     *
     * @param id ID of the song from MediaStore
     * @return Instance of song
     */
    Song getSongByID(Long id);

    /**
     * Get list of songs.
     *
     * @param songsID List of songs IDs from MediaStore
     * @return List of songs.
     */
    List<Song> getSongsByID(List<Long> songsID);

    Long getCurrentSongId();

    void setCurrentSongId(Long currentSongId);
}