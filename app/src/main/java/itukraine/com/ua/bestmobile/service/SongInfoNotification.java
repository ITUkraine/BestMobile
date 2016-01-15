package itukraine.com.ua.bestmobile.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;
import android.util.DisplayMetrics;

import itukraine.com.ua.bestmobile.App;
import itukraine.com.ua.bestmobile.R;
import itukraine.com.ua.bestmobile.ui.activity.MainActivity;

/**
 * Created by User on 15.01.2016.
 */
public class SongInfoNotification {

    private final int NOTIFICATION_ID = 281215;

    private NotificationManager mNM;

    // NOTIFICATION CONTENT
    private String artist;
    private String title;
    private int smallIconId;

    public SongInfoNotification() {
        mNM = (NotificationManager) App.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void showNotification() {
        Intent notificationIntent = new Intent(App.getInstance(), MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(App.getInstance(), 0,
                notificationIntent, 0);

        if (smallIconId == 0) {
            smallIconId = android.R.drawable.ic_media_pause;
        }

        // Set the info for the views that show in the notification panel.
        Notification notification = new Notification.Builder(App.getInstance())
                .setLargeIcon(getScaledLargeIcon())
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

    public void hideNotification() {
        mNM.cancel(NOTIFICATION_ID);
    }

    private Bitmap getScaledLargeIcon() {
        DisplayMetrics metrics = App.getInstance().getResources().getDisplayMetrics();
        float iconSize = 65 * metrics.density;
        return Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(
                        App.getInstance().getResources(),
                        R.drawable.default_song_picture),
                (int) iconSize,
                (int) iconSize,
                true);
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

}
