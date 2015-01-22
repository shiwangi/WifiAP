package com.example.rohanraja.wifiaptest1;

import java.util.Objects;

/**
 * Created by rohanraja on 22/01/15.
 */
public interface CommDevice {

    // Can be WiFi or Bluetooth or NFC or 3G

    public Object CreateIntranet(Object param) ;

    public Object getClientList(Object param);

    public Object sendMessage(String message, Object client);

    public Object listenMessage(Object pSocket);

}
