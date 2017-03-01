package com.cryogenius.popularmovies.Bus.Messages.Events;

import com.cryogenius.popularmovies.API.Models.MovieTrailerList;

/**
 * Created by Ana Neto on 01/03/2017.
 */

public class MovieDetailTrailersEvent {

    private MovieTrailerList trailersList;

    public MovieDetailTrailersEvent(MovieTrailerList trailersList){
        this.trailersList = trailersList;
    }

    public MovieTrailerList getSelectedMovie() {
        return trailersList;
    }

    public void setSelectedMovie(MovieTrailerList trailersList) {
        this.trailersList = trailersList;
    }
}
