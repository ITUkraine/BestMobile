package itukraine.com.ua.bestmobile.ui.activity;

import android.view.View;

public interface MainView {

    void hideKeyboard(View view);

    void updateNavigationHeaderControls();

    void clearBackStack();

    void sendCurrentSongDurationToFragment(int durationPosition);

    void sendCurrentSongUpdateRequestToFragment();

}
