package itukraine.com.ua.bestmobile.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import itukraine.com.ua.bestmobile.R;
import itukraine.com.ua.bestmobile.interactor.PlayerInteractor;
import itukraine.com.ua.bestmobile.interactor.impl.PlayerInteractorImpl;
import itukraine.com.ua.bestmobile.ui.activity.MainActivity;

public class PlaybackService extends Service {

    public static final String PLAYBACK_PROGRESS_UPDATE = PlaybackService.class.getCanonicalName() + "PROGRESS_UPDATE";
    public static final String PLAYBACK_INFO_UPDATE = PlaybackService.class.getCanonicalName() + "INFO_UPDATE";
    public static final String EXTRA_PROGRESS = "EXTRA_PROGRESS";
    public static final String EXTRA_INFO_CHANGED = "EXTRA_INFO_CHANGED";
    private final static String TAG = PlaybackService.class.getSimpleName();
    private final IBinder mPlaybackBinder = new PlaybackBinder();
    private final int NOTIFICATION_ID = 281215;
    MediaPlayer mMediaPlayer = null;
    private int songPos;

    private NotificationManager mNM;
    private LocalBroadcastManager broadcaster;

    private PlayerInteractor playerInteractor;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mPlaybackBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        playerInteractor.stop();
        return false;
    }

    @Override
    public void onCreate() {
        //create player
        playerInteractor = new PlayerInteractorImpl();

        Log.i(TAG, "Service created");

        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        broadcaster = LocalBroadcastManager.getInstance(this);
    }

    public void showNotification() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        // Set the info for the views that show in the notification panel.
        Notification notification = new Notification.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.default_song_picture))
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setContentTitle(playerInteractor.getCurrentSongArtist())  // the artist
                .setContentText(playerInteractor.getCurrentSongTitle())  // the song title
                .setContentIntent(contentIntent)// The intent to send when the entry is clicked
                .setWhen(0)
                .setOngoing(true) // user can't close notification
                .build();
        // Send the notification.
        mNM.notify(NOTIFICATION_ID, notification);
    }

    public void hideNotification() {
        mNM.cancel(NOTIFICATION_ID);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // display song info at first run
        sendInfoUpdate();

        showNotification();

        return super.onStartCommand(intent, flags, startId);
    }

    private void sendProgressUpdate() {
        Intent updateIntent = new Intent(PLAYBACK_PROGRESS_UPDATE);
        updateIntent.putExtra(EXTRA_PROGRESS, true);
        broadcaster.sendBroadcast(updateIntent);
    }

    private void sendInfoUpdate() {
        Intent updateIntent = new Intent(PLAYBACK_INFO_UPDATE);
        updateIntent.putExtra(EXTRA_INFO_CHANGED, true);
        broadcaster.sendBroadcast(updateIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        playerInteractor.stop();
        hideNotification();
        stopForeground(true);
        Log.i(TAG, "Service destroyed");
    }

    private void runAsyncProgressUpdate() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                while (MainActivity.isActive && playerInteractor.isPlaying()) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (MainActivity.isActive) {
                        sendProgressUpdate();
                    }
                }
                return null;
            }
        }.execute();
    }

    public class PlaybackBinder extends Binder {
        public PlaybackService getService() {
            return PlaybackService.this;
        }
    }
}
