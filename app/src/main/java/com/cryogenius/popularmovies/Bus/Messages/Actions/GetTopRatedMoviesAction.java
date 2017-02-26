package com.cryogenius.popularmovies.Bus.Messages.Actions;

/**
 * Created by Ana Neto on 26/01/2017.
 */

public class GetTopRatedMoviesAction {

    private String apiKey;

    public GetTopRatedMoviesAction(String apiKey){
        this.apiKey = apiKey;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
