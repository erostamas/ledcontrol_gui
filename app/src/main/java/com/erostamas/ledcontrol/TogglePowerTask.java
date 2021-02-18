package com.erostamas.ledcontrol;

import android.os.AsyncTask;

public class TogglePowerTask extends AsyncTask<String,String,String> {
    @Override
    protected String doInBackground(String... params) {
        return SonoffSwitchUtils.toggleSwitch("192.168.1.38", "1000a0866a");
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
