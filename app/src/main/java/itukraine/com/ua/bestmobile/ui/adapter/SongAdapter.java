package itukraine.com.ua.bestmobile.ui.adapter;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import itukraine.com.ua.bestmobile.R;
import itukraine.com.ua.bestmobile.entity.Song;
import itukraine.com.ua.bestmobile.interactor.impl.SongListInteractorImpl;

public class SongAdapter extends FilterSongAdapter<SongAdapter.ViewHolder> {

    private SongListInteractorImpl songListInteractor;

    public SongAdapter(List<Song> songs) {
        allRows = songs;
        visibleRows = songs;
        songListInteractor = new SongListInteractorImpl();
    }

    @Override
    public SongAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_song, parent, false);

        ViewHolder vh = new ViewHolder(v);
        vh.mSongTitle = (TextView) v.findViewById(R.id.song_title);
        vh.mSongArtist = (TextView) v.findViewById(R.id.song_artist);
        vh.mAlbumArt = (ImageView) v.findViewById(R.id.album_art);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mSongTitle.setText(getItem(position).title);
        holder.mSongArtist.setText(getItem(position).artist);

        new PictureLoader(getItem(position).albumId, holder).execute();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mSongTitle;
        public TextView mSongArtist;
        public ImageView mAlbumArt;

        public ViewHolder(View v) {
            super(v);
        }

    }

    private class PictureLoader extends AsyncTask<Void, Void, Bitmap> {

        private long mAlbumId;
        private ViewHolder mHolder;

        public PictureLoader(long albumId, ViewHolder holder) {
            mAlbumId = albumId;
            mHolder = holder;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            return songListInteractor.getAlbumArt(mAlbumId);
        }

        @Override
        protected void onPostExecute(Bitmap albumArt) {
            if (albumArt != null) {
                mHolder.mAlbumArt.setImageBitmap(albumArt);
            } else {
                mHolder.mAlbumArt.setImageResource(R.drawable.default_song_picture);
            }
        }
    }
}