package itukraine.com.ua.bestmobile.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import itukraine.com.ua.bestmobile.R;
import itukraine.com.ua.bestmobile.entity.Song;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {

    private static final String TAG = SongAdapter.class.getCanonicalName();

    public List<Song> visibleSongs;
    public List<Song> allSongs;
    private Context mContext;

    // Provide a suitable constructor (depends on the kind of dataset)
    public SongAdapter(Context context, List<Song> songs) {
        this.allSongs = songs;
        this.visibleSongs = songs;
        mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SongAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_song, parent, false);

        ViewHolder vh = new ViewHolder(v);
        vh.mSongTitle = (TextView) v.findViewById(R.id.song_title);
        vh.mSongArtist = (TextView) v.findViewById(R.id.song_artist);
        vh.mAlbumArt = (ImageView) v.findViewById(R.id.album_art);

        return vh;
    }

    private Song getItem(int pos) {
        return visibleSongs.get(pos);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mSongTitle.setText(getItem(position).title);
        holder.mSongArtist.setText(getItem(position).artist);
        /*Bitmap albumArt = MusicUtil.getInstance().getAlbumart(mContext, getItem(position).albumId); TODO move logic to SongFragment
        if (albumArt != null) {
            holder.mAlbumArt.setImageBitmap(albumArt);
        } else {
            holder.mAlbumArt.setImageResource(R.drawable.default_song_picture);
        }*/
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return visibleSongs.size();
    }

    public void flushFilter() {
        visibleSongs = new ArrayList<>();
        visibleSongs.addAll(allSongs);
        notifyDataSetChanged();
    }

    public void setFilter(String queryText) {

        visibleSongs = new ArrayList<>();
        for (Song song : allSongs) {
            if (song.artist.toLowerCase().contains(queryText.toLowerCase())
                    || song.title.toLowerCase().contains(queryText.toLowerCase()))
                visibleSongs.add(song);
        }
        notifyDataSetChanged();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mSongTitle;
        public TextView mSongArtist;
        public ImageView mAlbumArt;

        public ViewHolder(View v) {
            super(v);
        }

    }
}