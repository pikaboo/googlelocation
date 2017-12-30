package com.lenabru.googlelocation.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.lenabru.googlelocation.R;
import com.lenabru.googlelocation.base.Action;
import com.lenabru.googlelocation.base.BaseFragment;

/**
 * Created by Lena Brusilovski on 29/12/2017.
 */

public class RadiusFragment extends BaseFragment implements SeekBar.OnSeekBarChangeListener {


    public interface OnRadiusChangedListener {
        void onRadiusChanged(int radius);
    }

    private static final int RADIUS_INCREMENT = 500;
    private static final int MAX_DISTANCE_HALF_KM = 10;

    private SeekBar radius;
    private TextView radiusTitle;
    private TextView radiusContent;


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
        final int progress = getRadiusFromSeekbar(seekBar);
        sendEvent(OnRadiusChangedListener.class, new Action<OnRadiusChangedListener>() {
            @Override
            public void run(OnRadiusChangedListener listener) {
                listener.onRadiusChanged(progress);
            }
        });
    }

}
