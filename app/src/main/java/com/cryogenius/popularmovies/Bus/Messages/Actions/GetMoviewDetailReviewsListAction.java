package com.cryogenius.popularmovies.Bus.Messages.Actions;

/**
 * Created by Ana Neto on 01/03/2017.
 */

public class GetMoviewDetailReviewsListAction {

    private int selectedIndex;

    public GetMoviewDetailReviewsListAction(int selectedIndex){
        this.selectedIndex = selectedIndex;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }
}