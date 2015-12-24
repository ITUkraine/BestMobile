package itukraine.com.ua.bestmobile.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import itukraine.com.ua.bestmobile.R;
import itukraine.com.ua.bestmobile.dao.Playlist;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {

    private List<Playlist> playlists;

    // Provide a suitable constructor (depends on the kind of dataset)
    public PlaylistAdapter(List<Playlist> playlists) {
        this.playlists = playlists;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PlaylistAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_playlist, parent, false);

        ViewHolder vh = new ViewHolder(v);
        vh.mPlaylistName = (TextView) v.findViewById(R.id.playlist_name);

        return vh;
    }

    private Playlist getItem(int pos) {
        return playlists.get(pos);
    }

    public void add(Playlist playlist) {
        playlists.add(playlist);
        notifyDataSetChanged();
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mPlaylistName.setText(getItem(position).name);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return playlists.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mPlaylistName;

        public ViewHolder(View v) {
            super(v);
        }
    }
}