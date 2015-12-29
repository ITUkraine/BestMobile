package itukraine.com.ua.bestmobile.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import itukraine.com.ua.bestmobile.R;
import itukraine.com.ua.bestmobile.dao.Playlist;
import itukraine.com.ua.bestmobile.fragment.PlayerFragment;
import itukraine.com.ua.bestmobile.util.MusicUtil;
import itukraine.com.ua.bestmobile.util.TimeUtil;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {

    private static final String TAG = PlayerFragment.class.getCanonicalName();
    public List<Playlist> playlists;
    private Context context;

    // Provide a suitable constructor (depends on the kind of dataset)
    public PlaylistAdapter(Context context, List<Playlist> playlists) {
        this.context = context;
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
        vh.mPlaylistDuration = (TextView) v.findViewById(R.id.playlist_duration);

        return vh;
    }

    private Playlist getItem(int pos) {
        return playlists.get(pos);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mPlaylistName.setText(getItem(position).name);

        if (getItem(position).name.equals(context.getResources().getString(R.string.all_songs_playlist_name))) {
            holder.mPlaylistDuration.setText(
                    TimeUtil.getInstance().convertMillis(
                            TimeUtil.getInstance().calculateTotalTimeOfPlaylist(
                                    MusicUtil.getInstance().getAllSongs(context))));
        } else {
            holder.mPlaylistDuration.setText(TimeUtil.getInstance().convertMillis(getItem(position).totalTime));
        }
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
        public TextView mPlaylistDuration;

        public ViewHolder(View v) {
            super(v);
        }

    }
}