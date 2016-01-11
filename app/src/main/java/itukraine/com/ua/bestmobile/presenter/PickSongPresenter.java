package itukraine.com.ua.bestmobile.presenter;

public interface PickSongPresenter {

    void init(String playlistName);

    void createOrUpdatePlaylist();

    boolean checkIfSongsWasSelected();

    void selectSongFromList(int position);

    void filterSongs();

    void clearFilter();
}
