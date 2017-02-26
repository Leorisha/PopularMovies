package com.cryogenius.popularmovies.Bus.Messages.Actions;

/**
 * Created by Ana Neto on 29/01/2017.
 */

public class GetMovieDetailAction {

    private int selectedIndex;

    public GetMovieDetailAction(int selectedIndex){
        this.selectedIndex = selectedIndex;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }
}
