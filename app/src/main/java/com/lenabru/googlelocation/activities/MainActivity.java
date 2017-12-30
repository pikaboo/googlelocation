package com.lenabru.googlelocation.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;

import com.lenabru.googlelocation.R;
import com.lenabru.googlelocation.base.Action;
import com.lenabru.googlelocation.base.BaseActivity;
import com.lenabru.googlelocation.base.Constants;
import com.lenabru.googlelocation.fragments.PlaceInfoFragment;
import com.lenabru.googlelocation.fragments.PlacesListFragment;
import com.lenabru.googlelocation.fragments.UserInfoFragment;
import com.lenabru.googlelocation.fragments.RadiusMapFragment;
import com.lenabru.googlelocation.interfaces.HasCoordinates;
import com.lenabru.googlelocation.managers.DialogManager;
import com.lenabru.googlelocation.managers.OnPlaceSelectedListener;
import com.lenabru.googlelocation.managers.PermissionsManager;
import com.lenabru.googlelocation.models.PlacesResult;

public class MainActivity extends BaseActivity implements Constants,PermissionsManager.OnPermissionStatusUpdate, LocationListener, OnPlaceSelectedListener {

    private static final String RADIUS_MAP_FRAGMENT = "RadiusMapFragment";
    private static final String USER_INFO_FRAGMENT = "UserInfoFragment";
    private static final String PLACES_FRAGMENT = "PlacesFragment";

    private PermissionsManager permissionsManager;
    private DialogManager dialogManager;
    private LocationManager locationManager;

    private RadiusMapFragment radiusMapFragment;
    private UserInfoFragment userInfoFragment;
    private PlacesListFragment placesListFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            radiusMapFragment = (RadiusMapFragment) getSupportFragmentManager().getFragment(savedInstanceState, RADIUS_MAP_FRAGMENT);
            userInfoFragment = (UserInfoFragment) getSupportFragmentManager().getFragment(savedInstanceState, USER_INFO_FRAGMENT);
            placesListFragment = (PlacesListFragment) getSupportFragmentManager().getFragment(savedInstanceState,PLACES_FRAGMENT);
        } else {
            radiusMapFragment = new RadiusMapFragment();
            userInfoFragment = new UserInfoFragment();
            placesListFragment = new PlacesListFragment();
            getSupportFragmentManager().beginTransaction()//
                                        .replace(R.id.infoAddress, userInfoFragment)//
                                        .replace(R.id.main, radiusMapFragment)//
                                        .replace(R.id.placesList,placesListFragment)//
                                        .commit();

        }
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        permissionsManager = new PermissionsManager();
        dialogManager = new DialogManager();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        FragmentManager mgr = getSupportFragmentManager();
        mgr.putFragment(outState, RADIUS_MAP_FRAGMENT, radiusMapFragment);
        mgr.putFragment(outState, USER_INFO_FRAGMENT, userInfoFragment);
        mgr.putFragment(outState,PLACES_FRAGMENT,placesListFragment);
    }

    @Override
    protected void onResume() {
        super.onResume();
        permissionsManager.requestPermission(this, Manifest.permission.ACCESS_FINE_LOCATION, this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onPermissionGranted(String permission) {
        if (permission == Manifest.permission.ACCESS_FINE_LOCATION) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
    }

    @Override
    public void onShowPermissionRationale(final String permission, final PermissionsManager.OnPermissionRationaleUpdate update) {
        dialogManager.showAlert(this, getString(R.string.location_permission_rationale), new DialogManager.OnDialogButtonClick() {
            @Override
            public void onButtonClicked() {
                update.onPermissionRationaleContinue(MainActivity.this, permission);
            }
        });


    }

    @Override
    public void onPermissionDenied(final String permission) {
        dialogManager.showAlert(this, getString(R.string.location_permission_denied_settings), new DialogManager.OnDialogButtonClick() {
            @Override
            public void onButtonClicked() {
                permissionsManager.showPermissionSettings(MainActivity.this);
            }
        });

    }

    @Override
    public void onLocationChanged(Location location) {
        locationManager.removeUpdates(this);
        final double lattitude = location.getLatitude();
        final double longitude = location.getLongitude();
        sendEvent(HasCoordinates.class, new Action<HasCoordinates>() {
            @Override
            public void run(HasCoordinates listener) {
                listener.setCoordinates(lattitude,longitude);
            }
        });
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        if (s.equals(LocationManager.GPS_PROVIDER)) {
            locationManager.removeUpdates(this);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
    }

    @Override
    public void onProviderDisabled(String s) {

        //first choice
        if (s.equals(LocationManager.GPS_PROVIDER)) {
            locationManager.removeUpdates(this);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        }

        //2nd choice
        if(s.equals(LocationManager.NETWORK_PROVIDER)){
            dialogManager.showAlert(this, getString(R.string.no_gps_or_network), new DialogManager.OnDialogButtonClick() {
                @Override
                public void onButtonClicked() {

                }
            });
        }
    }

    @Override
    public void onPlaceSelected(PlacesResult place) {
        PlaceInfoFragment placeInfoFragment = new PlaceInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(PLACE,place);
        placeInfoFragment.setArguments(bundle);
        placeInfoFragment.show(getSupportFragmentManager(),"PlaceInfoFragment");
    }
}
