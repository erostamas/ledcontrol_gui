package com.erostamas.ledcontrol.ui.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.erostamas.ledcontrol.CommandSender;
import com.erostamas.ledcontrol.FavouritesRecyclerViewAdapter;
import com.erostamas.ledcontrol.R;
import com.erostamas.ledcontrol.RGBColor;
import com.erostamas.ledcontrol.UdpMessage;
import com.erostamas.ledcontrol.UdpSender;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class FavouritesFragment extends Fragment implements FavouritesRecyclerViewAdapter.ItemClickListener {

    private static final String ARG_SECTION_NUMBER = "section_number";
    public static ArrayList<RGBColor> favourites = new ArrayList<RGBColor>();

    private PageViewModel pageViewModel;
    public FavouritesRecyclerViewAdapter adapter;

    public static FavouritesFragment newInstance(int index) {
        FavouritesFragment fragment = new FavouritesFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favourites, container, false);

        // set up the RecyclerView
        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view);
        int numberOfColumns = 6;
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));
        adapter = new FavouritesRecyclerViewAdapter(getActivity(), favourites);
        adapter.setClickListener(this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);


        return rootView;
    }



    @Override
    public void onItemClick(View view, int position) {
        RGBColor item = adapter.getItem(position);
        CommandSender.setColor(getActivity(), item._red, item._green, item._blue);
        //Log.i("TAG", "You clicked number " + adapter.getItem(position) + ", which is at cell position " + position);
    }
}