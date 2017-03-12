
package com.cryogenius.popularmovies.API.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieList implements Parcelable {

    @SerializedName("page")
    @Expose
    private int page;
    @SerializedName("results")
    @Expose
    private List<Movie> movies = null;
    @SerializedName("total_results")
    @Expose
    private int totalResults;
    @SerializedName("total_pages")
    @Expose
    private int totalPages;

    public MovieList() {

    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    //Parcelable implementation

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(page);
        out.writeInt(totalResults);
        out.writeInt(totalPages);
        out.writeTypedList(movies);
    }

    public static final Parcelable.Creator<MovieList> CREATOR
            = new Parcelable.Creator<MovieList>() {
        public MovieList createFromParcel(Parcel in) {
            return new MovieList(in);
        }

        public MovieList[] newArray(int size) {
            return new MovieList[size];
        }
    };

    public MovieList(Parcel in) {
        page = in.readInt();
        totalResults = in.readInt();
        totalPages = in.readInt();
        in.readList(this.movies, Movie.class.getClassLoader());
    }
}
