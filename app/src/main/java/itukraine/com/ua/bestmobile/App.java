package itukraine.com.ua.bestmobile;

import android.app.Application;

import itukraine.com.ua.bestmobile.data.DatabaseManager;

/**
 * Created by User on 05.01.2016.
 */
public class App extends Application {

    private static App instance;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        instance = this;
        DatabaseManager.init(this);
        super.onCreate();
    }
}
