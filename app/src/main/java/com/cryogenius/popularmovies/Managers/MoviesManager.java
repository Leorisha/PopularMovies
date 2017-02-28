package com.cryogenius.popularmovies.Managers;

import android.util.Log;

import com.cryogenius.popularmovies.API.Models.Movie;
import com.cryogenius.popularmovies.API.Models.MovieList;
import com.cryogenius.popularmovies.API.Models.MovieListType;
import com.cryogenius.popularmovies.API.MoviesAPI;
import com.cryogenius.popularmovies.BuildConfig;
import com.cryogenius.popularmovies.Bus.EventBus;
import com.cryogenius.popularmovies.Bus.Messages.Actions.GetMovieDetailAction;
import com.cryogenius.popularmovies.Bus.Messages.Actions.GetPopularMoviesAction;
import com.cryogenius.popularmovies.Bus.Messages.Actions.GetSelectedMovieTypeAction;
import com.cryogenius.popularmovies.Bus.Messages.Actions.GetTopRatedMoviesAction;
import com.cryogenius.popularmovies.Bus.Messages.Events.MovieDetailEvent;
import com.cryogenius.popularmovies.Bus.Messages.Events.PopularMoviesEvent;
import com.cryogenius.popularmovies.Bus.Messages.Events.SelectedMovieTypeEvent;
import com.cryogenius.popularmovies.Bus.Messages.Events.TopRatedMoviesEvent;
import com.squareup.otto.Subscribe;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ana Neto on 23/01/2017.
 */

public class MoviesManager {

    private static MoviesManager instance = new MoviesManager();
    private MovieList topRatedMovies;
    private MovieList popularMovies;

    private MovieListType selectedMovieType = MovieListType.POPULAR;

    public static MoviesManager getInstance() {
        return instance;
    }

    private MoviesManager() {
        this.topRatedMovies = new MovieList();
        this.popularMovies = new MovieList();
    }

    public MovieList getTopRatedMovies() {
        return topRatedMovies;
    }

    public void setTopRatedMovies(MovieList topRatedMovies) {
        this.topRatedMovies = topRatedMovies;
    }

    public MovieList getPopularMovies() {
        return popularMovies;
    }

    public void setPopularMovies(MovieList popularMovies) {
        this.popularMovies = popularMovies;
    }

    public void initialize() {
        EventBus.getInstance().register(this);
    }

    @Subscribe
    public void onGetMovieDetailAction(final GetMovieDetailAction action){
        if (action.getSelectedIndex() >= 0) {
            switch (this.selectedMovieType) {
                case TOP_RATED:
                    Movie selectedTopRatedMovie = this.getTopRatedMovies().getMovies().get(action.getSelectedIndex());
                    EventBus.getInstance().post(new MovieDetailEvent(selectedTopRatedMovie));
                    break;
                case POPULAR:
                    Movie selectedPopularMovie = this.getPopularMovies().getMovies().get(action.getSelectedIndex());
                    EventBus.getInstance().post(new MovieDetailEvent(selectedPopularMovie));
                    break;
            }
        }
        else{
            Log.d("DEBUG","onGetMovieDetailAction - failure");
        }
    }

    @Subscribe
    public void onGetPopularMoviesAction(final GetPopularMoviesAction action) {
        MoviesAPI.Factory.getInstance().getPopularMoviesFromApi(BuildConfig.API_KEY).enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                MoviesManager.getInstance().selectedMovieType = MovieListType.POPULAR;
                MoviesManager.getInstance().setPopularMovies(response.body());
                EventBus.getInstance().post(new PopularMoviesEvent(MoviesManager.getInstance().getPopularMovies()));
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                Log.d("DEBUG","onGetPopularMoviesAction - failure");
            }
        });
    }

    @Subscribe
    public void onGetTopRatedMoviesAction(final GetTopRatedMoviesAction action) {
        MoviesAPI.Factory.getInstance().getTopMoviesFromApi(BuildConfig.API_KEY).enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                MoviesManager.getInstance().selectedMovieType = MovieListType.TOP_RATED;
                MoviesManager.getInstance().setTopRatedMovies(response.body());
                EventBus.getInstance().post(new TopRatedMoviesEvent(MoviesManager.getInstance().getTopRatedMovies()));
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                Log.d("DEBUG","onGetTopRatedMoviesAction - failure");
            }
        });
    }

    @Subscribe
    public void onGetSelectedMovieTypeAction (final GetSelectedMovieTypeAction action) {
        EventBus.getInstance().post(new SelectedMovieTypeEvent(this.selectedMovieType));
    }
}
