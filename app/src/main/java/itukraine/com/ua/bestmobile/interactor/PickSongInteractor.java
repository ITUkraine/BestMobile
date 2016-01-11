package itukraine.com.ua.bestmobile.interactor;

import java.util.List;

import itukraine.com.ua.bestmobile.entity.Playlist;
import itukraine.com.ua.bestmobile.entity.Song;

public interface PickSongInteractor {

    Playlist getPlaylist(String name);

    List<Song> getAllSongs();

    int calculateDurationOfPlaylist(Playlist playlist);

    void createOrUpdatePlaylist(Playlist playlist, boolean isNewPlaylist);
}