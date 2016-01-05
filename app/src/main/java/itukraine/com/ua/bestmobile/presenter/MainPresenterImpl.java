package itukraine.com.ua.bestmobile.presenter;

import itukraine.com.ua.bestmobile.ui.activity.MainView;

public class MainPresenterImpl implements MainPresenter {

    private MainView mainView;

    public MainPresenterImpl(MainView view) {
        this.mainView = view;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDestroy() {
        mainView = null;
    }
}
