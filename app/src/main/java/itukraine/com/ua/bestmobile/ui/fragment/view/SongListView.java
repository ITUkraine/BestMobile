package itukraine.com.ua.bestmobile.ui.fragment.view;

import android.support.v4.app.Fragment;

import itukraine.com.ua.bestmobile.ui.adapter.SongAdapter;

public interface SongListView {

    void setAdapter(SongAdapter adapter);

    void openFragmentWithBackStack(Fragment fragment);

    void callBackStack();

    void displayButtonAddSongsToPlaylist(boolean isDisplayed);

    String getSearchQuery();

    void displayClearSearchButton(boolean isDisplayed);

    void clearSearchQuery();
}
