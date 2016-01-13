package itukraine.com.ua.bestmobile.ui.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import itukraine.com.ua.bestmobile.entity.Playlist;

/**
 * Created by User on 11.01.2016.
 */
public abstract class FilterPlaylistAdapter<T extends RecyclerView.ViewHolder> extends FilterAdapter<Playlist, T> {

    @Override
    public void setFilter(String queryText) {

        visibleRows = new ArrayList<>();
        for (Playlist playlist : allRows) {
            if (playlist.name.toLowerCase().contains(queryText.toLowerCase()))
                visibleRows.add(playlist);
        }
        notifyDataSetChanged();
    }

}
