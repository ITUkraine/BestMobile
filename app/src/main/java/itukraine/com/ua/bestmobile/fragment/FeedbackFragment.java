package itukraine.com.ua.bestmobile.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import itukraine.com.ua.bestmobile.R;

public class FeedbackFragment extends Fragment {


    private static final String TAG = FeedbackFragment.class.getCanonicalName();
    private Button sendFeedback;
    private EditText feedbackMessage;
    private RatingBar feedbackRating;

    public FeedbackFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);

        feedbackRating = (RatingBar) view.findViewById(R.id.rating);

        feedbackMessage = (EditText) view.findViewById(R.id.feedback_msg);

        sendFeedback = (Button) view.findViewById(R.id.btn_send_feedback);
        sendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (feedbackMessage.getText().equals("")) {
                    Toast.makeText(getActivity(), "Write something", Toast.LENGTH_LONG).show();
                    return;
                }
                Log.i(TAG, "Rating: " + feedbackRating.getProgress() + " Message: " + feedbackMessage.getText());
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
