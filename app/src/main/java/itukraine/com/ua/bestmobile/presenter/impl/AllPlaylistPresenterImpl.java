package itukraine.com.ua.bestmobile.presenter.impl;

import android.os.Bundle;

import java.io.IOException;
import java.util.List;

import itukraine.com.ua.bestmobile.App;
import itukraine.com.ua.bestmobile.Constants;
import itukraine.com.ua.bestmobile.R;
import itukraine.com.ua.bestmobile.entity.Playlist;
import itukraine.com.ua.bestmobile.interactor.AllPlaylistInteractor;
import itukraine.com.ua.bestmobile.interactor.PlayerInteractor;
import itukraine.com.ua.bestmobile.interactor.impl.AllPlaylistInteractorImpl;
import itukraine.com.ua.bestmobile.interactor.impl.PlayerInteractorImpl;
import itukraine.com.ua.bestmobile.presenter.AllPlaylistPresenter;
import itukraine.com.ua.bestmobile.ui.adapter.PlaylistAdapter;
import itukraine.com.ua.bestmobile.ui.fragment.SongListFragment;
import itukraine.com.ua.bestmobile.ui.fragment.view.AllPlaylistView;

public class AllPlaylistPresenterImpl implements AllPlaylistPresenter {

    private AllPlaylistView allPlaylistView;
    private AllPlaylistInteractor allPlaylistInteractor;
    private PlayerInteractor playerInteractor;

    private List<Playlist> playlists;

    private PlaylistAdapter mAdapter;

    public AllPlaylistPresenterImpl(AllPlaylistView allPlaylistView) {
        this.allPlaylistView = allPlaylistView;
        this.allPlaylistInteractor = new AllPlaylistInteractorImpl();
        this.playerInteractor = new PlayerInteractorImpl();

        playlists = allPlaylistInteractor.getAllPlaylists();

        mAdapter = new PlaylistAdapter(playlists);
        allPlaylistView.setAdapter(mAdapter);
    }

    @Override
    public void deletePlaylist(int pos) {
        allPlaylistInteractor.deletePlaylist(playlists.get(pos).name);
        playlists.remove(pos);

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void filterSongs() {
        String query = allPlaylistView.getSearchQuery();
        mAdapter.setFilter(query);
        allPlaylistView.displayClearSearchButton(query.length() > 0);
    }

    @Override
    public void clearFilter() {
        mAdapter.flushFilter();
        allPlaylistView.displayClearSearchButton(false);
        allPlaylistView.clearSearchQuery();
    }

    @Override
    public boolean isPlaylistDefault(int positionInList) {
        return allPlaylistInteractor.isPlaylistDefault(playlists.get(positionInList).name);
    }

    @Override
    public void selectAndPlayPlaylist(int positionInList) {
        boolean isDefaultPlaylist = isPlaylistDefault(positionInList);
        if (isDefaultPlaylist) {
            playerInteractor.setPlaylist(new Playlist(App.getInstance().getString(R.string.all_songs_playlist_name)));// TODO
        } else {
            playerInteractor.setPlaylist(playlists.get(positionInList).name);
        }

        try {
            playerInteractor.play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void renamePlaylist(int positionOfOldPlaylist, String newName) {
        String oldPlaylistName = playlists.get(positionOfOldPlaylist).name;

        allPlaylistInteractor.renamePlaylist(oldPlaylistName, newName);

        playlists.get(positionOfOldPlaylist).name = newName;

        mAdapter.notifyItemChanged(positionOfOldPlaylist);
    }

    @Override
    public boolean isPlaylistExist(String playlistName) {
        return allPlaylistInteractor.isPlaylistDefault(playlistName)
                || allPlaylistInteractor.getPlaylist(playlistName) != null;
    }

    @Override
    public void openSongListFragmentForSelectedPlaylist(int playlistPosition) {
        SongListFragment songListFragment = new SongListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.PLAYLIST_NAME, playlists.get(playlistPosition).name);
        songListFragment.setArguments(bundle);
        allPlaylistView.openFragmentWithBackStack(songListFragment);
    }

}
