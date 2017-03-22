package com.cryogenius.popularmovies.DB;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

/**
 * Created by Ana Neto on 19/03/2017.
 */

public interface FavoriteMovieEntry {

    @DataType(INTEGER) @PrimaryKey String _ID = "_id";
    @DataType(TEXT) @NotNull String TITLE = "title";

    //movie poster, synopsis, user rating, and release date

}
