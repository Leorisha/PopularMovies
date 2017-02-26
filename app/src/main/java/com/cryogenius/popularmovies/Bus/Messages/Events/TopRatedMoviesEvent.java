package com.cryogenius.popularmovies.Bus.Messages.Events;

import com.cryogenius.popularmovies.API.Models.MovieList;

/**
 * Created by Ana Neto on 29/01/2017.
 */

public class TopRatedMoviesEvent {

    private MovieList topRatedMovieList;

    public TopRatedMoviesEvent(MovieList topRatedMovies){
        this.topRatedMovieList = topRatedMovies;
    }

    public MovieList getTopRatedMovieList() {
        return topRatedMovieList;
    }

    public void setTopRatedMovieList(MovieList topRatedMovieList) {
        this.topRatedMovieList = topRatedMovieList;
    }
}
