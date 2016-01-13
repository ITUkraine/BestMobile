package itukraine.com.ua.bestmobile.interactor.impl;

import java.util.List;

import itukraine.com.ua.bestmobile.App;
import itukraine.com.ua.bestmobile.entity.Playlist;
import itukraine.com.ua.bestmobile.entity.Song;
import itukraine.com.ua.bestmobile.interactor.PickSongInteractor;
import itukraine.com.ua.bestmobile.repository.PlaylistRepository;
import itukraine.com.ua.bestmobile.repository.SongRepository;
import itukraine.com.ua.bestmobile.repository.impl.PlaylistRepositoryImpl;
import itukraine.com.ua.bestmobile.repository.impl.SongRepositoryImpl;

public class PickSongInteractorImpl implements PickSongInteractor {

    private PlaylistRepository playlistRepository;
    private SongRepository songRepository;

    public PickSongInteractorImpl() {
        playlistRepository = new PlaylistRepositoryImpl(App.getInstance());
        songRepository = new SongRepositoryImpl(App.getInstance());
    }

    @Override
    public Playlist getPlaylist(String name) {
        return playlistRepository.findPlaylistByName(name);
    }

    @Override
    public List<Song> getAllSongs() {
        return songRepository.getAllSongs();
    }

    @Override
    public void createOrUpdatePlaylist(Playlist playlist) {
        playlistRepository.savePlaylist(playlist);
    }
}
