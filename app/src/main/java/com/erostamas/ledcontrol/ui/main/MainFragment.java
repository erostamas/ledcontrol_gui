package com.erostamas.ledcontrol.ui.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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
        final Button onOffSwitch = (Button) rootView.findViewById(R.id.onOffSwitch);
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

                    Drawable imgDrawable;
                    ImageView imageView = (ImageView)v;
                    imgDrawable = imageView.getDrawable();
                    Bitmap bitmap = ((BitmapDrawable)imgDrawable).getBitmap();

                    if(eventX < 0){
                        eventX = 0;
                    }else if(eventX > v.getWidth()-1){
                        eventX = imageView.getWidth()-1;
                    }

                    if(eventY < 0){
                        eventY = 0;
                    }else if(eventY > imageView.getHeight()-1){
                        eventY = imageView.getHeight()-1;
                    }

                    int touchedRGB = bitmap.getPixel(bitmap.getWidth() * Math.round(eventX) / imageView.getWidth(), bitmap.getHeight() * Math.round(eventY) / imageView.getHeight());

                    CommandSender.setColor(getActivity(), Color.red(touchedRGB), Color.green(touchedRGB), Color.blue(touchedRGB));
                }
                return true;
            }
        };
        colorPalette.setOnTouchListener(colorPaletteClickListener);

        View.OnTouchListener onOffSwitchClickListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (v.equals(onOffSwitch) && event.getAction() == MotionEvent.ACTION_UP) {
                    Log.i("click", "hellobello");
                    AsyncTaskRunner postReq = new AsyncTaskRunner();
                    postReq.execute("start");
                }
                return true;
            }
        };
        onOffSwitch.setOnTouchListener(onOffSwitchClickListener);
        return rootView;
    }

    private class AsyncTaskRunner extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                Log.i("click", "asynctaskrunning");
                String infoUrlStr="http://192.168.1.38:8081/zeroconf/info";
                URL infoUrl=new URL(infoUrlStr);

                HttpURLConnection con = (HttpURLConnection) infoUrl.openConnection();
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                con.setRequestMethod("POST");
                con.setRequestProperty("Accept", "application/json");

                JSONObject data = new JSONObject();
                data.put("switch","off");

                JSONObject payload = new JSONObject();
                payload.put("deviceid","1000a0866a");
                payload.put("data", data);

                DataOutputStream localDataOutputStream = new DataOutputStream(con.getOutputStream());
                localDataOutputStream.writeBytes(payload.toString());
                localDataOutputStream.flush();
                localDataOutputStream.close();

                try(BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    Log.i("click", response.toString());
                    JSONObject mainObject = new JSONObject(response.toString());
                    Log.i("click", "data is: " + mainObject.getString("data"));
                    JSONObject dataObject = new JSONObject(mainObject.getString("data"));
                    Log.i("click", "switch is: " + dataObject.getString("switch"));
                }


            }
            catch (Exception e){
                Log.v("ErrorAPP",e.toString());
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }
}

