package itukraine.com.ua.bestmobile.presenter;

public interface SongListPresenter extends Filterable {

    void onResume();

    void removeSongFromPlaylist(int positionInPlaylist);

    boolean isLastSong();

    void deleteLastSong(int positionInPlaylist);

    void playCurrentPlaylistFromPosition(int position);

    boolean isCurrentPlaylistDefault();

    void displayPickSongView();
}
