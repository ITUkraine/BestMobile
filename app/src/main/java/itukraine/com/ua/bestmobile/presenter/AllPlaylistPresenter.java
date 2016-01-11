package itukraine.com.ua.bestmobile.presenter;

public interface AllPlaylistPresenter {
    void deletePlaylist(int pos);

    void filterSongs();

    void clearFilter();

    boolean isPlaylistDefault(int positionInList);

    void selectAndPlayPlaylist(int positionInList);

    void renamePlaylist(int positionOfOldPlaylist, String newName);

    boolean isPlaylistExist(String playlistName);

    void openSongListFragmentForSelectedPlaylist(int playlistPosition);
}
