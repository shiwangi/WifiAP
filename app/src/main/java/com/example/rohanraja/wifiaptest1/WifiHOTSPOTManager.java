package com.example.rohanraja.wifiaptest1;

import android.content.Context;

import com.example.rohanraja.wifiaptest1.hotspotUtils.WifiApManager;

/**
 * Created by rohanraja on 21/01/15.
 */
public class WifiHOTSPOTManager {

    WifiApManager wifiApManager;
    Context mContext ;

    public void WifiHOTSPOTManager(Context pContext)
    {
        mContext = pContext ;
        wifiApManager = new WifiApManager(mContext);
    }

    public int EstablishConnection()
    {
        if(isWifiConnected())
            return 1;

        if(isHubAvailable())
        {
            connectToHub();
            return 1;
        }

        if(isHotspotActive())
        {
            if(isHotSpotHasClients())
                return 1;
        }

        createHotSpot();

        WaitForUsersToConnect("10 seconds");

        return EstablishConnection();

    }

    public boolean isHotspotActive()
    {

        return false;
    }
    

    public boolean isWifiConnected()
    {

        return false;
    }
    

    public boolean isHubAvailable()
    {

        return false;
    }
    

    public boolean isHotSpotHasClients()
    {

        return false;
    }
    

    public boolean connectToHub()
    {

        return false;
    }
    

    public boolean createHotSpot()
    {

        return false;
    }

    public boolean WaitForUsersToConnect(String par)
    {
        
        return false;
    }





}
