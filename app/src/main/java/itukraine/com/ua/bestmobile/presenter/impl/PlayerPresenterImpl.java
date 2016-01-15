package itukraine.com.ua.bestmobile.presenter.impl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.support.v4.content.LocalBroadcastManager;

import java.io.IOException;

import itukraine.com.ua.bestmobile.App;
import itukraine.com.ua.bestmobile.Constants;
import itukraine.com.ua.bestmobile.R;
import itukraine.com.ua.bestmobile.enums.RepeatState;
import itukraine.com.ua.bestmobile.enums.ShuffleState;
import itukraine.com.ua.bestmobile.interactor.PlayerInteractor;
import itukraine.com.ua.bestmobile.interactor.impl.PlayerInteractorImpl;
import itukraine.com.ua.bestmobile.presenter.PlayerPresenter;
import itukraine.com.ua.bestmobile.ui.fragment.view.PlayerView;

public class PlayerPresenterImpl implements PlayerPresenter {

    private PlayerView playerView;
    private PlayerInteractor playerInteractor;

    private BroadcastReceiver receiverProgressUpdate = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (playerInteractor.isPlaying()) {
                updateSongProgress();
            }
        }
    };

    private BroadcastReceiver receiverInfoUpdate = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateSongInfo();
        }
    };

    public PlayerPresenterImpl(PlayerView view) {
        this.playerView = view;

        playerInteractor = PlayerInteractorImpl.getInstance();

        LocalBroadcastManager.getInstance(App.getInstance()).registerReceiver(receiverInfoUpdate,
                new IntentFilter(Constants.SONG_INFO_UPDATE));
    }

    @Override
    public void onPlayButtonPressed() {
        updatePlayButton();

        try {
            playerInteractor.play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNextButtonPressed() {
        playerView.clearDurationProgress();

        playerView.setPlayButtonDrawable(R.drawable.ic_pause);

        try {
            playerInteractor.next();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPreviousButtonPressed() {
        playerView.clearDurationProgress();

        playerView.setPlayButtonDrawable(R.drawable.ic_pause);

        try {
            playerInteractor.previous();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateSongInfo() {

        playerView.setSongInfo(
                playerInteractor.getCurrentSongArtist(),
                playerInteractor.getCurrentSongTitle(),
                playerInteractor.getCurrentSongTotalDuration());

        Bitmap songAlbumArt = playerInteractor.getCurrentSongAlbumArt();
        playerView.setAlbumImage(songAlbumArt);

        playerView.clearDurationProgress();
    }

    @Override
    public void updateSongProgress() {
        playerView.setCurrentSongPlayedDuration(playerInteractor.getCurrentSongPlayedDuration());
    }

    @Override
    public void updatePlayButton() {
        if (!playerInteractor.isPlaying()) {
            playerView.setPlayButtonDrawable(R.drawable.ic_pause);
        } else {
            playerView.setPlayButtonDrawable(R.drawable.ic_play);
        }
    }

    @Override
    public void onCreate() {
        LocalBroadcastManager.getInstance(App.getInstance()).registerReceiver(receiverProgressUpdate,
                new IntentFilter(Constants.SONG_PROGRESS_UPDATE));
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(App.getInstance()).unregisterReceiver(receiverProgressUpdate);
        LocalBroadcastManager.getInstance(App.getInstance()).unregisterReceiver(receiverInfoUpdate);
    }

    @Override
    public void rewindTo(int pos) {
        playerInteractor.rewindTo(pos);
    }

    @Override
    public void onShuffleModePressed() {
        ShuffleState currentShuffleState = playerInteractor.getShuffleMode();
        if (currentShuffleState == ShuffleState.SHUFFLE_OFF) {
            playerView.setShuffleButtonDrawable(R.drawable.ic_shuffle_on);
            playerInteractor.setShuffleMode(ShuffleState.SHUFFLE_ON);
            playerInteractor.shufflePlaylist();
        } else {
            playerView.setShuffleButtonDrawable(R.drawable.ic_shuffle_off);
            playerInteractor.setShuffleMode(ShuffleState.SHUFFLE_OFF);
            playerInteractor.revertPlaylistToOriginal();
        }

    }

    @Override
    public void onRepeatModePressed() {
        RepeatState currentRepeatState = playerInteractor.getRepeatMode();
        switch (currentRepeatState) {
            case REPEAT_OFF:
                playerView.setRepeatButtonDrawable(R.drawable.ic_repeat_on);
                playerInteractor.setRepeatMode(RepeatState.REPEAT_PLAYLIST);
                break;
            case REPEAT_PLAYLIST:
                playerView.setRepeatButtonDrawable(R.drawable.ic_repeat_on_song);
                playerInteractor.setRepeatMode(RepeatState.REPEAT_SONG);
                break;
            case REPEAT_SONG:
                playerView.setRepeatButtonDrawable(R.drawable.ic_repeat_off);
                playerInteractor.setRepeatMode(RepeatState.REPEAT_OFF);
                break;
        }
    }
}
