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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lenabru.googlelocation.R;
import com.lenabru.googlelocation.fragments.MapInfoFragment;
import com.lenabru.googlelocation.fragments.RadiusMapFragment;
import com.lenabru.googlelocation.managers.DialogManager;
import com.lenabru.googlelocation.managers.PermissionsManager;

public class MainActivity extends AppCompatActivity implements PermissionsManager.OnPermissionStatusUpdate, LocationListener {

    private static final String RADIUS_MAP_FRAGMENT = "RadiusMapFragment";
    private static final String MAP_INFO_FRAGMENT = "MapInfoFragment";

    private PermissionsManager permissionsManager;
    private DialogManager dialogManager;
    private LocationManager locationManager;
    private RadiusMapFragment radiusMapFragment;
    private MapInfoFragment mapInfoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            radiusMapFragment = (RadiusMapFragment) getSupportFragmentManager().getFragment(savedInstanceState, RADIUS_MAP_FRAGMENT);
            mapInfoFragment = (MapInfoFragment) getSupportFragmentManager().getFragment(savedInstanceState, MAP_INFO_FRAGMENT);
        } else {
            radiusMapFragment = new RadiusMapFragment();
            mapInfoFragment = new MapInfoFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.infoAddress, mapInfoFragment).replace(R.id.main, radiusMapFragment).commit();

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
        mgr.putFragment(outState, MAP_INFO_FRAGMENT, mapInfoFragment);
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
        double lattitude = location.getLatitude();
        double longitude = location.getLongitude();
        radiusMapFragment.setCoordinates(lattitude, longitude);
        mapInfoFragment.setCoordinates(lattitude, longitude);
        locationManager.removeUpdates(this);
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
}
