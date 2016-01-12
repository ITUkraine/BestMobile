package itukraine.com.ua.bestmobile.async;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import itukraine.com.ua.bestmobile.App;
import itukraine.com.ua.bestmobile.Constants;

public class SendProgressUpdateBroadcastAsync extends AsyncTask<Void, Void, Void> {

    private final LocalBroadcastManager broadcaster;

    public SendProgressUpdateBroadcastAsync() {
        broadcaster = LocalBroadcastManager.getInstance(App.getInstance());
    }

    @Override
    protected Void doInBackground(Void... params) {

        Log.e("ASYNC", "START");

        while (true) {
            Intent updateIntent = new Intent(Constants.SONG_PROGRESS_UPDATE);
            broadcaster.sendBroadcast(updateIntent);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (isCancelled()) {
                Log.e("ASYNC", "STOP");
                return null;
            }
        }
    }
}
