package com.lenabru.googlelocation.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lenabru.googlelocation.R;
import com.lenabru.googlelocation.adapters.PlacesAdapter;
import com.lenabru.googlelocation.base.BaseFragment;
import com.lenabru.googlelocation.interfaces.GooglePlacesListener;
import com.lenabru.googlelocation.models.GooglePlaces;
import com.lenabru.googlelocation.models.PlacesResult;

import java.util.List;

/**
 * Created by Lena Brusilovski on 30/12/2017.
 */

public class PlacesListFragment extends BaseFragment implements GooglePlacesListener {


    private static final String PLACE_RESULTS = "PlaceResults";
    private RecyclerView recyclerView;
    private PlacesAdapter adapter = new PlacesAdapter();
    private GooglePlaces places;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.place_list,container,false);
        recyclerView = v.findViewById(R.id.placesList);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState!=null){
            places = (GooglePlaces) savedInstanceState.get(PLACE_RESULTS);
        }
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        if(places!=null){
            onGooglePlacesReceived(places);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(PLACE_RESULTS,places);
    }

    @Override
    public void onGooglePlacesReceived(GooglePlaces places) {
        this.places = places;
        adapter.setPlaces(places.getResults());
        adapter.notifyDataSetChanged();
    }

}
