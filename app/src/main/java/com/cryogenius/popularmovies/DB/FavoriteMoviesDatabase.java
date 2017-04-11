package com.cryogenius.popularmovies.DB;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by Ana Neto on 19/03/2017.
 */

@Database(version = FavoriteMoviesDatabase.VERSION)
public final class FavoriteMoviesDatabase {

    public static final int VERSION = 1;

    @Table(FavoriteMovieEntry.class) public static final String FAVORITE_MOVIES = "favorite_movies";

    private FavoriteMoviesDatabase(){

    }
}