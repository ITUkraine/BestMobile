package itukraine.com.ua.bestmobile.repository;

import java.util.List;

import itukraine.com.ua.bestmobile.entity.Playlist;

/**
 * Created by User on 05.01.2016.
 */
public interface PlaylistRepository {

    void addPlaylist(Playlist playlist);

    List<Playlist> getPlaylists();

    Playlist findPlaylistByName(String name);

    void deletePlaylistByName(String name);

    void renamePlaylist(String oldPlaylistName, String newPlaylistName);

    void updatePlaylist(Playlist playlist);

}
