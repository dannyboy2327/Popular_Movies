package com.example.popularmovies.Classes;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@Entity(tableName = "Favorite_Movies")
public class MoviesResults implements Serializable {

    @Ignore
    @SerializedName("popularity")
    @Expose
    private Double popularity;

    @Ignore
    @SerializedName("vote_count")
    @Expose
    private Integer voteCount;

    @Ignore
    @SerializedName("video")
    @Expose
    private Boolean video;

    @ColumnInfo(name = "Poster_Url")
    @SerializedName("poster_path")
    @Expose
    private String posterPath;

    @PrimaryKey
    @ColumnInfo(name = "Movie_ID")
    @SerializedName("id")
    @Expose
    private Integer id;

    @Ignore
    @SerializedName("adult")
    @Expose
    private Boolean adult;

    @Ignore
    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;

    @Ignore
    @SerializedName("original_language")
    @Expose
    private String originalLanguage;

    @ColumnInfo(name = "Title")
    @SerializedName("original_title")
    @Expose
    private String originalTitle;

    @Ignore
    @SerializedName("genre_ids")
    @Expose
    private List<Integer> genreIds = null;

    @Ignore
    @SerializedName("title")
    @Expose
    private String title;

    @ColumnInfo(name = "Votes")
    @SerializedName("vote_average")
    @Expose
    private Double voteAverage;

    @ColumnInfo(name = "Synopsis")
    @SerializedName("overview")
    @Expose
    private String overview;

    @ColumnInfo(name = "Date_Released")
    @SerializedName("release_date")
    @Expose
    private String releaseDate;

    @ColumnInfo(name = "isFavorite")
    private boolean isFavorite;

    public MoviesResults(String posterPath, Integer id, String originalTitle, Double voteAverage, String overview, String releaseDate, boolean isFavorite) {
        this.posterPath = posterPath;
        this.id = id;
        this.originalTitle = originalTitle;
        this.voteAverage = voteAverage;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.isFavorite = isFavorite;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public Boolean getVideo() {
        return video;
    }

    public void setVideo(Boolean video) {
        this.video = video;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public String getBackdropPath() {
        return "https://image.tmdb.org/t/p/" + "w500" + backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
