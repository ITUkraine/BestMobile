package itukraine.com.ua.bestmobile.presenter;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import java.io.IOException;

import itukraine.com.ua.bestmobile.App;
import itukraine.com.ua.bestmobile.R;
import itukraine.com.ua.bestmobile.interactor.PlayerInteractor;
import itukraine.com.ua.bestmobile.interactor.PlayerInteractorImpl;
import itukraine.com.ua.bestmobile.service.PlaybackService;
import itukraine.com.ua.bestmobile.ui.activity.MainView;
import itukraine.com.ua.bestmobile.util.ImageUtil;

public class MainPresenterImpl implements MainPresenter {

    private MainView mainView;

    private PlayerInteractor playerInteractor;

    private Intent mPlayIntent;

    private ServiceConnection playbackConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlaybackService.PlaybackBinder binder = (PlaybackService.PlaybackBinder) service;
            // GER INSTANCE OF SERVICE HERE  -> binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

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

        startPlaybackService();

        LocalBroadcastManager.getInstance(App.getInstance()).registerReceiver(receiverInfoUpdate,
                new IntentFilter(PlaybackService.PLAYBACK_INFO_UPDATE));


        // TODO OPEN "PlayerFragment"
    }

    @Override
    public void onDestroy() {
        stopPlaybackService();

        LocalBroadcastManager.getInstance(App.getInstance()).unregisterReceiver(receiverInfoUpdate);

        mainView = null;
    }

    @Override
    public void startPlaybackService() {
        if (mPlayIntent == null) {
            mPlayIntent = new Intent(App.getInstance(), PlaybackService.class);
            App.getInstance().bindService(mPlayIntent, playbackConnection, Context.BIND_AUTO_CREATE);
            App.getInstance().startService(mPlayIntent);
        }
    }

    @Override
    public void stopPlaybackService() {
        App.getInstance().unbindService(playbackConnection);
        App.getInstance().stopService(mPlayIntent);
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
