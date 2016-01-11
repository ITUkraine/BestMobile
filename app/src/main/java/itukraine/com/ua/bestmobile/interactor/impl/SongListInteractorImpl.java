package itukraine.com.ua.bestmobile.interactor.impl;

import android.graphics.Bitmap;

import java.util.List;

import itukraine.com.ua.bestmobile.App;
import itukraine.com.ua.bestmobile.R;
import itukraine.com.ua.bestmobile.entity.Playlist;
import itukraine.com.ua.bestmobile.entity.Song;
import itukraine.com.ua.bestmobile.interactor.SongListInteractor;
import itukraine.com.ua.bestmobile.repository.PlaylistRepository;
import itukraine.com.ua.bestmobile.repository.SongRepository;
import itukraine.com.ua.bestmobile.repository.impl.PlaylistRepositoryImpl;
import itukraine.com.ua.bestmobile.repository.impl.SongRepositoryImpl;

public class SongListInteractorImpl implements SongListInteractor {

    private final PlaylistRepository playlistRepository;
    private final SongRepository songRepository;

    public SongListInteractorImpl() {
        playlistRepository = new PlaylistRepositoryImpl(App.getInstance());
        songRepository = new SongRepositoryImpl(App.getInstance());
    }

    @Override
    public List<Song> getSongsFromPlaylist(Playlist playlist) {
        List<Song> songList;
        if (playlist.name.equals(App.getInstance().getString(R.string.all_songs_playlist_name))) {
            songList = songRepository.getAllSongs();
        } else {
            songList = songRepository.getSongsByID(playlist.songsId);
        }
        return songList;
    }

    @Override
    public Playlist getPlaylist(String name) {
        return playlistRepository.findPlaylistByName(name);
    }

    @Override
    public void updatePlaylist(Playlist playlist) {
        playlistRepository.updatePlaylist(playlist);
    }

    @Override
    public int calculateDurationOfPlaylist(Playlist playlist) {
        return playlistRepository.calculateTotalDuration(playlist);
    }

    @Override
    public void deletePlaylist(String playlistName) {
        playlistRepository.deletePlaylistByName(playlistName);
    }

    @Override
    public Bitmap getAlbumArt(Long albumId) {
        return songRepository.getAlbumArt(albumId);
    }
}
