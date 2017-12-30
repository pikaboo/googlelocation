package com.lenabru.googlelocation.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lenabru.googlelocation.R;
import com.lenabru.googlelocation.base.BaseFragment;
import com.lenabru.googlelocation.interfaces.GoogleAddressesListener;
import com.lenabru.googlelocation.interfaces.HasCoordinates;
import com.lenabru.googlelocation.managers.GooglePlacesManager;
import com.lenabru.googlelocation.models.AddressResult;
import com.lenabru.googlelocation.models.GoogleAddresses;
import com.lenabru.googlelocation.models.Location;


/**
 * Created by Lena Brusilovski on 29/12/2017.
 */

public class UserInfoFragment extends BaseFragment implements HasCoordinates, GoogleAddressesListener {

    private static final String LAST_KNOWN_ADDRESS = "LastKnownAddress";

    private GooglePlacesManager googlePlacesManager;
    private GoogleAddresses lastKnownAddress;

    private TextView latLngTextView;
    private TextView addressTextView;
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
        View v = inflater.inflate(R.layout.user_info, container, false);
        latLngTextView = v.findViewById(R.id.latlng);
        addressTextView = v.findViewById(R.id.address);
        globeLayout = v.findViewById(R.id.globeLayout);
        foundYouLayout = v.findViewById(R.id.foundYouLayout);
        setSearching(true);
        return v;
    }

    @Override
    public void setCoordinates(double latitude, double longitude) {
        googlePlacesManager.getMyLocationInfo(latitude, longitude/*, this*/);
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
            setSearching(false);
        }

    }

    private void setSearching(boolean searching){
        globeLayout.setVisibility(searching? View.VISIBLE : View.GONE);
        foundYouLayout.setVisibility(searching? View.GONE : View.VISIBLE);
    }
}
