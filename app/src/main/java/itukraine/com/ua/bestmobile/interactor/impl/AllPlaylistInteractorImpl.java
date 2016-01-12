package itukraine.com.ua.bestmobile.interactor.impl;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import itukraine.com.ua.bestmobile.App;
import itukraine.com.ua.bestmobile.R;
import itukraine.com.ua.bestmobile.entity.Playlist;
import itukraine.com.ua.bestmobile.entity.Song;
import itukraine.com.ua.bestmobile.interactor.AllPlaylistInteractor;
import itukraine.com.ua.bestmobile.repository.PlaylistRepository;
import itukraine.com.ua.bestmobile.repository.SongRepository;
import itukraine.com.ua.bestmobile.repository.impl.PlaylistRepositoryImpl;
import itukraine.com.ua.bestmobile.repository.impl.SongRepositoryImpl;

public class AllPlaylistInteractorImpl implements AllPlaylistInteractor {

    private PlaylistRepository playlistRepository;
    private SongRepository songRepository;

    public AllPlaylistInteractorImpl() {
        playlistRepository = new PlaylistRepositoryImpl(App.getInstance());
        songRepository = new SongRepositoryImpl(App.getInstance());
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

    @Override
    public int getTotalTimeOfPlaylist(Playlist playlist) {
        return playlistRepository.calculateTotalDuration(playlist);
    }

    @Override
    public void updatePlaylists() {
        scanMediaStore();
        Iterator<Long> iterator;
        for (Playlist playlist : playlistRepository.getPlaylists()) {
            iterator = playlist.songsId.iterator();
            while (iterator.hasNext()) {
                if (!getIdOfExistingSongs().contains(iterator.next())) {
                    iterator.remove();
                }
            }
            playlistRepository.savePlaylist(playlist);
        }
    }

    private void scanMediaStore() {
        App.getInstance().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.parse("file://" + Environment.getExternalStorageDirectory())));
    }

    private List<Long> getIdOfExistingSongs() {
        List<Long> existingSongsId = new ArrayList<>();
        for (Song song : songRepository.getAllSongs()) {
            existingSongsId.add(song.id);
        }
        return existingSongsId;
    }
}
