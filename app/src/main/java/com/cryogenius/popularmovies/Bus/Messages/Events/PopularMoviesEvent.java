package com.cryogenius.popularmovies.Bus.Messages.Events;

import com.cryogenius.popularmovies.API.Models.MovieList;

/**
 * Created by Ana Neto on 26/01/2017.
 */

public class PopularMoviesEvent {

    private MovieList popularMoviesList;

    public PopularMoviesEvent(MovieList popularMovies) {
        this.popularMoviesList = popularMovies;
    }

    public MovieList getPopularMoviesList() {
        return popularMoviesList;
    }

    public void setPopularMoviesList(MovieList popularMoviesList) {
        this.popularMoviesList = popularMoviesList;
    }
}
