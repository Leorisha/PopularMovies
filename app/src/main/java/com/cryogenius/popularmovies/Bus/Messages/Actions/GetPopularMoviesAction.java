package com.cryogenius.popularmovies.Bus.Messages.Actions;

/**
 * Created by Ana Neto on 28/01/2017.
 */

public class GetPopularMoviesAction {

    private String apiKey;

    public GetPopularMoviesAction(String apiKey){
        this.apiKey = apiKey;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
