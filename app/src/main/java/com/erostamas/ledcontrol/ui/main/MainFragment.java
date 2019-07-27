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

import com.erostamas.ledcontrol.CommandSender;
import com.erostamas.ledcontrol.R;
import com.erostamas.ledcontrol.UdpMessage;
import com.erostamas.ledcontrol.UdpSender;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;

    public static MainFragment newInstance(int index) {
        MainFragment fragment = new MainFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        SeekBar intensityBar = (SeekBar) rootView.findViewById(R.id.seekBarIntensity);
        final ImageView colorPalette = (ImageView) rootView.findViewById(R.id.colorPalette);
        intensityBar.setMax(100);

        intensityBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.i("ledcontrol", "Intensity progress changed to : " + seekBar.getProgress());

                CommandSender.setIntensity(getActivity(), seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        View.OnTouchListener colorPaletteClickListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (v.equals(colorPalette)) {
                    // Write your awesome code here
                    float eventX = event.getX();
                    float eventY = event.getY();
                    float[] eventXY = new float[] {eventX, eventY};

                    Matrix invertMatrix = new Matrix();
                    ((ImageView)v).getImageMatrix().invert(invertMatrix);

                    invertMatrix.mapPoints(eventXY);
                    int x = Integer.valueOf((int)eventXY[0]);
                    int y = Integer.valueOf((int)eventXY[1]);


                    Drawable imgDrawable;
                    imgDrawable = ((ImageView)v).getDrawable();
                    Bitmap bitmap = ((BitmapDrawable)imgDrawable).getBitmap();

                    //Limit x, y range within bitmap
                    if(x < 0){
                        x = 0;
                    }else if(x > bitmap.getWidth()-1){
                        x = bitmap.getWidth()-1;
                    }

                    if(y < 0){
                        y = 0;
                    }else if(y > bitmap.getHeight()-1){
                        y = bitmap.getHeight()-1;
                    }

                    int touchedRGB = bitmap.getPixel(x, y);

                    CommandSender.setColor(getActivity(), Color.red(touchedRGB), Color.green(touchedRGB), Color.blue(touchedRGB));
                }
                return true;
            }
        };
        colorPalette.setOnTouchListener(colorPaletteClickListener);
        return rootView;
    }
}