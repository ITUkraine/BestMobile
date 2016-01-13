package itukraine.com.ua.bestmobile.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.util.Log;

import itukraine.com.ua.bestmobile.R;
import itukraine.com.ua.bestmobile.ui.activity.MainActivity;

public class PlaybackService extends Service {

    private final static String TAG = PlaybackService.class.getSimpleName();
    private final IBinder mPlaybackBinder = new PlaybackBinder();
    private final int NOTIFICATION_ID = 281215;

    private NotificationManager mNM;

    // NOTIFICATION CONTENT
    private String artist;
    private String title;
    private int smallIconId;

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
    }

    private void showNotification() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        if (smallIconId == 0) {
            smallIconId = android.R.drawable.ic_media_pause;
        }

        // Set the info for the views that show in the notification panel.
        Notification notification = new Notification.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.default_song_picture))
                .setSmallIcon(smallIconId)
                .setContentTitle(artist)  // the artist
                .setContentText(title)  // the song title
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
        showNotification();

        return super.onStartCommand(intent, flags, startId);
    }

    public void setNotificationSongInfo(String artist, String songTitle) {
        this.artist = artist;
        this.title = songTitle;

        showNotification();
    }

    public void setNotificationSmallIcon(@DrawableRes int iconId) {
        this.smallIconId = iconId;

        showNotification();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideNotification();
        stopForeground(true);
        Log.i(TAG, "Service destroyed");
    }

    public class PlaybackBinder extends Binder {
        public PlaybackService getService() {
            return PlaybackService.this;
        }
    }
}
