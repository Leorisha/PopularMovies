
package com.cryogenius.popularmovies.API.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieTrailerList {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("results")
    @Expose
    private List<MovieTrailer> trailers = null;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<MovieTrailer> getTrailers() {
        return trailers;
    }

    public void setResults(List<MovieTrailer> trailers) {
        this.trailers = trailers;
    }

}
