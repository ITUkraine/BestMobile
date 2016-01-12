package itukraine.com.ua.bestmobile.presenter;

public interface MainPresenter {

    void onDestroy();

    void updateNavigationHeaderControls();

    void updateNavigationHeaderSongInfo();

    void onHeaderPlayButtonPressed();

    void onHeaderPreviousButtonPressed();

    void onHeaderNextButtonPressed();

}
