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
import itukraine.com.ua.bestmobile.R;
import itukraine.com.ua.bestmobile.interactor.PlayerInteractor;
import itukraine.com.ua.bestmobile.interactor.impl.PlayerInteractorImpl;
import itukraine.com.ua.bestmobile.presenter.MainPresenter;
import itukraine.com.ua.bestmobile.service.PlaybackService;
import itukraine.com.ua.bestmobile.ui.activity.view.MainView;
import itukraine.com.ua.bestmobile.util.ImageUtil;

public class MainPresenterImpl implements MainPresenter {

    private MainView mainView;

    private PlayerInteractor playerInteractor;

    private BroadcastReceiver receiverInfoUpdate = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean infoUpdate = intent.getBooleanExtra(PlaybackService.EXTRA_INFO_CHANGED, false);
            if (infoUpdate) {
                updateNavigationHeaderSongInfo();
                updateNavigationHeaderControls();
            }
        }
    };

    public MainPresenterImpl(MainView view) {
        this.mainView = view;
        playerInteractor = new PlayerInteractorImpl();

        LocalBroadcastManager.getInstance(App.getInstance()).registerReceiver(receiverInfoUpdate,
                new IntentFilter(PlaybackService.PLAYBACK_INFO_UPDATE));


        // TODO OPEN "PlayerFragment"
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(App.getInstance()).unregisterReceiver(receiverInfoUpdate);

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
        if (bitmap == null) {
            art = App.getInstance().getResources().getDrawable(R.drawable.default_song_picture);
        } else {
            art = new BitmapDrawable(
                    App.getInstance().getResources(),
                    ImageUtil.getInstance().getScaledBitmap1to1(App.getInstance(), bitmap));
        }

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
}
