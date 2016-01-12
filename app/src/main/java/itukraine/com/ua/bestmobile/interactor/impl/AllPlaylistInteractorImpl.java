package itukraine.com.ua.bestmobile.interactor.impl;

import java.util.List;

import itukraine.com.ua.bestmobile.App;
import itukraine.com.ua.bestmobile.R;
import itukraine.com.ua.bestmobile.entity.Playlist;
import itukraine.com.ua.bestmobile.interactor.AllPlaylistInteractor;
import itukraine.com.ua.bestmobile.repository.PlaylistRepository;
import itukraine.com.ua.bestmobile.repository.impl.PlaylistRepositoryImpl;

public class AllPlaylistInteractorImpl implements AllPlaylistInteractor {

    private PlaylistRepository playlistRepository;

    public AllPlaylistInteractorImpl() {
        playlistRepository = new PlaylistRepositoryImpl(App.getInstance());
    }

    @Override
    public List<Playlist> getAllPlaylists() {
        return playlistRepository.getPlaylists();
    }

    @Override
    public Playlist getPlaylist(String name) {
        return playlistRepository.findPlaylistByName(name);
    }

    @Override
    public void deletePlaylist(String name) {
        playlistRepository.deletePlaylistByName(name);
    }

    @Override
    public boolean isPlaylistDefault(String playlistName) {
        return playlistName.toLowerCase().equals(
                App.getInstance().getResources().getString(R.string.all_songs_playlist_name).toLowerCase());
    }

    @Override
    public void renamePlaylist(String oldName, String newName) {
        playlistRepository.renamePlaylist(oldName, newName);
    }
}
