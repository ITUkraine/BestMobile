package itukraine.com.ua.bestmobile.interactor.impl;

import android.util.Log;

import itukraine.com.ua.bestmobile.interactor.FeedbackInteractor;

/**
 * Created by User on 11.01.2016.
 */
public class FeedbackInteractorImpl implements FeedbackInteractor {


    private static final String TAG = FeedbackInteractorImpl.class.getCanonicalName();

    @Override
    public void sendFeedback(String message, float rating) {
        Log.i(TAG, "Rating: " + rating + " Message: " + message);
    }
}
