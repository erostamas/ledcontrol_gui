package com.erostamas.ledcontrol;

import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.erostamas.ledcontrol.ui.main.FavouritesFragment;

import java.util.ArrayList;

public class FavouriteItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public LinearLayout rootView;
    public int position;
    private FavouritesRecyclerViewAdapter.ItemClickListener _favouriteItemClickListener;
    private FavouritesRecyclerViewAdapter _adapter;
    private ArrayList<RGBColor> _favouritesContainer;

    FavouriteItemViewHolder(View itemView, ArrayList<RGBColor> favouritesContainer, FavouritesRecyclerViewAdapter.ItemClickListener favouriteItemClickListener, FavouritesRecyclerViewAdapter adapter) {
        super(itemView);
        _favouritesContainer = favouritesContainer;
        _favouriteItemClickListener = favouriteItemClickListener;
        _adapter = adapter;
        rootView = (LinearLayout)itemView.findViewById(R.id.favourite_item_root_layout);
        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(mOnCreateContextMenuListener);
    }

    @Override
    public void onClick(View view) {
        if (_favouriteItemClickListener != null) _favouriteItemClickListener.onItemClick(view, getAdapterPosition());
    }

    private final View.OnCreateContextMenuListener mOnCreateContextMenuListener = new View.OnCreateContextMenuListener() {
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            if (_favouritesContainer!= null) {
                MenuItem myActionItem = menu.add("Delete");
                myActionItem.setOnMenuItemClickListener(mOnMyActionClickListener);
            }
        }
    };

    private final MenuItem.OnMenuItemClickListener mOnMyActionClickListener = new MenuItem.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            FavouritesFragment.favourites.remove(position);
            _adapter.notifyDataSetChanged();
            return true;
        }
    };
}
