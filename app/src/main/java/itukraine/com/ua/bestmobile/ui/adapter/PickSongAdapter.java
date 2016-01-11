package itukraine.com.ua.bestmobile.ui.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import itukraine.com.ua.bestmobile.R;
import itukraine.com.ua.bestmobile.entity.Playlist;
import itukraine.com.ua.bestmobile.entity.Song;
import itukraine.com.ua.bestmobile.interactor.PickSongInteractor;
import itukraine.com.ua.bestmobile.interactor.impl.PickSongInteractorImpl;

public class PickSongAdapter extends RecyclerView.Adapter<PickSongAdapter.ViewHolder> {

    private static final String TAG = PickSongAdapter.class.getCanonicalName();
    public List<Long> selectedSongs = new ArrayList<>();
    public List<Song> visibleSongs;
    private PickSongInteractor pickSongInteractor;
    private List<Song> allSongs;

    public PickSongAdapter(List<Song> allSongs, Playlist playlist) {
        this.allSongs = allSongs;
        this.visibleSongs = allSongs;
        if (playlist != null) {
            selectedSongs = playlist.songsId;
        }
        pickSongInteractor = new PickSongInteractorImpl();
    }

    @Override
    public PickSongAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_song, parent, false);

        ViewHolder vh = new ViewHolder(v);
        vh.mSongTitle = (TextView) v.findViewById(R.id.song_title);
        vh.mSongArtist = (TextView) v.findViewById(R.id.song_artist);
        vh.mAlbumArt = (ImageView) v.findViewById(R.id.album_art);
        vh.mWholeItem = (LinearLayout) v.findViewById(R.id.whole_item);

        return vh;
    }

    private Song getItem(int pos) {
        return visibleSongs.get(pos);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mSongTitle.setText(getItem(position).title);
        holder.mSongArtist.setText(getItem(position).artist);
        Bitmap albumArt = pickSongInteractor.getAlbumArt(getItem(position).albumId);
        if (albumArt != null) {
            holder.mAlbumArt.setImageBitmap(albumArt);
        } else {
            holder.mAlbumArt.setImageResource(R.drawable.default_song_picture);
        }

        holder.mWholeItem.setSelected(selectedSongs.contains(getItem(position).id));
    }

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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mSongTitle;
        public TextView mSongArtist;
        public ImageView mAlbumArt;
        public LinearLayout mWholeItem;

        public ViewHolder(View v) {
            super(v);
            v.setClickable(true);
        }

    }
}