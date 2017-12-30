package com.lenabru.googlelocation.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.lenabru.googlelocation.R;
import com.lenabru.googlelocation.base.Action;
import com.lenabru.googlelocation.managers.EventBusManager;
import com.lenabru.googlelocation.managers.OnPlaceSelectedListener;
import com.lenabru.googlelocation.models.PlacesResult;
import com.lenabru.googlelocation.viewholders.PlacesViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lena Brusilovski on 30/12/2017.
 */

public class PlacesAdapter extends RecyclerView.Adapter<PlacesViewHolder> {

    private List<PlacesResult> placesResultList = new ArrayList<>();

    @Override
    public PlacesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        final PlacesViewHolder vh = new PlacesViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBusManager.getInstance().sendEvent(OnPlaceSelectedListener.class, new Action<OnPlaceSelectedListener>() {
                    @Override
                    public void run(OnPlaceSelectedListener listener) {
                        listener.onPlaceSelected(placesResultList.get(vh.getAdapterPosition()));
                    }
                });
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(PlacesViewHolder holder, int position) {
        PlacesResult result = placesResultList.get(position);
        holder.name.setText(result.getName());
        holder.vicinity.setText(result.getVicinity());
        if (result.getIcon() != null) {
            Glide.with(holder.icon).load(result.getIcon()).into(holder.icon);
        }

    }

    @Override
    public int getItemCount() {
        return placesResultList.size();
    }

    public void setPlaces(List<PlacesResult> places) {
        placesResultList.clear();
        placesResultList.addAll(places);
    }


}
