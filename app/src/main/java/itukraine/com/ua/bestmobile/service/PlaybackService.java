package itukraine.com.ua.bestmobile.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class PlaybackService extends Service {

    private final static String TAG = PlaybackService.class.getSimpleName();
    private final IBinder mPlaybackBinder = new PlaybackBinder();

    private SongInfoNotification notification;

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

        notification = new SongInfoNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notification.showNotification();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        notification.hideNotification();
        stopForeground(true);
        Log.i(TAG, "Service destroyed");
    }

    public SongInfoNotification getNotification() {
        return notification;
    }

    public class PlaybackBinder extends Binder {
        public PlaybackService getService() {
            return PlaybackService.this;
        }
    }
}
