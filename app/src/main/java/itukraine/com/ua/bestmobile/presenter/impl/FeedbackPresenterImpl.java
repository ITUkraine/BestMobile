package itukraine.com.ua.bestmobile.presenter.impl;

import itukraine.com.ua.bestmobile.interactor.FeedbackInteractor;
import itukraine.com.ua.bestmobile.interactor.impl.FeedbackInteractorImpl;
import itukraine.com.ua.bestmobile.presenter.FeedbackPresenter;
import itukraine.com.ua.bestmobile.ui.fragment.view.FeedbackView;

/**
 * Created by User on 11.01.2016.
 */
public class FeedbackPresenterImpl implements FeedbackPresenter {

    private FeedbackView feedbackView;
    private FeedbackInteractor feedbackInteractor;

    public FeedbackPresenterImpl(FeedbackView feedbackView) {
        this.feedbackView = feedbackView;
        this.feedbackInteractor = new FeedbackInteractorImpl();
    }

    @Override
    public void sendFeedback() {
        String feedbackMessage = feedbackView.getFeedbackMessage();
        float feedbackRating = feedbackView.getFeedbackRating();
        feedbackInteractor.sendFeedback(feedbackMessage, feedbackRating);
    }
}
