package itukraine.com.ua.bestmobile.presenter.impl;

import java.util.List;

import itukraine.com.ua.bestmobile.entity.Playlist;
import itukraine.com.ua.bestmobile.interactor.AllPlaylistInteractor;
import itukraine.com.ua.bestmobile.interactor.impl.AllPlaylistInteractorImpl;
import itukraine.com.ua.bestmobile.presenter.AllPlaylistPresenter;
import itukraine.com.ua.bestmobile.ui.fragment.view.AllPlaylistView;

/**
 * Created by User on 05.01.2016.
 */
public class AllPlaylistPresenterImpl implements AllPlaylistPresenter {

    private AllPlaylistView allPlaylistView;
    private AllPlaylistInteractor allPlaylistInteractor;

    private List<Playlist> playlists;

    public AllPlaylistPresenterImpl(AllPlaylistView allPlaylistView) {
        this.allPlaylistView = allPlaylistView;
        this.allPlaylistInteractor = new AllPlaylistInteractorImpl();
    }


    @Override
    public void onCreate() {
        allPlaylistInteractor.updateDefaultPlaylist();
        playlists = allPlaylistInteractor.getAllPlaylists();
        allPlaylistView.setPlaylists(playlists);
    }

    @Override
    public void deletePlaylist(int pos) {
        allPlaylistInteractor.deletePlaylist(playlists.get(pos).name);
        playlists.remove(pos);
    }
}
