package itukraine.com.ua.bestmobile.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import itukraine.com.ua.bestmobile.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayerFragment extends Fragment {

    private static final String TAG = PlayerFragment.class.getCanonicalName();

    public PlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player, container, false);
        final Button buttonPlay = (Button) view.findViewById(R.id.button_play);
        final Button buttonPause = (Button) view.findViewById(R.id.button_pause);
        final ProgressBar songProgressbar = (ProgressBar) view.findViewById(R.id.song_progressbar);

        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPlay.setVisibility(View.GONE);
                buttonPause.setVisibility(View.VISIBLE);
            }
        });

        buttonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPlay.setVisibility(View.VISIBLE);
                buttonPause.setVisibility(View.GONE);
            }
        });

        return view;
    }

}
