package com.erostamas.ledcontrol;

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

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements RedisResponseUser{

    private Map<String, String> _redisContent = new HashMap<String, String>() {{}};
    private View _view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);

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
}