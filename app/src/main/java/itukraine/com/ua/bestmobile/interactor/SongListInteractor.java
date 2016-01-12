package itukraine.com.ua.bestmobile.interactor;

import android.graphics.Bitmap;

import java.util.List;

import itukraine.com.ua.bestmobile.entity.Playlist;
import itukraine.com.ua.bestmobile.entity.Song;

public interface SongListInteractor {

    List<Song> getSongsFromPlaylist(Playlist playlist);

    Playlist getPlaylist(String name);

    void updatePlaylist(Playlist playlist);

    void deletePlaylist(String playlistName);

    Bitmap getAlbumArt(Long albumId);
}
