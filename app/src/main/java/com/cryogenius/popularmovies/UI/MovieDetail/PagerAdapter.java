package com.cryogenius.popularmovies.UI.MovieDetail;

/**
 * Created by Ana Neto on 05/03/2017.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.cryogenius.popularmovies.UI.MovieDetail.Reviews.ReviewsFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private int tabCount;
    private int selectedId;
    private int selectedIndex;
    private InfoFragment tab1;
    private TrailersFragment tab2;
    private ReviewsFragment tab3;

    //Constructor to the class
    public PagerAdapter(FragmentManager fm, int tabCount, int selectedIndex) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;
        this.selectedIndex = selectedIndex;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                tab1 = new InfoFragment();
                tab1.setSelectedIndex(selectedIndex);
                return tab1;
            case 1:
                tab2  = new TrailersFragment();
                tab2.setSelectedMovieId(selectedId);
                return tab2;
            case 2:
                tab3 = new ReviewsFragment();
                tab3.setSelectedMovieId(selectedId);
                return tab3;
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    public void setSelectedId(int selectedId) {
        this.selectedId = selectedId;
    }
}