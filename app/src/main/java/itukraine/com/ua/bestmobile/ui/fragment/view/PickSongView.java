package itukraine.com.ua.bestmobile.ui.fragment.view;

import itukraine.com.ua.bestmobile.ui.adapter.PickSongAdapter;

public interface PickSongView {

    void setAdapter(PickSongAdapter adapter);

    void showErrorToast();

    void showNotificationToastToSelectSongs();

    String getSearchQuery();

    void clearSearchQuery();

    void displayClearSearchButton(boolean isDisplayed);

}
