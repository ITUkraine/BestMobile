package itukraine.com.ua.bestmobile.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.List;

import itukraine.com.ua.bestmobile.MainActivity;
import itukraine.com.ua.bestmobile.R;
import itukraine.com.ua.bestmobile.dao.Song;

public class PlaybackService extends Service implements
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    public static final String PLAYBACK_PROGRESS_UPDATE = PlaybackService.class.getCanonicalName() + "PROGRESS_UPDATE";
    public static final String PLAYBACK_INFO_UPDATE = PlaybackService.class.getCanonicalName() + "INFO_UPDATE";
    public static final String EXTRA_PROGRESS = "EXTRA_PROGRESS";
    public static final String EXTRA_INFO_CHANGED = "EXTRA_INFO_CHANGED";
    private final static String TAG = PlaybackService.class.getSimpleName();
    private final IBinder mPlaybackBinder = new PlaybackBinder();
    private final int NOTIFICATION_ID = 281215;
    MediaPlayer mMediaPlayer = null;
    private int songPos;
    private List<Song> mSongs;

    private NotificationManager mNM;
    private LocalBroadcastManager broadcaster;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mPlaybackBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mMediaPlayer.stop();
        mMediaPlayer.release();
        return false;
    }

    @Override
    public void onCreate() {
        songPos = 0;
        //create player
        mMediaPlayer = new MediaPlayer();

        initMusicPlayer();
        Log.i(TAG, "Service created");

        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        broadcaster = LocalBroadcastManager.getInstance(this);
    }

    private void showNotification() {
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        // Set the info for the views that show in the notification panel.
        Notification notification = new Notification.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.default_song_picture))
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setContentTitle(getCurrentSong().artist)  // the artist
                .setContentText(getCurrentSong().title)  // the song title
                .setContentIntent(contentIntent)// The intent to send when the entry is clicked
                .setWhen(0)
                .build();
        // Send the notification.
        mNM.notify(NOTIFICATION_ID, notification);
    }

    public void initMusicPlayer() {
        mMediaPlayer.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnErrorListener(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // display song info at first run
        sendInfoUpdate();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //start playback
        mp.start();

        sendInfoUpdate();

        runAsyncProgressUpdate();

        Log.i(TAG, "Service prepared");
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.i(TAG, "Song completed");

        nextSong();
    }

    private void sendProgressUpdate() {
        Intent updateIntent = new Intent(PLAYBACK_PROGRESS_UPDATE);
        updateIntent.putExtra(EXTRA_PROGRESS, mMediaPlayer.getCurrentPosition());
        broadcaster.sendBroadcast(updateIntent);
    }

    private void sendInfoUpdate() {
        Intent updateIntent = new Intent(PLAYBACK_INFO_UPDATE);
        updateIntent.putExtra(EXTRA_INFO_CHANGED, true);
        broadcaster.sendBroadcast(updateIntent);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        // error
        return false;
    }

    public void setSongs(List<Song> songs) {
        this.mSongs = songs;
    }

    public void pauseSong() {
        mMediaPlayer.pause();
        mNM.cancel(NOTIFICATION_ID);
        stopForeground(true);
    }

    public void playSong() {
        showNotification();
        mMediaPlayer.reset();
        //get song
        Song playSong = mSongs.get(songPos);
        //get id
        long currSong = playSong.id;
        //set uri
        Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currSong);

        try {
            mMediaPlayer.setDataSource(getApplicationContext(), trackUri);
        } catch (Exception e) {
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }


        Log.i(TAG, String.format("Play %d from %d : %s", songPos, mSongs.size() - 1, playSong));

        mMediaPlayer.prepareAsync();
    }

    public void continueSong() {
        mMediaPlayer.start();

        runAsyncProgressUpdate();
    }

    public void nextSong() {
        songPos++;

        // TODO make logic for "repeat" functionality when go to the end of list,
        // for now playback will begin from start
        if (songPos >= mSongs.size()) {
            songPos = 0;
        }

        playSong();
    }

    public void prevSong() {
        songPos--;

        // TODO make logic for "repeat" functionality when go to the begin of list,
        // for now playback will begin from the end
        if (songPos < 0) {
            songPos = mSongs.size() - 1;
        }

        playSong();
    }

    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    public Song getCurrentSong() {
        return mSongs.get(songPos);
    }

    public int getCurrentTime() {
        return mMediaPlayer.getCurrentPosition();
    }

    /**
     * Rewind current song to certain position.
     *
     * @param position Time in song to which song will be rewound
     */
    public void rewindTo(int position) {
        mMediaPlayer.seekTo(position);
    }

    /**
     * Choose what action perform when song wasn't played yet,
     * was at pause or have played for a some period of time already.
     */
    public void smartPlay() {
        if (isPlaying()) {
            pauseSong();
        } else {
            if (getCurrentTime() > getCurrentSong().duration) {
                playSong();
            } else {
                continueSong();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mNM.cancel(NOTIFICATION_ID);
        stopForeground(true);
        if (mMediaPlayer != null) mMediaPlayer.release();
        Log.i(TAG, "Service destroyed");
    }

    private void runAsyncProgressUpdate() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                while (isPlaying()) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    sendProgressUpdate();
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
