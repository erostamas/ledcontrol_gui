package com.erostamas.ledcontrol;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SonoffSwitchUtils {

    public static String toggleSwitch(String ipAddress, String deviceId) {
        try {
            String switchTargetState = "";
            String switchCurrentState = getSwitchState(ipAddress, deviceId);
            if (switchCurrentState.equals("off")) {
                switchTargetState = "on";
            } else if (switchCurrentState.equals("on")) {
                switchTargetState = "off";
            } else {
                Log.e("sonoff", "Unknown switch state received: " + switchCurrentState);
            }
            setSwitch(switchTargetState, ipAddress, deviceId);

        } catch (Exception e) {
            Log.v("ErrorAPP", e.toString());
        }
        return "";
    }

    public static String setSwitch(String switchTargetState, String ipAddress, String deviceId) {
        try {
            String infoUrlStr = "http://" + ipAddress + ":8081/zeroconf/switch";

            JSONObject data = new JSONObject();
            data.put("switch", switchTargetState);

            JSONObject payload = new JSONObject();
            payload.put("deviceid", deviceId);
            payload.put("data", data);

            return postRequest(infoUrlStr, payload.toString());
        }
        catch (Exception e){
            Log.v("ErrorAPP",e.toString());
        }
        return "";
    }

    public static String postRequest(String urlStr, String payload) {
        Log.i("sonoff", "postRequest");
        try {
            URL url = new URL(urlStr);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/json");

            DataOutputStream localDataOutputStream = new DataOutputStream(connection.getOutputStream());
            localDataOutputStream.writeBytes(payload);
            localDataOutputStream.flush();
            localDataOutputStream.close();

            StringBuilder response = new StringBuilder();
            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }
            Log.i("sonoff", "postRequest response: " + response.toString());
            return response.toString();
        }
        catch (Exception e){
            Log.i("sonoff", "postRequest error: " + e.toString());
            Log.v("ErrorAPP",e.toString());
        }
        return "";
    }

    public static String getSwitchState(String ipAddress, String deviceId){
        try {
            Log.i("sonoff", "getSwitchState");
            String infoUrlStr = "http://" + ipAddress + ":8081/zeroconf/info";

            JSONObject data = new JSONObject();

            JSONObject payload = new JSONObject();
            payload.put("deviceid", deviceId);
            payload.put("data", data);

            String info = postRequest(infoUrlStr, payload.toString());

            Log.i("sonoff", "getSwitchState request response: " + info);
            JSONObject mainObject = new JSONObject(info.toString());
            JSONObject dataObject = new JSONObject(mainObject.getString("data"));
            Log.i("sonoff", "switch is: " + dataObject.getString("switch"));
            return dataObject.getString("switch");
        }
        catch (Exception e){
            Log.i("sonoff", "getSwitchState ERROR: " + e.toString());
            Log.v("ErrorAPP",e.toString());
        }
        return "";
    }
}
