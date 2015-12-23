package itukraine.com.ua.bestmobile.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import itukraine.com.ua.bestmobile.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayerFragment extends Fragment {


    public PlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);


        return inflater.inflate(R.layout.fragment_player, container, false);
    }

}
