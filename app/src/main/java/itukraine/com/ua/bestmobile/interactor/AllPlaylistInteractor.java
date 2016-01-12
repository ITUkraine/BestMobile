package itukraine.com.ua.bestmobile.interactor;

import java.util.List;

import itukraine.com.ua.bestmobile.entity.Playlist;

/**
 * Created by User on 05.01.2016.
 */
public interface AllPlaylistInteractor {
    List<Playlist> getAllPlaylists();

    Playlist getPlaylist(String name);

    void deletePlaylist(String name);

    boolean isPlaylistDefault(String playlistName);

    void renamePlaylist(String oldName, String newName);
}
