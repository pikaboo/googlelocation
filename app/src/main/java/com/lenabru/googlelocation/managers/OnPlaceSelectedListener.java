package com.lenabru.googlelocation.managers;

import com.google.android.gms.location.places.PlaceReport;
import com.lenabru.googlelocation.models.PlacesResult;

/**
 * Created by Lena Brusilovski on 30/12/2017.
 */

public interface OnPlaceSelectedListener {

    void onPlaceSelected(PlacesResult place);
}
