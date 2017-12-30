package com.lenabru.googlelocation.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lenabru.googlelocation.R;

/**
 * Created by Lena Brusilovski on 30/12/2017.
 */

public class PlacesViewHolder extends RecyclerView.ViewHolder {

    public TextView  name;
    public TextView vicinity;
    public ImageView icon;

    public PlacesViewHolder(View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.name);
        vicinity = itemView.findViewById(R.id.vicinity);
        icon = itemView.findViewById(R.id.icon);
    }



}
