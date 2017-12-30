package com.lenabru.googlelocation.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Created by Lena Brusilovski on 26/12/2017.
 */


public class GooglePlaces implements Serializable {

    @SerializedName("results")
    @Expose
    private List<PlacesResult> results = new ArrayList<PlacesResult>();
    @SerializedName("status")
    @Expose
    private String status;

    public List<PlacesResult> getResults() {
        return results;
    }

    public void setResults(List<PlacesResult> results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}