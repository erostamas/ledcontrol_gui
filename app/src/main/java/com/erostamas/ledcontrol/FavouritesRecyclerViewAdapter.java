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

public class FavouritesRecyclerViewAdapter extends RecyclerView.Adapter<FavouritesRecyclerViewAdapter.ViewHolder> {

    private ArrayList<RGBColor> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private FavouritesFragment favouritesFragment;

    // data is passed into the constructor
    public FavouritesRecyclerViewAdapter(Context context, ArrayList<RGBColor> data, FavouritesFragment favouritesFragment) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.favouritesFragment = favouritesFragment;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.favourite_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.rootView.setBackgroundColor(Color.rgb(mData.get(position)._red , mData.get(position)._green,  mData.get(position)._blue));
        holder.position = position;

    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout rootView;
        public int position;

        ViewHolder(View itemView) {
            super(itemView);
            rootView = (LinearLayout)itemView.findViewById(R.id.favourite_item_root_layout);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(mOnCreateContextMenuListener);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

        private final View.OnCreateContextMenuListener mOnCreateContextMenuListener = new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                if (mData!= null) {
                    MenuItem myActionItem = menu.add("Delete");
                    myActionItem.setOnMenuItemClickListener(mOnMyActionClickListener);
                }
            }
        };

        private final MenuItem.OnMenuItemClickListener mOnMyActionClickListener = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                FavouritesFragment.favourites.remove(position);
                favouritesFragment.adapter.notifyDataSetChanged();
                return true;
            }
        };
    }

    // convenience method for getting data at click position
    public RGBColor getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
