package itukraine.com.ua.bestmobile.interactor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;

import java.io.IOException;
import java.util.List;

import itukraine.com.ua.bestmobile.App;
import itukraine.com.ua.bestmobile.R;
import itukraine.com.ua.bestmobile.entity.Playlist;
import itukraine.com.ua.bestmobile.entity.Song;
import itukraine.com.ua.bestmobile.repository.MediaPlayerRepository;
import itukraine.com.ua.bestmobile.repository.PlaylistRepository;
import itukraine.com.ua.bestmobile.repository.SongRepository;
import itukraine.com.ua.bestmobile.repository.impl.MediaPlayerRepositoryImpl;
import itukraine.com.ua.bestmobile.repository.impl.PlaylistRepositoryImpl;
import itukraine.com.ua.bestmobile.repository.impl.SongRepositoryImpl;
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

    public PlayerInteractorImpl() {
        songRepository = new SongRepositoryImpl(App.getInstance());
        playlistRepository = new PlaylistRepositoryImpl(App.getInstance());

        mediaPlayerRepository = MediaPlayerRepositoryImpl.getInstance();
        mediaPlayerRepository.setListeners(this, this);

        loadLatestPlayedPlaylist();
        loadLatestPlayedSong();
    }

    @Override
    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;

        playlistRepository.setCurrentPlaylistName(playlist.name);

        songPosInPlaylist = 0;
    }

    @Override
    public void setCurrentSongPosition(int position) {
        songPosInPlaylist = position;
    }

    @Override
    public void loadLatestPlayedPlaylist() {
        // TODO REWRITE LOGIC ABOUT "ALL SONGS" PLAYLIST
        Playlist playlist;
        String currentPlaylist = playlistRepository.getCurrentPlaylistName();
        if (currentPlaylist == null
                || currentPlaylist.equals(App.getInstance().getResources().getString(R.string.all_songs_playlist_name))) {
            playlist = new Playlist("All songs");
            List<Song> songs = songRepository.getAllSongs();
            playlist.songsId.add(songs.get(0).id);
        } else {
            playlist = playlistRepository.findPlaylistByName(currentPlaylist);
        }
        setPlaylist(playlist);
    }

    @Override
    public void loadLatestPlayedSong() {
        long currentSongId = songRepository.getCurrentSongId();
        if (currentSongId == -1) {
            setCurrentSongPosition(0);
        } else {
            setCurrentSongPosition(
                    playlistRepository.getSongPositionInPlaylistById(playlist, currentSongId));
        }
    }

    @Override
    public void play() throws IOException {
        if (mediaPlayerRepository.isPlaying()) {
            mediaPlayerRepository.pause();
        } else {
            if (mediaPlayerRepository.getCurrentSongTimePlayed() > getCurrentSong().duration) {
                mediaPlayerRepository.play(songRepository.getSongUri(getCurrentSong().id));
            } else {
                mediaPlayerRepository.unPause();
            }
        }

        songRepository.setCurrentSongId(getCurrentSong().id);
    }

    @Override
    public void next() throws IOException {
        songPosInPlaylist++;

        // TODO make logic for "repeat" functionality when go to the end of list,
        // for now playback will begin from start
        if (songPosInPlaylist >= playlist.songsId.size()) {
            songPosInPlaylist = 0;
        }

        play();
    }

    @Override
    public void previous() throws IOException {
        songPosInPlaylist--;

        // TODO make logic for "repeat" functionality when go to the begin of list,
        // for now playback will begin from the end
        if (songPosInPlaylist < 0) {
            songPosInPlaylist = playlist.songsId.size() - 1;
        }

        play();
    }

    @Override
    public void rewindTo(int positionMSec) {
        mediaPlayerRepository.rewindTo(positionMSec);
    }

    @Override
    public void stop() {
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
