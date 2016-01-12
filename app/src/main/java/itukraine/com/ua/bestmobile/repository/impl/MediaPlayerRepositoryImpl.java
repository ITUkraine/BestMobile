package itukraine.com.ua.bestmobile.repository.impl;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.PowerManager;

import java.io.IOException;

import itukraine.com.ua.bestmobile.App;
import itukraine.com.ua.bestmobile.repository.MediaPlayerRepository;

public class MediaPlayerRepositoryImpl implements MediaPlayerRepository {
    private static MediaPlayerRepository instance;
    private static MediaPlayer mediaPlayer;

    private MediaPlayerRepositoryImpl() {
        mediaPlayer = new MediaPlayer();
        initMusicPlayer();
    }

    public static MediaPlayerRepository getInstance() {
        if (instance == null) {
            instance = new MediaPlayerRepositoryImpl();
        }
        return instance;
    }

    public void initMusicPlayer() {
        mediaPlayer.setWakeMode(App.getInstance(), PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    @Override
    public void play(Uri songUri) throws IOException {
        mediaPlayer.reset();
        mediaPlayer.setDataSource(App.getInstance(), songUri);
        mediaPlayer.prepareAsync();
    }

    @Override
    public void pause() {
        mediaPlayer.pause();
    }

    @Override
    public void unPause() {
        mediaPlayer.start();
    }

    @Override
    public void rewindTo(int positionMSec) {
        mediaPlayer.seekTo(positionMSec);
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public int getCurrentSongTimePlayed() {
        return mediaPlayer.getCurrentPosition();
    }

    @Override
    public void release() {
        if (mediaPlayer != null) {
            stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void stop() {
        mediaPlayer.stop();
    }

    @Override
    public void start(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }

    @Override
    public void setListeners(
            MediaPlayer.OnErrorListener onErrorListener,
            MediaPlayer.OnCompletionListener onCompletionListener,
            MediaPlayer.OnPreparedListener onPreparedListener) {

        mediaPlayer.setOnCompletionListener(onCompletionListener);
        mediaPlayer.setOnErrorListener(onErrorListener);
        mediaPlayer.setOnPreparedListener(onPreparedListener);
    }
}
