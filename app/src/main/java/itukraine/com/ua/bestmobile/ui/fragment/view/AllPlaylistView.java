package itukraine.com.ua.bestmobile.ui.fragment.view;

import android.support.v4.app.Fragment;

import itukraine.com.ua.bestmobile.ui.adapter.PlaylistAdapter;

public interface AllPlaylistView {

    void setAdapter(PlaylistAdapter adapter);

    String getSearchQuery();

    void clearSearchQuery();

    void displayClearSearchButton(boolean isDisplayed);

    void displayErrorToastThatPlaylistAlreadyExist();

    void displayErrorToastThatPlaylistMustHaveAtLeastOneChar();

    void openFragmentWithBackStack(Fragment fragment);

    void displayConfirmDeleteDialog(final int position);

    void displayPlaylistTitleDialog(final Integer position, final boolean isNewPlaylist);
}
