package com.cryogenius.popularmovies.Bus.Messages.Events;

import com.cryogenius.popularmovies.API.Models.Movie;

/**
 * Created by Ana Neto on 29/01/2017.
 */

public class MovieDetailEvent {

    private Movie selectedMovie;

    public MovieDetailEvent(Movie selectedMovie){
        this.selectedMovie = selectedMovie;
    }

    public Movie getSelectedMovie() {
        return selectedMovie;
    }

    public void setSelectedMovie(Movie selectedMovie) {
        this.selectedMovie = selectedMovie;
    }
}
