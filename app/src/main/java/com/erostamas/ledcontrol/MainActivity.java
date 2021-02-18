package com.erostamas.ledcontrol;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.erostamas.ledcontrol.ui.main.FavouritesFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.erostamas.ledcontrol.ui.main.SectionsPagerAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements RedisResponseUser{

    private Map<String, String> _redisContent = new HashMap<String, String>() {{}};
    private View _view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("lifecycle", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);

        loadFavourites();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _view = view;
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                String controllerIpAddress = prefs.getString("controller_ip_address", "192.168.1.247");
                new RedisClient(controllerIpAddress, "ledcontrol_process_data", _redisContent, MainActivity.this).execute();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent myIntent = new Intent(MainActivity.this, SettingsActivity.class);
                MainActivity.this.startActivity(myIntent);
                return true;
            default:
                // Do nothing
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRedisReplyReceived() {
        FavouritesFragment.favourites.add(new RGBColor(Integer.parseInt(_redisContent.get("red")), Integer.parseInt(_redisContent.get("green")), Integer.parseInt(_redisContent.get("blue"))));
        SectionsPagerAdapter.favouritesFragment.adapter.notifyDataSetChanged();
        Snackbar.make(_view, "Added color to favourites (" + _redisContent.get("red") + "," + _redisContent.get("green") + "," + _redisContent.get("blue"), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    protected void onPause() {
        Log.i("lifecycle", "onPause");
        super.onPause();
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("FAVOURITES", favouritesToJson()).apply();
    }

    private String favouritesToJson() {
        JSONArray jsonArray = new JSONArray();

        for (RGBColor color : FavouritesFragment.favourites) {
            try {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("red", color._red);
                jsonObj.put("green", color._green);
                jsonObj.put("blue", color._blue);
                jsonArray.put(jsonObj);
            } catch (Exception e) {
                Log.e("ledcontrol", "Failed to serialize color to json");
            }
        }
        return jsonArray.toString();
    }

    private void loadFavourites() {
        FavouritesFragment.favourites.clear();
        String favourites = PreferenceManager.getDefaultSharedPreferences(this).getString("FAVOURITES", "{}");
        try {

            JSONArray jr = new JSONArray(favourites);
            for(int i=0;i<jr.length();i++)
            {
                JSONObject color = jr.getJSONObject(i);
                FavouritesFragment.favourites.add(new RGBColor(Integer.parseInt(color.getString("red")), Integer.parseInt(color.getString("green")), Integer.parseInt(color.getString("blue"))));
            }
        } catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}