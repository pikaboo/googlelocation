package com.lenabru.googlelocation.webservice;

import com.lenabru.googlelocation.models.AddressResult;
import com.lenabru.googlelocation.models.GoogleAddresses;
import com.lenabru.googlelocation.models.GooglePlaces;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryName;

/**
 * Created by Lena Brusilovski on 26/12/2017.
 */

public interface GoogleWebService {

    @GET("maps/api/place/nearbysearch/json")
    Call<GooglePlaces> getPlacesNearby(@Query("location")String location,@Query("radius")Integer radius,@Query("key") String key);

    @GET("maps/api/geocode/json")
    Call<GoogleAddresses>getAddress(@Query("latlng") String latlng, @Query("key") String key);

}


