package com.cryogenius.popularmovies.UI.MovieGrid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cryogenius.popularmovies.API.Models.Movie;
import com.cryogenius.popularmovies.API.Models.MovieList;
import com.cryogenius.popularmovies.API.Models.MovieListType;
import com.cryogenius.popularmovies.Bus.EventBus;
import com.cryogenius.popularmovies.Bus.Messages.Actions.GetPopularMoviesAction;
import com.cryogenius.popularmovies.Bus.Messages.Actions.GetSelectedMovieTypeAction;
import com.cryogenius.popularmovies.Bus.Messages.Actions.GetTopRatedMoviesAction;
import com.cryogenius.popularmovies.Bus.Messages.Events.PopularMoviesEvent;
import com.cryogenius.popularmovies.Bus.Messages.Events.SelectedMovieTypeEvent;
import com.cryogenius.popularmovies.Bus.Messages.Events.TopRatedMoviesEvent;
import com.cryogenius.popularmovies.DB.FavoriteMovieEntry;
import com.cryogenius.popularmovies.DB.FavoriteMoviesContentProvider;
import com.cryogenius.popularmovies.Managers.MoviesManager;
import com.cryogenius.popularmovies.R;
import com.cryogenius.popularmovies.UI.MovieDetail.MovieDetailActivity;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

public class MovieListActivity extends AppCompatActivity  implements GridItemClickListener {

    public RecyclerView movieGridView;
    private GridViewAdapter mAdapter;
    private MovieListType selectedMovieType;
    private ProgressBar loadingCircle;
    private TextView errorMessage;
    private GridLayoutManager layoutManager;
    private MovieList savesGrid;
    private int gridPosition;

    private static final String LIFECYCLE_MOVIE_TYPE = "movie_type";
    private static final String LIFECYCLE_MOVIE_LIST = "movie_list";
    private static final String LIFECYCLE_MOVIE_POSITION = "movie_grid_position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        this.errorMessage = (TextView)findViewById(R.id.tv_error_message);
        this.loadingCircle = (ProgressBar)findViewById(R.id.pb_loading_indicator);
        this.movieGridView = (RecyclerView)findViewById(R.id.rv_movie_grid);

        updateSizeInfo();

        mAdapter = new GridViewAdapter(this);
        movieGridView.setAdapter(mAdapter);

        loadSelectedMovieTypeFromSharedPreferences();

        if(savedInstanceState == null){
            switch (selectedMovieType) {
                case POPULAR:
                    makePopularMoviesRequest();
                    break;
                case TOP_RATED:
                    makeTopRatedMoviesRequest();
                    break;
                case FAVORITES:
                    makeFavoritesRequest();
                    break;

            }
        }

        setSubtitleOnActionBar(getString(R.string.app_name),null);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(LIFECYCLE_MOVIE_TYPE,this.selectedMovieType);
        outState.putParcelable(LIFECYCLE_MOVIE_LIST, mAdapter.getMoviesInGrid());

