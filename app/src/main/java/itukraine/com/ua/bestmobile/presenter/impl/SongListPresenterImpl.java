package itukraine.com.ua.bestmobile.presenter.impl;

import android.os.Bundle;

import java.io.IOException;
import java.util.List;

import itukraine.com.ua.bestmobile.App;
import itukraine.com.ua.bestmobile.Constants;
import itukraine.com.ua.bestmobile.R;
import itukraine.com.ua.bestmobile.entity.Playlist;
import itukraine.com.ua.bestmobile.entity.Song;
import itukraine.com.ua.bestmobile.interactor.PlayerInteractor;
import itukraine.com.ua.bestmobile.interactor.SongListInteractor;
import itukraine.com.ua.bestmobile.interactor.impl.PlayerInteractorImpl;
import itukraine.com.ua.bestmobile.interactor.impl.SongListInteractorImpl;
import itukraine.com.ua.bestmobile.presenter.SongListPresenter;
import itukraine.com.ua.bestmobile.ui.adapter.SongAdapter;
import itukraine.com.ua.bestmobile.ui.fragment.PickSongFragment;
import itukraine.com.ua.bestmobile.ui.fragment.view.SongListView;

public class SongListPresenterImpl implements SongListPresenter {

    private SongListView songListView;
    private SongListInteractor songListInteractor;
    private PlayerInteractor playerInteractor;

    private SongAdapter mAdapter;

    private List<Song> songList;
    private Playlist currentPlaylist;

    public SongListPresenterImpl(SongListView songListView, String playlistName) {
        this.songListView = songListView;
        this.songListInteractor = new SongListInteractorImpl();
        this.playerInteractor = new PlayerInteractorImpl();

        currentPlaylist = songListInteractor.getPlaylist(playlistName);
    }


    @Override
    public void onResume() {
        songList = songListInteractor.getSongsFromPlaylist(currentPlaylist);
        mAdapter = new SongAdapter(App.getInstance(), songList);
        songListView.setAdapter(mAdapter);

        songListView.displayButtonAddSongsToPlaylist(
                currentPlaylist.name.equals(
                        App.getInstance().getString(R.string.all_songs_playlist_name)));
    }

    @Override
    public void removeSongFromPlaylist(int positionInPlaylist) {
        currentPlaylist.songsId.remove(songList.get(positionInPlaylist).id);

        currentPlaylist.totalTime = songListInteractor.calculateDurationOfPlaylist(currentPlaylist);

        songListInteractor.updatePlaylist(currentPlaylist);

        songList.remove(positionInPlaylist);

        //Performance with notifyItemChanged() will be better,
        // but exist issue https://code.google.com/p/android/issues/detail?id=77846
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean isLastSong() {
        return currentPlaylist.songsId.size() == 1;
    }

    @Override
    public void deleteLastSong(int positionInPlaylist) {
        boolean isLastSong = isLastSong();
        if (isLastSong) {
            songListInteractor.deletePlaylist(currentPlaylist.name);
            songListView.callBackStack();
        } else {
            removeSongFromPlaylist(positionInPlaylist);
        }
    }

    @Override
    public void playCurrentPlaylistFromPosition(int position) {
        playerInteractor.setPlaylist(currentPlaylist);
        playerInteractor.setCurrentSongPosition(position);
        try {
            playerInteractor.play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isCurrentPlaylistDefault() {
        return currentPlaylist.name.equals(App.getInstance().getString(R.string.all_songs_playlist_name));
    }

    @Override
    public void displayPickSongView() {
        PickSongFragment pickSongFragment = new PickSongFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.PLAYLIST_NAME, currentPlaylist.name);
        bundle.putBoolean(Constants.PLAYLIST_IS_NEW, false);
        pickSongFragment.setArguments(bundle);
        songListView.openFragmentWithBackStack(pickSongFragment);
    }
}