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


}
