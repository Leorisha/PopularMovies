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

    public MovieTrailerList getTrailersList() {
        return trailersList;
    }

    public void setTrailersList(MovieTrailerList trailersList) {
        this.trailersList = trailersList;
    }
}
