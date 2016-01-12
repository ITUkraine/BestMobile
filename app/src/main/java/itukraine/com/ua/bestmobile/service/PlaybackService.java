package itukraine.com.ua.bestmobile.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import itukraine.com.ua.bestmobile.R;
import itukraine.com.ua.bestmobile.ui.activity.MainActivity;

public class PlaybackService extends Service {

    public static final String PLAYBACK_PROGRESS_UPDATE = PlaybackService.class.getCanonicalName() + "PROGRESS_UPDATE";
    public static final String PLAYBACK_INFO_UPDATE = PlaybackService.class.getCanonicalName() + "INFO_UPDATE";
    public static final String EXTRA_PROGRESS = "EXTRA_PROGRESS";
    public static final String EXTRA_INFO_CHANGED = "EXTRA_INFO_CHANGED";
    private final static String TAG = PlaybackService.class.getSimpleName();
    private final IBinder mPlaybackBinder = new PlaybackBinder();
    private final int NOTIFICATION_ID = 281215;
    private Notification notification;

    private NotificationManager mNM;
    private LocalBroadcastManager broadcaster;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mPlaybackBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return false;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "Service created");

        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        broadcaster = LocalBroadcastManager.getInstance(this);
    }

    private void showNotification(String artist, String songTitle) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        // Set the info for the views that show in the notification panel.
        notification = new Notification.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.default_song_picture))
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setContentTitle(artist)  // the artist
                .setContentText(songTitle)  // the song title
                .setContentIntent(contentIntent)// The intent to send when the entry is clicked
                .setWhen(0)
                .setOngoing(true) // user can't close notification
                .build();
        // Send the notification.
        mNM.notify(NOTIFICATION_ID, notification);
    }

    private void hideNotification() {
        mNM.cancel(NOTIFICATION_ID);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // display song info at first run
        sendInfoUpdate();

        showNotification(null, null);

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

    public void setNotificationSongInfo(String artist, String songTitle) {
        showNotification(artist, songTitle);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideNotification();
        stopForeground(true);
        Log.i(TAG, "Service destroyed");
    }

    private void runAsyncProgressUpdate() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                while (MainActivity.isActive) {
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
