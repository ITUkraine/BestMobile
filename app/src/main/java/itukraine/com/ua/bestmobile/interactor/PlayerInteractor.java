package itukraine.com.ua.bestmobile.interactor;

import android.graphics.Bitmap;

import java.io.IOException;

import itukraine.com.ua.bestmobile.entity.Playlist;
import itukraine.com.ua.bestmobile.enums.RepeatState;
import itukraine.com.ua.bestmobile.enums.ShuffleState;

public interface PlayerInteractor {

    void setPlaylist(Playlist playlist);

    void setPlaylist(String playlistName);

    void updateDefaultPlaylist();

    void setCurrentSongPosition(int position);

    void loadLatestPlayedPlaylist();

    void loadLatestPlayedSong();

    void play() throws IOException;

    void playCurrentSong() throws IOException;

    void next() throws IOException;

    void previous() throws IOException;

    void rewindTo(int positionMSec);

    void stop();

    boolean isPlaying();

    Bitmap getAlbumArt(long songAlbumId);

    Bitmap getCurrentSongAlbumArt();

    int getCurrentSongPlayedDuration();

    String getCurrentSongArtist();

    String getCurrentSongTitle();

    int getCurrentSongTotalDuration();

    ShuffleState getShuffleMode();

    void setShuffleMode(ShuffleState state);

    void shufflePlaylist();

    void revertPlaylistToOriginal();

    RepeatState getRepeatMode();

    void setRepeatMode(RepeatState repeatMode);
}
