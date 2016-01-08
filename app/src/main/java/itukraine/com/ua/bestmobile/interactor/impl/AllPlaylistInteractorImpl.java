package itukraine.com.ua.bestmobile.interactor.impl;

import java.util.List;

import itukraine.com.ua.bestmobile.App;
import itukraine.com.ua.bestmobile.R;
import itukraine.com.ua.bestmobile.entity.Playlist;
import itukraine.com.ua.bestmobile.entity.Song;
import itukraine.com.ua.bestmobile.interactor.AllPlaylistInteractor;
import itukraine.com.ua.bestmobile.repository.PlaylistRepository;
import itukraine.com.ua.bestmobile.repository.SongRepository;
import itukraine.com.ua.bestmobile.repository.impl.PlaylistRepositoryImpl;

/**
 * Created by User on 05.01.2016.
 */
public class AllPlaylistInteractorImpl implements AllPlaylistInteractor {

    private PlaylistRepository playlistRepository;
    private SongRepository songRepository;

    public AllPlaylistInteractorImpl() {
        playlistRepository = new PlaylistRepositoryImpl(App.getInstance());
    }

    @Override
    public List<Playlist> getAllPlaylists() {
        return playlistRepository.getPlaylists();
    }

    @Override
    public void deletePlaylist(String name) {
        playlistRepository.deletePlaylistByName(name);
    }

    @Override
    public void updateDefaultPlaylist() {
        Playlist defaultPlaylist = new Playlist(App.getInstance().getResources().getString(R.string.all_songs_playlist_name));
        for (Song song : songRepository.getAllSongs()) {
            defaultPlaylist.songsId.add(song.id);
        }
        playlistRepository.addPlaylist(defaultPlaylist);
    }
}