        int currentVisiblePosition = layoutManager.findFirstCompletelyVisibleItemPosition();
        outState.putInt(LIFECYCLE_MOVIE_POSITION,currentVisiblePosition);

    }

    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        updateSizeInfo();

        if(state.containsKey(LIFECYCLE_MOVIE_TYPE)){
            this.selectedMovieType = (MovieListType) state.getSerializable(LIFECYCLE_MOVIE_TYPE);
        }

        // Retrieve list state and list/item positions
        if(state != null) {
            savesGrid = state.getParcelable(LIFECYCLE_MOVIE_LIST);
            gridPosition = state.getInt(LIFECYCLE_MOVIE_POSITION);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //showLoader();

        EventBus.getInstance().register(this);
    }

    private void updateSizeInfo() {
        float density  = getResources().getDisplayMetrics().density;
        int width = (int) Math.abs(getResources().getDisplayMetrics().widthPixels/density);
        this.layoutManager = new GridLayoutManager(this,Math.abs(width/130));
        movieGridView.setLayoutManager(layoutManager);
        movieGridView.setHasFixedSize(true);
    }

    @Override
    protected void onPause() {
        super.onPause();

        gridPosition = layoutManager.findFirstCompletelyVisibleItemPosition();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (savesGrid != null) {
            mAdapter.setMoviesInGrid(savesGrid);
            //mAdapter.notifyDataSetChanged();
        }

        if (gridPosition > -1) {
            movieGridView.scrollToPosition(gridPosition);
        }
    }

    @Override
    public void onStop() {
        EventBus.getInstance().unregister(this);
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filter_menu, menu);

        loadSelectedMovieTypeFromSharedPreferences();

        switch (this.selectedMovieType) {
            case POPULAR:
                onOptionsItemSelected(menu.findItem(R.id.action_filter_popular));
                break;
            case TOP_RATED:
                onOptionsItemSelected(menu.findItem(R.id.action_filter_top_rated));
                break;
            case FAVORITES:
                onOptionsItemSelected(menu.findItem(R.id.action_filter_favorites));
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (!item.isChecked()) {
            int itemId = item.getItemId();
            item.setChecked(true);

            switch (itemId) {
                case R.id.action_filter_popular:
                    this.selectedMovieType = MovieListType.POPULAR;
                    saveSelectedMovieTypeInSharedPreferences(this.selectedMovieType);
                    makePopularMoviesRequest();
                    return true;

                case R.id.action_filter_top_rated:
                    this.selectedMovieType = MovieListType.TOP_RATED;
                    saveSelectedMovieTypeInSharedPreferences(this.selectedMovieType);
                    makeTopRatedMoviesRequest();
                    return true;
                case R.id.action_filter_favorites:
                    this.selectedMovieType = MovieListType.FAVORITES;
                    saveSelectedMovieTypeInSharedPreferences(this.selectedMovieType);
                    makeFavoritesRequest();
                    return true;
            }
        }


        return super.onOptionsItemSelected(item);
    }

    private void saveSelectedMovieTypeInSharedPreferences(MovieListType selectedMovieIndex) {
        SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();

        switch (selectedMovieIndex) {
            case POPULAR:
                prefsEditor.putString("SELECTED_MOVIE_TYPE","POPULAR");
                break;
            case TOP_RATED:
                prefsEditor.putString("SELECTED_MOVIE_TYPE","TOP_RATED");
                break;
            case FAVORITES:
                prefsEditor.putString("SELECTED_MOVIE_TYPE","FAVORITES");
                break;
        }
        prefsEditor.commit();
    }

    private void loadSelectedMovieTypeFromSharedPreferences() {
        SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);
        String storedMovieType = mPrefs.getString("SELECTED_MOVIE_TYPE","POPULAR");

        switch (storedMovieType) {
            case "POPULAR":
                this.selectedMovieType = MovieListType.POPULAR;
                break;
            case "TOP_RATED":
                this.selectedMovieType = MovieListType.TOP_RATED;
                break;
            case "FAVORITES":
                this.selectedMovieType = MovieListType.FAVORITES;
                break;
        }

    }

    public void makeFavoritesRequest() {
        setSubtitleOnActionBar(null,getString(R.string.sorted_title)+" "+getString(R.string.action_filter_by_favorites));
        showLoader();

        MoviesManager.getInstance().setSelectedMovieType(this.selectedMovieType);

        Cursor c = getApplicationContext().getContentResolver().query(FavoriteMoviesContentProvider.FavoriteMovies.FAVORITE_MOVIES,
                null, null, null, null);
        Log.i("LOG", "cursor count: " + c.getCount());

        if (c == null || c.getCount() == 0){
            this.displayNoFavoritesMessage();
        }
        else {
            ArrayList<Movie> mArrayList = new ArrayList<Movie>();
            for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                Movie favoriteMovie = new Movie();
                favoriteMovie.setId(c.getInt(0));
                favoriteMovie.setTitle(c.getString(1));
                favoriteMovie.setPosterPath(c.getString(2));
                favoriteMovie.setOverview(c.getString(3));
                favoriteMovie.setVoteAverage(c.getInt(4));
                favoriteMovie.setReleaseDate(c.getString(5));
                mArrayList.add(favoriteMovie);
            }

            MovieList favoriteMovieList = new MovieList();
            favoriteMovieList.setMovies(mArrayList);

            this.showGrid();

            mAdapter = new GridViewAdapter(this);
            mAdapter.setMoviesInGrid(favoriteMovieList);
            movieGridView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();

        }
    }

    public void makePopularMoviesRequest(){
        setSubtitleOnActionBar(null,getString(R.string.sorted_title)+" "+getString(R.string.action_filter_by_popularity));
        showLoader();

        if (this.isOnline()){
            EventBus.getInstance().post(new GetPopularMoviesAction());
        }
        else {
            this.displayErrorMessage();
        }
    }

    public void setSubtitleOnActionBar(String title, String subtitle){
        if (getSupportActionBar() != null ){
            ActionBar actionBar = getSupportActionBar();
            if (title != null){
                actionBar.setTitle(title);
            }
            if (subtitle != null){
                actionBar.setSubtitle(subtitle);
            }
        }
    }


    public void makeTopRatedMoviesRequest(){
        setSubtitleOnActionBar(null,getString(R.string.sorted_title)+" "+getString(R.string.action_filter_by_top_rated));
        showLoader();

        if (this.isOnline()){
            EventBus.getInstance().post(new GetTopRatedMoviesAction());
        }
        else {
            this.displayErrorMessage();
        }
    }

    @Subscribe
    public void onPopularMoviesEvent(final PopularMoviesEvent event) {
        if (event.getPopularMoviesList().getMovies().size() > 0){
            showGrid();
            mAdapter.setMoviesInGrid(event.getPopularMoviesList());
            mAdapter.notifyDataSetChanged();
        }
        else {
            displayErrorMessage();
        }
    }

    @Subscribe
    public void onTopRatedMoviesEvent(final TopRatedMoviesEvent event){
        if (event.getTopRatedMovieList().getMovies().size() > 0){
            showGrid();
            mAdapter.setMoviesInGrid(event.getTopRatedMovieList());
            mAdapter.notifyDataSetChanged();
        }
        else {
            displayErrorMessage();
        }
    }

    public void displayErrorMessage(){
        this.loadingCircle.setVisibility(View.INVISIBLE);
        this.movieGridView.setVisibility(View.INVISIBLE);
        this.errorMessage.setVisibility(View.VISIBLE);
    }

    public void displayNoFavoritesMessage(){
        this.loadingCircle.setVisibility(View.INVISIBLE);
        this.movieGridView.setVisibility(View.INVISIBLE);
        this.errorMessage.setVisibility(View.VISIBLE);
    }

    public void showGrid(){
        this.loadingCircle.setVisibility(View.INVISIBLE);
        this.movieGridView.setVisibility(View.VISIBLE);
        this.errorMessage.setVisibility(View.INVISIBLE);
    }

    public void showLoader(){
        this.loadingCircle.setVisibility(View.VISIBLE);
        this.movieGridView.setVisibility(View.INVISIBLE);
        this.errorMessage.setVisibility(View.INVISIBLE);
    }
    @Override
    public void onListItemClick(int clickedItemIndex) {

        Context context = MovieListActivity.this;
        Class destinationActivity = MovieDetailActivity.class;
        Intent startChildActivityIntent = new Intent(context, destinationActivity);
        startChildActivityIntent.putExtra(Intent.EXTRA_TEXT, Integer.toString(clickedItemIndex));

        startActivity(startChildActivityIntent);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
