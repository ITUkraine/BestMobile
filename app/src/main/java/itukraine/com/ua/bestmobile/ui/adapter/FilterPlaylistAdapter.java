package itukraine.com.ua.bestmobile.ui.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import itukraine.com.ua.bestmobile.entity.Playlist;

/**
 * Created by User on 11.01.2016.
 */
public abstract class FilterPlaylistAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {

    public List<Playlist> allPlaylists;
    public List<Playlist> visiblePlaylists;

    @Override
    public int getItemCount() {
        return visiblePlaylists.size();
    }

    public Playlist getItem(int pos) {
        return visiblePlaylists.get(pos);
    }

    public void flushFilter() {
        visiblePlaylists = new ArrayList<>();
        visiblePlaylists.addAll(allPlaylists);
        notifyDataSetChanged();
    }

    public void setFilter(String queryText) {

        visiblePlaylists = new ArrayList<>();
        for (Playlist playlist : allPlaylists) {
            if (playlist.name.toLowerCase().contains(queryText.toLowerCase()))
                visiblePlaylists.add(playlist);
        }
        notifyDataSetChanged();
    }

}
