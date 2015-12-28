
package itukraine.com.ua.bestmobile.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import itukraine.com.ua.bestmobile.dao.Playlist;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getCanonicalName();
    // name of the database file for your application -- change to something appropriate for your app
    private static final String DATABASE_NAME = "bestmobile.db";
    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 1;

    private static final Class[] daoList = {Playlist.class};

    private static DatabaseHelper instance;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DatabaseHelper getInstance(Context paramContext) {
        if (instance == null) {
            instance = new DatabaseHelper(paramContext);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase paramSQLiteDatabase, ConnectionSource paramConnectionSource) {
        for (int i = 0; i < daoList.length; i++) {
            try {
                TableUtils.createTableIfNotExists(paramConnectionSource, daoList[i]);
            } catch (SQLException e) {
                Log.e(TAG, "Exception when try to create table " + daoList[i]);
            }
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, ConnectionSource paramConnectionSource, int paramInt1,
                          int paramInt2) {
        Log.w(TAG, "onUpgrade");
        for (int i = 0; i < daoList.length; i++) {
            try {
                TableUtils.dropTable(paramConnectionSource, daoList[i], true);
            } catch (SQLException e) {
                Log.e(TAG, "Exception when try to drop tables on onUpgrade()");
            }
        }
        for (int i = 0; i < daoList.length; i++) {
            try {
                TableUtils.createTable(paramConnectionSource, daoList[i]);
            } catch (SQLException e) {
                Log.e(TAG, "Exception when try to create tables on onUpgrade()");
            }
        }
    }

    @Override
    public void close() {
        super.close();
    }

    public void addPlaylist(Playlist playlist) {
        try {
            getDao(Playlist.class).createOrUpdate(playlist);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Playlist> getPlaylists() {
        try {
            return getDao(Playlist.class).queryForAll();
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
    public Playlist findPlaylistByName(String name) {
        List<Playlist> playlists = new ArrayList<>();
        try {
            playlists = getDao(Playlist.class).query(getDao(Playlist.class).queryBuilder().where().like("name", name).prepare());
            if (playlists.size() == 0) {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return playlists.get(0);
    }
}
