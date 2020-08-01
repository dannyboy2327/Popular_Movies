package com.example.popularmovies.Classes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Trailers {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<TrailersResults> results = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<TrailersResults> getResults() {
        return results;
    }

    public void setResults(List<TrailersResults> results) {
        this.results = results;
    }
}
