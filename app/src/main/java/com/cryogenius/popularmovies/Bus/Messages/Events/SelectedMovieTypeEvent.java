package com.cryogenius.popularmovies.Bus.Messages.Events;

import com.cryogenius.popularmovies.API.Models.MovieListType;

/**
 * Created by Ana Neto on 05/02/2017.
 */

public class SelectedMovieTypeEvent {

    private MovieListType selectedMovieType;

    public SelectedMovieTypeEvent(MovieListType selectedMovieType){
        this.selectedMovieType = selectedMovieType;
    }

    public MovieListType getSelectedMovieType() {
        return selectedMovieType;
    }

    public void setSelectedMovieType(MovieListType selectedMovieType) {
        this.selectedMovieType = selectedMovieType;
    }
}
