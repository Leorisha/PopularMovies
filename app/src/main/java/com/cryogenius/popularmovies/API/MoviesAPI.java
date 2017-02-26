package com.cryogenius.popularmovies.API;

import com.cryogenius.popularmovies.API.Models.MovieList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Ana Neto on 28/01/2017.
 */

public interface MoviesAPI {

    @GET("movie/top_rated")
    Call<MovieList> getTopMoviesFromApi (
            @Query("api_key") String apiKey
    );

    @GET("movie/popular")
    Call<MovieList> getPopularMoviesFromApi (
            @Query("api_key") String apiKey
    );

    class Factory {

        private static MoviesAPI api;
        private static final String BASE_API_URL= "https://api.themoviedb.org/3/";

        public static MoviesAPI getInstance() {
            if (api == null){
                Retrofit retrofit = new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(BASE_API_URL)
                        .build();

                api = retrofit.create(MoviesAPI.class);
            }
            return api;
        }
    }


}
