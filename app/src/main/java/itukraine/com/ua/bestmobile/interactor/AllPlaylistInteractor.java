package itukraine.com.ua.bestmobile.interactor;

import java.util.List;

import itukraine.com.ua.bestmobile.entity.Playlist;

/**
 * Created by User on 05.01.2016.
 */
public interface AllPlaylistInteractor {
    List<Playlist> getAllPlaylists();

    void deletePlaylist(String name);

    void updateDefaultPlaylist();
}
