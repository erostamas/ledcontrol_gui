package com.erostamas.ledcontrol;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;

import androidx.preference.PreferenceManager;

public class CommandSender {
    public static void setColor(Activity activity, int red, int green, int blue) {

        String _red = Integer.toString(red);
        String _green = Integer.toString(green);
        String _blue = Integer.toString(blue);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        String controllerIpAddress = prefs.getString("controller_ip_address", "192.168.1.247");

        UdpMessage udpMessage = new UdpMessage(controllerIpAddress, 50001, "setcolor " + _red + " " + _green + " " + _blue);
        UdpSender sender = new UdpSender();
        sender.execute(udpMessage);
    }

    public static void setIntensity(Activity activity, int intensity) {


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        String controllerIpAddress = prefs.getString("controller_ip_address", "192.168.1.247");

        UdpMessage udpMessage = new UdpMessage(controllerIpAddress, 50001, "setintensity " + Integer.toString(intensity));
        UdpSender sender = new UdpSender();
        sender.execute(udpMessage);
    }
}
