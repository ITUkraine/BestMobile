package itukraine.com.ua.bestmobile.ui.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import itukraine.com.ua.bestmobile.R;
import itukraine.com.ua.bestmobile.entity.Playlist;
import itukraine.com.ua.bestmobile.interactor.AllPlaylistInteractor;
import itukraine.com.ua.bestmobile.interactor.impl.AllPlaylistInteractorImpl;
import itukraine.com.ua.bestmobile.util.TimeUtil;

public class PlaylistAdapter extends FilterPlaylistAdapter<PlaylistAdapter.ViewHolder> {

    private AllPlaylistInteractor allPlaylistInteractor;

    public PlaylistAdapter(List<Playlist> playlists) {
        allRows = playlists;
        visibleRows = playlists;
        allPlaylistInteractor = new AllPlaylistInteractorImpl();
    }

    @Override
    public PlaylistAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_playlist, parent, false);

        ViewHolder vh = new ViewHolder(v);
        vh.mPlaylistName = (TextView) v.findViewById(R.id.playlist_name);
        vh.mPlaylistDuration = (TextView) v.findViewById(R.id.playlist_duration);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mPlaylistName.setText(getItem(position).name);
        holder.mPlaylistDuration.setText(TimeUtil.getInstance()
                .formatTime(allPlaylistInteractor.getTotalTimeOfPlaylist(getItem(position))));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mPlaylistName;
        public TextView mPlaylistDuration;

        public ViewHolder(View v) {
            super(v);
        }

    }

}