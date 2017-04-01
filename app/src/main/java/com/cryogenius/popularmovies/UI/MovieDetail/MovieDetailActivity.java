package com.cryogenius.popularmovies.UI.MovieDetail;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.cryogenius.popularmovies.API.Models.Movie;
import com.cryogenius.popularmovies.API.Models.MovieListType;
import com.cryogenius.popularmovies.Bus.EventBus;
import com.cryogenius.popularmovies.Bus.Messages.Events.MovieDetailEvent;
import com.cryogenius.popularmovies.DB.FavoriteMovieEntry;
import com.cryogenius.popularmovies.DB.FavoriteMoviesContentProvider;
import com.cryogenius.popularmovies.R;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.io.Serializable;

/**
 * Created by Ana Neto on 26/01/2017.
 */

public class MovieDetailActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    private int selectedIndex;
    private Movie selectedMovie;
    private int selectedTabIndex = -1;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private PagerAdapter viewAdapter;
    private ImageView moviePoster;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton favoriteButton;

    private static final String LIFECYCLE_MOVIE = "selected_movie";
    private static final String LIFECYCLE_MOVIE_INDEX = "selected_movie_index";
    private static final String LIFECYCLE_TAB_INDEX = "selected_tab_index";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
            String selectedIndexInString = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
            this.selectedIndex = Integer.parseInt(selectedIndexInString);
        }

        if(savedInstanceState != null){
            if(savedInstanceState.containsKey(LIFECYCLE_MOVIE)){
                this.selectedMovie = savedInstanceState.getParcelable(LIFECYCLE_MOVIE);
            }

            if(savedInstanceState.containsKey(LIFECYCLE_MOVIE_INDEX)){
                this.selectedIndex = savedInstanceState.getInt(LIFECYCLE_MOVIE_INDEX);
            }

            if (savedInstanceState.containsKey(LIFECYCLE_TAB_INDEX)){
                this.selectedTabIndex = savedInstanceState.getInt(LIFECYCLE_TAB_INDEX);
            }
        }

        moviePoster = (ImageView) findViewById(R.id.iv_movie_detail_poster);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.pager);
        favoriteButton = (FloatingActionButton)findViewById(R.id.favorite_button);
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!favoriteButton.isSelected()){
                    favoriteButton.setSelected(true);
                    favoriteButton.setImageDrawable( getResources().getDrawable(R.drawable.ic_favorite_selected));

                    ContentValues cv = new ContentValues();
                    cv.put(FavoriteMovieEntry._ID,selectedMovie.getId());
                    cv.put(FavoriteMovieEntry.TITLE,selectedMovie.getTitle());

                    ContentResolver resolver = getContentResolver();
                    resolver.insert(FavoriteMoviesContentProvider.FavoriteMovies.FAVORITE_MOVIES,cv);

                    Cursor c = getApplicationContext().getContentResolver().query(FavoriteMoviesContentProvider.FavoriteMovies.FAVORITE_MOVIES,
                            null, null, null, null);
                    Log.i("LOG", "cursor count: " + c.getCount());
                    if (c == null || c.getCount() == 0){
                    }

                }
                else {

                    getContentResolver().delete(FavoriteMoviesContentProvider.FavoriteMovies.withId(selectedMovie.getId()),null,null);

                    Cursor c = getApplicationContext().getContentResolver().query(FavoriteMoviesContentProvider.FavoriteMovies.FAVORITE_MOVIES,
                            null, null, null, null);
                    Log.i("LOG", "cursor count: " + c.getCount());
                    if (c == null || c.getCount() == 0){
                    }


                    favoriteButton.setSelected(false);
                    favoriteButton.setImageDrawable( getResources().getDrawable(R.drawable.ic_favorite_unselected));
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(LIFECYCLE_TAB_INDEX, this.selectedTabIndex);
        outState.putParcelable(LIFECYCLE_MOVIE, this.selectedMovie);
        outState.putSerializable(LIFECYCLE_MOVIE_INDEX,this.selectedIndex);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getInstance().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (this.selectedIndex >= 0) {

            viewAdapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(),this.selectedIndex);
            viewPager.setAdapter(viewAdapter);
            viewPager.setOffscreenPageLimit(3);
            tabLayout.addOnTabSelectedListener(this);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    selectedTabIndex = position;
                    TabLayout.Tab tab = tabLayout.getTabAt(position);
                    tab.select();
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            //EventBus.getInstance().post(new GetMovieDetailAction(this.selectedIndex));
        } else {
            //TODO: display error
        }
    }

    @Override
    public void onStop() {
        EventBus.getInstance().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onMovieDetailEvent(final MovieDetailEvent event) {
        if (event.getSelectedMovie() != null) {

            Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);

            selectedMovie = event.getSelectedMovie();

            Cursor c = getContentResolver().query(FavoriteMoviesContentProvider.FavoriteMovies.withId(selectedMovie.getId()),null,null,null,null);

            if (c.getCount() > 0) {
                favoriteButton.setSelected(true);
                favoriteButton.setImageDrawable( getResources().getDrawable(R.drawable.ic_favorite_selected));
            }
            else {
                favoriteButton.setSelected(false);
                favoriteButton.setImageDrawable( getResources().getDrawable(R.drawable.ic_favorite_unselected));
            }

            collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
            collapsingToolbarLayout.setTitle(event.getSelectedMovie().getOriginalTitle());
            actionBar.setTitle(event.getSelectedMovie().getOriginalTitle());

            Context context = moviePoster.getContext();

            String posterURL = context.getString(R.string.image_url_detail) + event.getSelectedMovie().getPosterPath();
            Picasso.with(context).load(posterURL).into(moviePoster);

            if (viewAdapter != null){
                if (this.selectedTabIndex >= 0) {
                    TabLayout.Tab tab = tabLayout.getTabAt(this.selectedTabIndex);
                    tab.select();
                }
                else {
                    viewAdapter.setSelectedId(event.getSelectedMovie().getId());
                    viewAdapter.notifyDataSetChanged();
                    viewPager.invalidate();

                }

            }

        } else {
            //TODO: error
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

}
