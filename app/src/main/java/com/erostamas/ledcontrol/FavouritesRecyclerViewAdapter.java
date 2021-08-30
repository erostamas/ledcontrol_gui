package com.erostamas.ledcontrol;

import android.content.Context;
import android.graphics.Color;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.erostamas.ledcontrol.ui.main.FavouritesFragment;

import java.util.ArrayList;

public class FavouritesRecyclerViewAdapter extends RecyclerView.Adapter<FavouriteItemViewHolder> {
    private ArrayList<RGBColor> _favouritesContainer;
    private LayoutInflater layoutInflater;
    private FavouritesFragment _favouritesFragment;

    // data is passed into the constructor
    public FavouritesRecyclerViewAdapter(Context context, ArrayList<RGBColor> data, FavouritesFragment favouritesFragment) {
        this.layoutInflater = LayoutInflater.from(context);
        this._favouritesContainer = data;
        this._favouritesFragment = favouritesFragment;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public FavouriteItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.favourite_item, parent, false);
        return new FavouriteItemViewHolder(view, _favouritesContainer, _favouritesFragment, this);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull FavouriteItemViewHolder holder, int position) {
        holder.rootView.setBackgroundColor(Color.rgb(_favouritesContainer.get(position)._red , _favouritesContainer.get(position)._green,  _favouritesContainer.get(position)._blue));
        holder.position = position;

    }

    // total number of cells
    @Override
    public int getItemCount() {
        return _favouritesContainer.size();
    }

    // convenience method for getting data at click position
    public RGBColor getItem(int id) {
        return _favouritesContainer.get(id);
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
