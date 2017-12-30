package com.lenabru.googlelocation.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lenabru.googlelocation.R;
import com.lenabru.googlelocation.base.Constants;
import com.lenabru.googlelocation.managers.GooglePlacesManager;
import com.lenabru.googlelocation.models.PlacesResult;

/**
 * Created by Lena Brusilovski on 30/12/2017.
 */

public class PlaceInfoFragment extends DialogFragment implements Constants {

    private PlacesResult place;

    private GooglePlacesManager googlePlacesManager;

    private ImageView photo;
    private TextView title;
    private TextView vicinity;
    private TextView openingHours;

    private String getPhotoURL() {
        if (place.getPhotos() != null && place.getPhotos().size() > 0) {
            return googlePlacesManager.getPlacePhotoURL(place.getPhotos().get(0).getPhotoReference());
        }

        return "http://surplusgain.com/sellorbuy/showcase/img-not-available.png";
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        googlePlacesManager = new GooglePlacesManager();

        if (getArguments() != null) {
            place = (PlacesResult) getArguments().get(PLACE);
        } else {
            if (savedInstanceState != null) {
                place = (PlacesResult) savedInstanceState.get(PLACE);
            }
        }
        setCancelable(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(PLACE, place);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.place_info, container, false);
        photo = v.findViewById(R.id.photo);
        title = v.findViewById(R.id.title);
        vicinity = v.findViewById(R.id.vicinity);
        openingHours = v.findViewById(R.id.openHours);
        Glide.with(photo).asGif().load(R.drawable.earth_spinning_rotating_animation_25).into(photo);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Glide.with(getActivity()).load(getPhotoURL()).into(photo);
        title.setText(place.getName());
        vicinity.setText(place.getVicinity());
        boolean openNow = false;
        if (place.getOpeningHours() != null) {
            openNow = place.getOpeningHours().getOpenNow();
        }
        openingHours.setText(getString(R.string.opening_hours, getString(openNow ? R.string.yes : R.string.no)));
    }


}
