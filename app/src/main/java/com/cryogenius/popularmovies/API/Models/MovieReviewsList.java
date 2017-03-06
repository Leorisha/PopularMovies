
package com.cryogenius.popularmovies.API.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieReviewsList {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("page")
    @Expose
    private Long page;
    @SerializedName("results")
    @Expose
    private List<MovieReview> reviews = null;
    @SerializedName("total_pages")
    @Expose
    private Long totalPages;
    @SerializedName("total_results")
    @Expose
    private Long totalResults;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPage() {
        return page;
    }

    public void setPage(Long page) {
        this.page = page;
    }

    public List<MovieReview> getReviews() {
        return reviews;
    }

    public void setReviews(List<MovieReview> reviews) {
        this.reviews = reviews;
    }

    public Long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Long totalPages) {
        this.totalPages = totalPages;
    }

    public Long getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Long totalResults) {
        this.totalResults = totalResults;
    }

}
