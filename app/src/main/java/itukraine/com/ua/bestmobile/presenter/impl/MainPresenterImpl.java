package itukraine.com.ua.bestmobile.presenter.impl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.LocalBroadcastManager;

import java.io.IOException;

import itukraine.com.ua.bestmobile.App;
import itukraine.com.ua.bestmobile.interactor.AllPlaylistInteractor;
import itukraine.com.ua.bestmobile.interactor.PlayerInteractor;
import itukraine.com.ua.bestmobile.interactor.impl.AllPlaylistInteractorImpl;
import itukraine.com.ua.bestmobile.interactor.impl.PlayerInteractorImpl;
import itukraine.com.ua.bestmobile.presenter.MainPresenter;
import itukraine.com.ua.bestmobile.service.PlaybackService;
import itukraine.com.ua.bestmobile.ui.activity.view.MainView;
import itukraine.com.ua.bestmobile.ui.fragment.PlayerFragment;

public class MainPresenterImpl implements MainPresenter {

    private MainView mainView;

    private PlayerInteractor playerInteractor;
    private AllPlaylistInteractor allPlaylistInteractor;

    private BroadcastReceiver receiverInfoUpdate = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateNavigationHeaderSongInfo();
            updateNavigationHeaderControls();
        }
    };

    public MainPresenterImpl(MainView view) {
        this.mainView = view;
        playerInteractor = PlayerInteractorImpl.getInstance();
        allPlaylistInteractor = new AllPlaylistInteractorImpl();

        LocalBroadcastManager.getInstance(App.getInstance()).registerReceiver(receiverInfoUpdate,
                new IntentFilter(PlaybackService.PLAYBACK_INFO_UPDATE));

        mainView.openFragment(new PlayerFragment());
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(App.getInstance()).unregisterReceiver(receiverInfoUpdate);
        playerInteractor.stop();
        mainView = null;
    }

    @Override
    public void updateNavigationHeaderControls() {
        if (playerInteractor.isPlaying()) {
            mainView.setNavigationHeaderPlayButtonDrawable(android.R.drawable.ic_media_pause);
        } else {
            mainView.setNavigationHeaderPlayButtonDrawable(android.R.drawable.ic_media_play);
        }
    }

    @Override
    public void updateNavigationHeaderSongInfo() {
        Drawable art;
        Bitmap bitmap = playerInteractor.getCurrentSongAlbumArt();

        art = new BitmapDrawable(App.getInstance().getResources(), bitmap);
        art.setColorFilter(Color.argb(50, 155, 155, 155), PorterDuff.Mode.SRC_ATOP);

        mainView.setNavigationHeaderBackgroundDrawable(art);
    }

    @Override
    public void onHeaderPlayButtonPressed() {
        updateNavigationHeaderControls();

        try {
            playerInteractor.play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onHeaderPreviousButtonPressed() {
        try {
            playerInteractor.previous();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onHeaderNextButtonPressed() {
        try {
            playerInteractor.next();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updatePlaylists() {
        allPlaylistInteractor.updatePlaylists();
    }
}
