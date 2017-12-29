package com.lenabru.googlelocation.interfaces;

import com.lenabru.googlelocation.models.GooglePlaces;

/**
 * Created by Lena Brusilovski on 26/12/2017.
 */

public interface GooglePlacesListener {

    void onGooglePlacesReceived(GooglePlaces places);
    void onError(String error);
}
