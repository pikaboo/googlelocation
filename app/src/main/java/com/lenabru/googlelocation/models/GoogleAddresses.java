package com.lenabru.googlelocation.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Lena Brusilovski on 29/12/2017.
 */

public class GoogleAddresses implements Serializable{

    @SerializedName("results")
    @Expose
    private List<AddressResult> results = null;
    @SerializedName("status")
    @Expose
    private String status;

    public List<AddressResult> getResults() {
        return results;
    }

    public void setResults(List<AddressResult> results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
