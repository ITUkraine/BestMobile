package itukraine.com.ua.bestmobile.presenter;

public interface AllPlaylistPresenter extends Filterable {
    void deletePlaylist(int pos);

    boolean isPlaylistDefault(int positionInList);

    void selectAndPlayPlaylist(int positionInList);

    void renamePlaylist(int positionOfOldPlaylist, String newName);

    boolean isPlaylistExist(String playlistName);

    void openSongListFragmentForSelectedPlaylist(int playlistPosition);

    String getOldPlaylistName(Integer position);
}
