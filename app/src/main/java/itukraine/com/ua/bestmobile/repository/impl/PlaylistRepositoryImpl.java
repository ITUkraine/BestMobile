package itukraine.com.ua.bestmobile.repository.impl;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import itukraine.com.ua.bestmobile.data.DatabaseManager;
import itukraine.com.ua.bestmobile.entity.Playlist;
import itukraine.com.ua.bestmobile.repository.PlaylistRepository;

/**
 * Created by User on 05.01.2016.
 */
public class PlaylistRepositoryImpl implements PlaylistRepository {

    private DatabaseManager databaseManager;

    public PlaylistRepositoryImpl(Context context) {
        this.databaseManager = DatabaseManager.getInstance(context);
    }

    @Override
    public void addPlaylist(Playlist playlist) {
        try {
            getDao().createOrUpdate(playlist);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Dao<Playlist, ?> getDao() throws SQLException {
        return databaseManager.getDao(Playlist.class);
    }

    @Override
    public List<Playlist> getPlaylists() {
        try {
            return getDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * Method to find playlist by name
     *
     * @param name name of the playlist
     * @return instance of the playlist or null if playlist with this name doesn't exist
     */
    @Override
    public Playlist findPlaylistByName(String name) {
        List<Playlist> playlists = new ArrayList<>();
        try {
            playlists = getDao().query(getDao().queryBuilder().where().like("name", name).prepare());
            if (playlists.size() == 0) {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return playlists.get(0);
    }

    @Override
    public void deletePlaylistByName(String name) {
        Dao dao;
        try {
            dao = getDao();
            DeleteBuilder deleteBuilder = dao.deleteBuilder();
            deleteBuilder.where().eq("name", name);
            deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void renamePlaylist(String oldPlaylistName, String newPlaylistName) {
        Dao dao;
        try {
            dao = getDao();
            UpdateBuilder updateBuilder = dao.updateBuilder();
            updateBuilder.where().eq("name", oldPlaylistName);
            updateBuilder.updateColumnValue("name", newPlaylistName);
            updateBuilder.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updatePlaylist(Playlist playlist) {
        try {
            getDao().update(playlist);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
