package com.cryogenius.popularmovies.Bus.Messages.Events;

import com.cryogenius.popularmovies.API.Models.MovieReviewsList;

/**
 * Created by Ana Neto on 01/03/2017.
 */

public class MovieDetailReviewsEvent {

    private MovieReviewsList reviewsList;

    public MovieDetailReviewsEvent(MovieReviewsList reviewsList){
        this.reviewsList = reviewsList;
    }

    public MovieReviewsList getReviewsList() {
        return reviewsList;
    }

    public void setReviewsList(MovieReviewsList reviewsList) {
        this.reviewsList = reviewsList;
    }
}
