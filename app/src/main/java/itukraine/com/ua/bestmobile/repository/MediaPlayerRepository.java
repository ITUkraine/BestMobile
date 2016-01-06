package itukraine.com.ua.bestmobile.repository;

import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;

public interface MediaPlayerRepository {
    void play(Uri songUri) throws IOException;

    void pause();

    void unPause();

    void rewindTo(int positionMSec);

    boolean isPlaying();

    int getCurrentSongTimePlayed();

    void release();

    void setListeners(
            MediaPlayer.OnErrorListener onErrorListener,
            MediaPlayer.OnCompletionListener onCompletionListener);
}
