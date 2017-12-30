package com.lenabru.googlelocation.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lenabru.googlelocation.BuildConfig;
import com.lenabru.googlelocation.base.Action;
import com.lenabru.googlelocation.base.BaseManager;
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

public class GooglePlacesManager extends BaseManager{

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

    public void getGooglePlaces(Double latitude, Double longitude, Integer radius) {
        String latlngString = String.format("%s,%s", latitude, longitude);
        Call<GooglePlaces> call = service.getPlacesNearby(latlngString, radius, API_KEY);
        call.enqueue(new Callback<GooglePlaces>() {
            @Override
            public void onResponse(Call<GooglePlaces> call, final Response<GooglePlaces> response) {
                sendEvent(GooglePlacesListener.class, new Action<GooglePlacesListener>() {
                    @Override
                    public void run(GooglePlacesListener listener) {
                        listener.onGooglePlacesReceived(response.body());
                    }
                });
            }

            @Override
            public void onFailure(Call<GooglePlaces> call, Throwable t) {
                sendEvent(GooglePlacesListener.class, new Action<GooglePlacesListener>() {
                    @Override
                    public void run(GooglePlacesListener listener) {
                        listener.onGooglePlacesReceived(null);
                    }
                });
            }
        });
    }

    public void getMyLocationInfo(Double latitude, Double longitude) {
        String latlngString = String.format("%s,%s", latitude, longitude);
        Call<GoogleAddresses> call = service.getAddress(latlngString, API_KEY);
        call.enqueue(new Callback<GoogleAddresses>() {
            @Override
            public void onResponse(Call<GoogleAddresses> call, final Response<GoogleAddresses> response) {
                sendEvent(GoogleAddressesListener.class, new Action<GoogleAddressesListener>() {
                    @Override
                    public void run(GoogleAddressesListener listener) {
                        listener.onAddressReceived(response.body());
                    }
                });
            }

            @Override
            public void onFailure(Call<GoogleAddresses> call, Throwable t) {
                sendEvent(GoogleAddressesListener.class, new Action<GoogleAddressesListener>() {
                    @Override
                    public void run(GoogleAddressesListener listener) {
                        listener.onAddressReceived(null);
                    }
                });
            }
        });
    }

    public String getPlacePhotoURL(String photoReference){
        String url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=%s&key=%s";
        return String.format(url,photoReference,API_KEY);
    }
}
