package com.lenabru.googlelocation.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lenabru.googlelocation.R;
import com.lenabru.googlelocation.base.Action;
import com.lenabru.googlelocation.base.BaseFragment;
import com.lenabru.googlelocation.interfaces.GooglePlacesListener;
import com.lenabru.googlelocation.interfaces.HasCoordinates;
import com.lenabru.googlelocation.managers.DialogManager;
import com.lenabru.googlelocation.managers.GooglePlacesManager;
import com.lenabru.googlelocation.managers.OnPlaceSelectedListener;
import com.lenabru.googlelocation.models.GooglePlaces;
import com.lenabru.googlelocation.models.Location;
import com.lenabru.googlelocation.models.MapUpdate;
import com.lenabru.googlelocation.models.PlacesResult;

/**
 * Created by Lena Brusilovski on 26/12/2017.
 */

public class MapFragment extends BaseFragment implements OnMapReadyCallback, HasCoordinates, GooglePlacesListener, RadiusFragment.OnRadiusChangedListener {

    private static final String MAP_FRAGMENT = "MapFragment";
    private static final String MAP_UPDATE = "MAP_UPDATE";

    private GoogleMap map;
    private MapUpdate lastMapUpdate;

    private GooglePlacesManager googlePlacesManager;
    private DialogManager dialogManager;

    private SupportMapFragment mapFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        googlePlacesManager = new GooglePlacesManager();
        lastMapUpdate = new MapUpdate(32.086838, 34.802212, 500, null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.map_fragment, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mapFragment = (SupportMapFragment) getChildFragmentManager().getFragment(savedInstanceState, MAP_FRAGMENT);
            lastMapUpdate = (MapUpdate) savedInstanceState.getSerializable(MAP_UPDATE);
        } else {
            mapFragment = SupportMapFragment.newInstance();

            getChildFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
        dialogManager = new DialogManager();

    }

    @Override
    public void onResume() {
        super.onResume();
        performMapUpdate();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getChildFragmentManager().putFragment(outState, MAP_FRAGMENT, mapFragment);
        outState.putSerializable(MAP_UPDATE, lastMapUpdate);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                final PlacesResult placesResult = (PlacesResult) marker.getTag();
                if (placesResult != null) {
                    sendEvent(OnPlaceSelectedListener.class, new Action<OnPlaceSelectedListener>() {
                        @Override
                        public void run(OnPlaceSelectedListener listener) {
                            listener.onPlaceSelected(placesResult);
                        }
                    });
                    return true;
                }

                return false;
            }
        });
        performMapUpdate();
    }

    @Override
    public void setCoordinates(double latitude, double longitude) {
        lastMapUpdate.setLatitude(latitude);
        lastMapUpdate.setLongitude(longitude);
        performMapUpdate();
        googlePlacesManager.getGooglePlaces(latitude, longitude, lastMapUpdate.getRadius());

    }

    private void addMyPositionMarker() {
        MarkerOptions options = new MarkerOptions().position(new LatLng(lastMapUpdate.getLatitude(), lastMapUpdate.getLongitude())).title("You are here").icon(BitmapDescriptorFactory.fromResource(R.drawable.micky));
        map.addMarker(options);
    }

    @Override
    public void onGooglePlacesReceived(GooglePlaces places) {
        //If there are places to display or no results update the map
        if (places.getStatus().equals("OK") || places.getStatus().equals("ZERO_RESULTS")) {
            lastMapUpdate.setPlaceMarkers(places.getResults());
            performMapUpdate();
            if (places.getStatus().equals("ZERO_RESULTS")) {
                dialogManager.showAlert(getActivity(), getString(R.string.no_places_nearby), new DialogManager.OnDialogButtonClick() {
                    @Override
                    public void onButtonClicked() {

                    }
                });
            }
        } else {
            //display error dialog if there is an error
            //possible error OVER_QUERY_LIMIT
            dialogManager.showAlert(getActivity(), places.getStatus(), new DialogManager.OnDialogButtonClick() {
                @Override
                public void onButtonClicked() {

                }
            });
        }
    }

    private void performMapUpdate() {
        if (map == null) {
            mapFragment.getMapAsync(this);
        } else {
            map.clear();
            addMyPositionMarker();
            if (lastMapUpdate.getPlaceMarkers() != null && lastMapUpdate.getPlaceMarkers().size() > 0) {
                for (PlacesResult result : lastMapUpdate.getPlaceMarkers()) {
                    Location geometry = result.getGeometry().getLocation();
                    LatLng markerCoordinates = new LatLng(geometry.getLat(), geometry.getLng());
                    MarkerOptions options = new MarkerOptions().position(markerCoordinates)//
                            .title(result.getName())//
                            .snippet(result.getVicinity());
                    Marker marker = map.addMarker(options);
                    marker.setTag(result);
                }
            }
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastMapUpdate.getLatitude(), lastMapUpdate.getLongitude()), 15));
        }
    }

    @Override
    public void onRadiusChanged(int radius) {
        lastMapUpdate.setRadius(radius);
        googlePlacesManager.getGooglePlaces(lastMapUpdate.getLatitude(), lastMapUpdate.getLongitude(), radius/*,this*/);
    }
}
