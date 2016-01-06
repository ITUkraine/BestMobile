package itukraine.com.ua.bestmobile.presenter;

public interface PlayerPresenter {

    void onPlayButtonPressed();

    void onNextButtonPressed();

    void onPreviousButtonPressed();

    void updateSongInfo();

    void updateSongProgress();

    void updatePlayButton();

    void onCreate();

    void onDestroy();

    void rewindTo(int pos);
}
