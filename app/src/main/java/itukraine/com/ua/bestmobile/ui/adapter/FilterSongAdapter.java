package itukraine.com.ua.bestmobile.ui.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import itukraine.com.ua.bestmobile.entity.Song;

/**
 * Created by User on 11.01.2016.
 */
public abstract class FilterSongAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {

    public List<Song> allSongs;
    public List<Song> visibleSongs;

    @Override
    public int getItemCount() {
        return visibleSongs.size();
    }

    public Song getItem(int pos) {
        return visibleSongs.get(pos);
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

}
