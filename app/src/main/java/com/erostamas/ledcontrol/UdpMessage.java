package com.erostamas.ledcontrol;

public class UdpMessage {

    private int _port;
    private String _address;
    private String _message;

    public UdpMessage(String address, int port, String message) {
        _address = address;
        _port = port;
        _message = message;
    }

    String getAddress() { return _address; }
    int getPort() {return _port;}
    String getMessage() {return _message;}

}

