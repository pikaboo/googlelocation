package com.lenabru.googlelocation.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.lenabru.googlelocation.R;
import com.lenabru.googlelocation.interfaces.GoogleAddressesListener;
import com.lenabru.googlelocation.interfaces.HasCoordinates;
import com.lenabru.googlelocation.managers.GooglePlacesManager;
import com.lenabru.googlelocation.models.AddressResult;
import com.lenabru.googlelocation.models.GoogleAddresses;
import com.lenabru.googlelocation.models.Location;

import javax.microedition.khronos.opengles.GL;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Lena Brusilovski on 29/12/2017.
 */

public class MapInfoFragment extends Fragment implements HasCoordinates, GoogleAddressesListener {

    private static final String LAST_KNOWN_ADDRESS = "LastKnownAddress";
    private GooglePlacesManager googlePlacesManager;

    private TextView latLngTextView;
    private TextView addressTextView;

    private GoogleAddresses lastKnownAddress;
    private LatLng myPosition;
    private View globeLayout;
    private View foundYouLayout;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            lastKnownAddress = (GoogleAddresses) savedInstanceState.getSerializable(LAST_KNOWN_ADDRESS);
        }
        googlePlacesManager = new GooglePlacesManager();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onAddressReceived(lastKnownAddress);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.map_info, container, false);
        latLngTextView = v.findViewById(R.id.latlng);
        addressTextView = v.findViewById(R.id.address);
        globeLayout = v.findViewById(R.id.globeLayout);
        foundYouLayout = v.findViewById(R.id.foundYouLayout);
        foundYouLayout.setVisibility(View.INVISIBLE);
        return v;
    }

    @Override
    public void setCoordinates(double latitude, double longitude) {
        myPosition = new LatLng(latitude, longitude);
        googlePlacesManager.getMyLocationInfo(latitude, longitude, this);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(LAST_KNOWN_ADDRESS, lastKnownAddress);
    }

    @Override
    public void onAddressReceived(GoogleAddresses addresses) {
        if (addresses != null) {
            lastKnownAddress = addresses;
            //assuming the user is at the first address found
            AddressResult result = addresses.getResults().get(0);
            Location location = result.getGeometry().getLocation();
            latLngTextView.setText("LatLng:" + location.getLat() + "," + location.getLng());
            addressTextView.setText(result.getFormattedAddress());
            globeLayout.setVisibility(View.GONE);
            foundYouLayout.setVisibility(View.VISIBLE);
        }

    }
}
