package itukraine.com.ua.bestmobile.presenter;

public interface PickSongPresenter extends Filterable {

    void init(String playlistName);

    void createOrUpdatePlaylist();

    boolean checkIfSongsWasSelected();

    void selectSongFromList(int position);

}
