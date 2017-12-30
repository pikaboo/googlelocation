package com.lenabru.googlelocation.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lenabru.googlelocation.R;
import com.lenabru.googlelocation.base.BaseFragment;
import com.lenabru.googlelocation.interfaces.HasCoordinates;

/**
 * Created by Lena Brusilovski on 29/12/2017.
 */

public class RadiusMapFragment extends BaseFragment {


    private static final String MAP_FRAGMENT = "MapFragment";
    private static final String RADIUS_FRAGNENT = "RadiusFragment";

    private MapFragment mapFragment;
    private RadiusFragment radiusFragment;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.radius_map, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            mapFragment = new MapFragment();
            radiusFragment = new RadiusFragment();
            getChildFragmentManager().beginTransaction().replace(R.id.mapFragment, mapFragment).replace(R.id.layoutRadius, radiusFragment).commit();
        } else {
            mapFragment = (MapFragment) getChildFragmentManager().getFragment(savedInstanceState, MAP_FRAGMENT);
            radiusFragment = (RadiusFragment) getChildFragmentManager().getFragment(savedInstanceState, RADIUS_FRAGNENT);
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getChildFragmentManager().putFragment(outState, MAP_FRAGMENT, mapFragment);
        getChildFragmentManager().putFragment(outState, RADIUS_FRAGNENT, radiusFragment);
    }

}
