package itukraine.com.ua.bestmobile.interactor.impl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

import itukraine.com.ua.bestmobile.App;
import itukraine.com.ua.bestmobile.R;
import itukraine.com.ua.bestmobile.entity.Playlist;
import itukraine.com.ua.bestmobile.entity.Song;
import itukraine.com.ua.bestmobile.interactor.PlayerInteractor;
import itukraine.com.ua.bestmobile.repository.MediaPlayerRepository;
import itukraine.com.ua.bestmobile.repository.PlaylistRepository;
import itukraine.com.ua.bestmobile.repository.SongRepository;
import itukraine.com.ua.bestmobile.repository.impl.MediaPlayerRepositoryImpl;
import itukraine.com.ua.bestmobile.repository.impl.PlaylistRepositoryImpl;
import itukraine.com.ua.bestmobile.repository.impl.SongRepositoryImpl;
import itukraine.com.ua.bestmobile.service.PlaybackService;
import itukraine.com.ua.bestmobile.util.ImageUtil;
import itukraine.com.ua.bestmobile.util.TimeUtil;

public class PlayerInteractorImpl implements
        PlayerInteractor,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    private SongRepository songRepository;
    private PlaylistRepository playlistRepository;
    private MediaPlayerRepository mediaPlayerRepository;

    private Playlist playlist;
    private int songPosInPlaylist;

    private Intent mPlayIntent;
    private PlaybackService playbackService;
    private ServiceConnection playbackConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlaybackService.PlaybackBinder binder = (PlaybackService.PlaybackBinder) service;
            playbackService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    public PlayerInteractorImpl() {
        songRepository = new SongRepositoryImpl(App.getInstance());
        playlistRepository = new PlaylistRepositoryImpl(App.getInstance());

        mediaPlayerRepository = MediaPlayerRepositoryImpl.getInstance();
        mediaPlayerRepository.setListeners(this, this);

        updateDefaultPlaylist();

        loadLatestPlayedPlaylist();
        loadLatestPlayedSong();

        startPlaybackService();
    }

    private void startPlaybackService() {
        if (mPlayIntent == null) {
            mPlayIntent = new Intent(App.getInstance(), PlaybackService.class);
            App.getInstance().bindService(mPlayIntent, playbackConnection, Context.BIND_AUTO_CREATE);
            App.getInstance().startService(mPlayIntent);
        }
    }

    private void stopPlaybackService() {
        App.getInstance().unbindService(playbackConnection);
        App.getInstance().stopService(mPlayIntent);
    }

    @Override
    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;

        playlistRepository.setCurrentPlaylistName(playlist.name);

        songPosInPlaylist = 0;
    }

    @Override
    public void updateDefaultPlaylist() {
        Playlist defaultPlaylist = new Playlist(App.getInstance().getResources().getString(R.string.all_songs_playlist_name));
        for (Song song : songRepository.getAllSongs()) {
            defaultPlaylist.songsId.add(song.id);
            //defaultPlaylist.totalTime += song.duration;
        }
        playlistRepository.savePlaylist(defaultPlaylist);
    }

    @Override
    public void setPlaylist(String playlistName) {
        setPlaylist(playlistRepository.findPlaylistByName(playlistName));
    }

    @Override
    public void setCurrentSongPosition(int position) {
        songPosInPlaylist = position;
    }

    @Override
    public void loadLatestPlayedPlaylist() {
        Playlist playlist;
        String currentPlaylist = playlistRepository.getCurrentPlaylistName();
        if (currentPlaylist == null) {
            playlist = playlistRepository.findPlaylistByName(App.getInstance().getString(R.string.all_songs_playlist_name));
        } else {
            playlist = playlistRepository.findPlaylistByName(currentPlaylist);
        }
        setPlaylist(playlist);
    }

    @Override
    public void loadLatestPlayedSong() {
        long currentSongId = playlistRepository.getSongPositionInPlaylistById(playlist, songRepository.getCurrentSongId());
        if (currentSongId == -1) {
            setCurrentSongPosition(0);
        } else {
            setCurrentSongPosition((int) currentSongId);
        }
    }

    @Override
    public void play() throws IOException {
        if (mediaPlayerRepository.isPlaying()) {
            mediaPlayerRepository.pause();
        } else {
            if (mediaPlayerRepository.getCurrentSongTimePlayed() > getCurrentSong().duration) {
                playCurrentSong();
            } else {
                mediaPlayerRepository.unPause();
            }
        }

        playbackService.setNotificationSongInfo(getCurrentSongArtist(), getCurrentSongTitle());
        Log.d("Play", "Same Song");
        songRepository.setCurrentSongId(getCurrentSong().id);
    }

    @Override
    public void playCurrentSong() throws IOException {
        Log.d("Play", "Change song");
        playbackService.setNotificationSongInfo(getCurrentSongArtist(), getCurrentSongTitle());
        mediaPlayerRepository.play(songRepository.getSongUri(getCurrentSong().id));
    }

    @Override
    public void next() throws IOException {
        songPosInPlaylist++;

        // TODO make logic for "repeat" functionality when go to the end of list,
        // for now playback will begin from start
        if (songPosInPlaylist >= playlist.songsId.size()) {
            songPosInPlaylist = 0;
        }

        playCurrentSong();

        playbackService.sendInfoUpdate();
    }

    @Override
    public void previous() throws IOException {
        songPosInPlaylist--;

        // TODO make logic for "repeat" functionality when go to the begin of list,
        // for now playback will begin from the end
        if (songPosInPlaylist < 0) {
            songPosInPlaylist = playlist.songsId.size() - 1;
        }

        playCurrentSong();

        playbackService.sendInfoUpdate();
    }

    @Override
    public void rewindTo(int positionMSec) {
        mediaPlayerRepository.rewindTo(positionMSec);
    }

    @Override
    public void stop() {
        stopPlaybackService();

        mediaPlayerRepository.release();
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayerRepository.isPlaying();
    }

    @Override
    public Bitmap getAlbumArt(long songAlbumId) {
        Bitmap bitmap = songRepository.getAlbumArt(songAlbumId);
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(App.getInstance().getResources(),
                    R.drawable.default_song_picture);
        }
        return ImageUtil.getInstance().getScaledBitmap1to1(App.getInstance(), bitmap);
    }

    @Override
    public Bitmap getCurrentSongAlbumArt() {
        return getAlbumArt(getCurrentSong().albumId);
    }

    @Override
    public String getCurrentSongFormatTotalDuration() {
        return TimeUtil.getInstance().formatTime(getCurrentSong().duration);
    }

    @Override
    public String getCurrentSongFormatPlayedDuration() {
        return TimeUtil.getInstance().formatTime(mediaPlayerRepository.getCurrentSongTimePlayed());
    }

    @Override
    public int getCurrentSongPlayedDuration() {
        return mediaPlayerRepository.getCurrentSongTimePlayed();
    }

    @Override
    public String getCurrentSongArtist() {
        return getCurrentSong().artist;
    }

    @Override
    public String getCurrentSongTitle() {
        return getCurrentSong().title;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        try {
            next();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    private Song getCurrentSong() {
        return songRepository.getSongByID(playlist.songsId.get(songPosInPlaylist));
    }
}
