package itukraine.com.ua.bestmobile.presenter;

public interface PickSongPresenter {

    void init(String playlistName, boolean isNewPlaylist);

    void createOrUpdatePlaylist();

    boolean checkIfSongsWasSelected();

    void selectSongFromList(int position);

    void filterSongs();

    void clearFilter();
}
