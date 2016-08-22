package com.example.nirajmarkandey.popularmovies.model;

/**
 * Created by niraj.markandey on 11/08/16.
 */
public class MovieModel {
    String originalTitle,overView,posterPath,releaseDate,voteAverage;

    public MovieModel(String originalTitle, String overView, String posterPath, String releaseDate, String voteAverage) {
        this.originalTitle = originalTitle;
        this.overView = overView;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
    }

    public MovieModel() {
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOverView() {
        return overView;
    }

    public void setOverView(String overView) {
        this.overView = overView;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }
}
