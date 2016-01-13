package itukraine.com.ua.bestmobile.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import itukraine.com.ua.bestmobile.R;
import itukraine.com.ua.bestmobile.presenter.FeedbackPresenter;
import itukraine.com.ua.bestmobile.presenter.impl.FeedbackPresenterImpl;
import itukraine.com.ua.bestmobile.ui.activity.view.MainView;
import itukraine.com.ua.bestmobile.ui.fragment.view.FeedbackView;

public class FeedbackFragment extends Fragment implements FeedbackView {

    private FeedbackPresenter feedbackPresenter;

    private Button sendFeedback;
    private EditText feedbackMessage;
    private RatingBar feedbackRating;

    public FeedbackFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);

        ((MainView) getActivity()).setToolbarTitle(getResources().getString(R.string.feedback));

        initFeedbackViews(view);

        feedbackPresenter = new FeedbackPresenterImpl(this);

        return view;
    }

    private void initFeedbackViews(View view) {
        feedbackRating = (RatingBar) view.findViewById(R.id.rating);
        feedbackMessage = (EditText) view.findViewById(R.id.feedback_msg);
        sendFeedback = (Button) view.findViewById(R.id.btn_send_feedback);

        sendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedbackPresenter.sendFeedback();
            }
        });
    }

    @Override
    public String getFeedbackMessage() {
        return feedbackMessage.getText().toString();
    }

    @Override
    public float getFeedbackRating() {
        return feedbackRating.getRating();
    }
}
