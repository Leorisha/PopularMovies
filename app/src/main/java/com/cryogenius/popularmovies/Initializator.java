package com.cryogenius.popularmovies;

import android.app.Application;

import com.cryogenius.popularmovies.Managers.MoviesManager;

/**
 * Created by Ana Neto on 26/01/2017.
 */

public class Initializator  extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        MoviesManager.getInstance().initialize();
    }

}
