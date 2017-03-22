package com.cryogenius.popularmovies.DB;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;


/**
 * Created by Ana Neto on 19/03/2017.
 */

@ContentProvider(authority = FavoriteMoviesContentProvider.AUTHORITY, database = FavoriteMoviesDatabase.class)
public final class FavoriteMoviesContentProvider {

    public static final String AUTHORITY = "com.cryogenius.popularmovies.DB.FavoriteMoviesContentProvider";
    private  static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY) ;

    @TableEndpoint(table = FavoriteMoviesDatabase.FAVORITE_MOVIES)
    public static class FavoriteMovies {

        private static Uri buildUri(String... paths) {
            Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
            for (String path : paths) {
                builder.appendPath(path);
            }
            return builder.build();
        }

        @ContentUri(
                path = "favorite_movies",
                type = "vnd.android.cursor.dir/favorite_movies",
                defaultSort = FavoriteMovieEntry._ID + " ASC")
        public static final Uri FAVORITE_MOVIES = buildUri("favorite_movies");

        @InexactContentUri(
                name = "favorite_movie_with_id",
                path = "favorite_movies" + "/#",
                type = "vnd.android.cursor.item/favorite_movies",
                whereColumn = FavoriteMovieEntry._ID,
                pathSegment = 1
        )
        public static Uri withId(int id) {
            return buildUri("favorite_movies", String.valueOf(id));
        }
    }
}
