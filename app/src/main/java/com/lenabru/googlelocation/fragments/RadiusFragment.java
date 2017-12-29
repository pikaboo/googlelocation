package com.lenabru.googlelocation.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.lenabru.googlelocation.R;

/**
 * Created by Lena Brusilovski on 29/12/2017.
 */

public class RadiusFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {


    public interface OnRadiusChangedListener {
        void onRadiusChanged(int radius);
    }

    private static final int RADIUS_INCREMENT = 500;
    private static final int MAX_DISTANCE_HALF_KM = 10;

    private SeekBar radius;
    private TextView radiusTitle;
    private TextView radiusContent;

    private OnRadiusChangedListener radiusChangedListener;
    private final OnRadiusChangedListener EMPTY_RADIUS_LISTENER = new OnRadiusChangedListener() {
        @Override
        public void onRadiusChanged(int radius) {
            //empty implementation
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.radius_layout, container, false);
        radius = v.findViewById(R.id.radius);
        radiusTitle = v.findViewById(R.id.radiusTitle);
        radiusContent = v.findViewById(R.id.radiusContent);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        radiusTitle.setText(R.string.radius);
        radiusContent.setText(RADIUS_INCREMENT + "");
        radius.setMax(MAX_DISTANCE_HALF_KM * RADIUS_INCREMENT);
        radius.setOnSeekBarChangeListener(this);
    }


    public OnRadiusChangedListener getRadiusChangedListener() {
        return radiusChangedListener != null ? radiusChangedListener : EMPTY_RADIUS_LISTENER;
    }

    public void setRadiusChangedListener(OnRadiusChangedListener radiusChangedListener) {
        this.radiusChangedListener = radiusChangedListener;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        int progress = getRadiusFromSeekbar(seekBar); //minimum of RADIUS_INCREMENT distance
        radiusContent.setText(progress + " " + getString(R.string.meters));
    }

    private int getRadiusFromSeekbar(SeekBar seekBar) {
        return seekBar.getProgress() + RADIUS_INCREMENT;
    }


    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int progress = getRadiusFromSeekbar(seekBar);
        getRadiusChangedListener().onRadiusChanged(progress);

    }

}
