package com.erostamas.ledcontrol;

import android.os.AsyncTask;
import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpSender extends AsyncTask<UdpMessage, Long, Long> {
    @Override
    protected Long doInBackground(UdpMessage... udpMessages) {
        try {
            int server_port = udpMessages[0].getPort();
            DatagramSocket s = new DatagramSocket();
            InetAddress local = InetAddress.getByName(udpMessages[0].getAddress());
            int msg_length = udpMessages[0].getMessage().length();
            byte[] message = udpMessages[0].getMessage().getBytes();
            DatagramPacket p = new DatagramPacket(message, msg_length, local, server_port);
            s.send(p);
        } catch (Exception e) {
            Log.e("com.erostamas.common.UdpSender", "Exception during UDP send: " + e.getMessage());
        }
        return null;
    }
}
