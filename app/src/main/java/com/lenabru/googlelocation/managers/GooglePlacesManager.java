package com.lenabru.googlelocation.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lenabru.googlelocation.BuildConfig;
import com.lenabru.googlelocation.interfaces.GoogleAddressesListener;
import com.lenabru.googlelocation.interfaces.GooglePlacesListener;
import com.lenabru.googlelocation.models.AddressResult;
import com.lenabru.googlelocation.models.GoogleAddresses;
import com.lenabru.googlelocation.models.GooglePlaces;
import com.lenabru.googlelocation.webservice.GoogleWebService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Lena Brusilovski on 26/12/2017.
 */

public class GooglePlacesManager {

    private static final String BASE_URL = "https://maps.googleapis.com";
    private static final String API_KEY = BuildConfig.API_KEY;
    private GoogleWebService service;

    public GooglePlacesManager() {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        service = retrofit.create(GoogleWebService.class);
    }

    public void getGooglePlaces(Double latitude, Double longitude, Integer radius, final GooglePlacesListener listener) {
        String latlngString = String.format("%s,%s", latitude, longitude);
        Call<GooglePlaces> call = service.getPlacesNearby(latlngString, radius, API_KEY);
        call.enqueue(new Callback<GooglePlaces>() {
            @Override
            public void onResponse(Call<GooglePlaces> call, Response<GooglePlaces> response) {
                if (listener != null) {
                    listener.onGooglePlacesReceived(response.body());
                }
            }

            @Override
            public void onFailure(Call<GooglePlaces> call, Throwable t) {
                if (listener != null) {
                    listener.onGooglePlacesReceived(null);
                }
            }
        });
    }

    public void getMyLocationInfo(Double latitude, Double longitude, final GoogleAddressesListener listener) {
        String latlngString = String.format("%s,%s", latitude, longitude);
        Call<GoogleAddresses> call = service.getAddress(latlngString, API_KEY);
        call.enqueue(new Callback<GoogleAddresses>() {
            @Override
            public void onResponse(Call<GoogleAddresses> call, Response<GoogleAddresses> response) {
                if (listener != null) {
                    listener.onAddressReceived(response.body());
                }
            }

            @Override
            public void onFailure(Call<GoogleAddresses> call, Throwable t) {
                if (listener != null) {
                    listener.onAddressReceived(null);
                }
            }
        });
    }
}
