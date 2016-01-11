package itukraine.com.ua.bestmobile.presenter.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import itukraine.com.ua.bestmobile.entity.Playlist;
import itukraine.com.ua.bestmobile.entity.Song;
import itukraine.com.ua.bestmobile.interactor.PickSongInteractor;
import itukraine.com.ua.bestmobile.interactor.impl.PickSongInteractorImpl;
import itukraine.com.ua.bestmobile.presenter.PickSongPresenter;
import itukraine.com.ua.bestmobile.ui.adapter.PickSongAdapter;
import itukraine.com.ua.bestmobile.ui.fragment.view.PickSongView;

public class PickSongPresenterImpl implements PickSongPresenter {

    private PickSongView pickSongView;
    private PickSongInteractor pickSongInteractor;

    private PickSongAdapter mAdapter;

    private String playlistName;

    private Comparator<Song> alphabeticalSongComparator = new Comparator<Song>() {
        public int compare(Song s1, Song s2) {
            return s1.artist.toLowerCase().compareTo(s2.artist.toLowerCase());
        }
    };

    public PickSongPresenterImpl(PickSongView view) {
        this.pickSongView = view;
        pickSongInteractor = new PickSongInteractorImpl();
    }

    @Override
    public void init(String playlistName) {
        this.playlistName = playlistName;

        Playlist playlist = pickSongInteractor.getPlaylist(playlistName);
        List<Song> allSongs = pickSongInteractor.getAllSongs();
        Collections.sort(allSongs, alphabeticalSongComparator);

        mAdapter = new PickSongAdapter(allSongs, playlist);

        pickSongView.setAdapter(mAdapter);

        pickSongView.showNotificationToastToSelectSongs();
    }

    @Override
    public void createOrUpdatePlaylist() {
        Playlist newPlaylist = new Playlist(this.playlistName);
        newPlaylist.songsId.clear();
        newPlaylist.songsId.addAll(mAdapter.selectedSongs);
        newPlaylist.totalTime = pickSongInteractor.calculateDurationOfPlaylist(newPlaylist);
        pickSongInteractor.createOrUpdatePlaylist(newPlaylist);
    }

    @Override
    public boolean checkIfSongsWasSelected() {
        boolean isSongsSelected = true;
        if (mAdapter.selectedSongs.size() == 0) {
            pickSongView.showErrorToast();
            isSongsSelected = false;
        }
        return isSongsSelected;
    }

    @Override
    public void selectSongFromList(int position) {
        if (!mAdapter.selectedSongs.contains(mAdapter.visibleSongs.get(position).id)) {
            mAdapter.selectedSongs.add(mAdapter.visibleSongs.get(position).id);
        } else {
            mAdapter.selectedSongs.remove(mAdapter.visibleSongs.get(position).id);
        }
        mAdapter.notifyItemChanged(position);
    }

    @Override
    public void filterSongs() {
        String query = pickSongView.getSearchQuery();
        mAdapter.setFilter(query);
        pickSongView.displayClearSearchButton(query.length() > 0);
    }

    @Override
    public void clearFilter() {
        // Previously was like that
        // ((PickSongAdapter) mRecyclerView.getAdapter())
        mAdapter.flushFilter();
        pickSongView.displayClearSearchButton(false);
        pickSongView.clearSearchQuery();
    }

}
