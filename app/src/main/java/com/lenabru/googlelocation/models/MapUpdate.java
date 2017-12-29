package com.lenabru.googlelocation.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Lena Brusilovski on 29/12/2017.
 */

public class MapUpdate implements Serializable {
    private double latitude;
    private double longitude;
    private List<PlacesResult> placeMarkers;
    private int radius;

    public int getRadius() {
        return radius;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public List<PlacesResult> getPlaceMarkers() {
        return placeMarkers;
    }

    public MapUpdate(double latitude, double longitude,int radius,List<PlacesResult>placeMarkers){
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        this.placeMarkers = placeMarkers;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setPlaceMarkers(List<PlacesResult> placeMarkers) {
        this.placeMarkers = placeMarkers;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}
