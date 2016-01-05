
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

import itukraine.com.ua.bestmobile.entity.Playlist;

public class DatabaseManager extends OrmLiteSqliteOpenHelper {

    private static final String TAG = DatabaseManager.class.getCanonicalName();
    // name of the database file for your application -- change to something appropriate for your app
    private static final String DATABASE_NAME = "bestmobile.db";
    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 1;

    private static final Class[] daoList = {Playlist.class};

    private static DatabaseManager instance;

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DatabaseManager getInstance(Context paramContext) {
        if (instance == null) {
            instance = new DatabaseManager(paramContext);
        }
        return instance;
    }

    public static void init(Context context) {
        getInstance(context).getWritableDatabase();
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
        onCreate(paramSQLiteDatabase, paramConnectionSource);
    }

    @Override
    public void close() {
        super.close();
    }

    public List<Playlist> getPlaylists() {
        try {
            return getDao(Playlist.class).queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
