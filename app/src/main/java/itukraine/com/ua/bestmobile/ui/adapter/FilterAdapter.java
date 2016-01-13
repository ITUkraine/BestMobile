package itukraine.com.ua.bestmobile.ui.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 13.01.2016.
 */
public abstract class FilterAdapter<V, T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {

    public List<V> allRows;
    public List<V> visibleRows;

    public void flushFilter() {
        visibleRows = new ArrayList<>();
        visibleRows.addAll(allRows);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return visibleRows.size();
    }

    public V getItem(int pos) {
        return visibleRows.get(pos);
    }

    public abstract void setFilter(String queryText);

}
