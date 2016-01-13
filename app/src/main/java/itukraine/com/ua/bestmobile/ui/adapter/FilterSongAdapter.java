package itukraine.com.ua.bestmobile.ui.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import itukraine.com.ua.bestmobile.entity.Song;

/**
 * Created by User on 11.01.2016.
 */
public abstract class FilterSongAdapter<T extends RecyclerView.ViewHolder> extends FilterAdapter<Song, T> {

    public void setFilter(String queryText) {

        visibleRows = new ArrayList<>();
        for (Song song : allRows) {
            if (song.artist.toLowerCase().contains(queryText.toLowerCase())
                    || song.title.toLowerCase().contains(queryText.toLowerCase()))
                visibleRows.add(song);
        }
        notifyDataSetChanged();
    }

}
