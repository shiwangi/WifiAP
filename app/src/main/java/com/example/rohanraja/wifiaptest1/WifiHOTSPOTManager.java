package com.example.rohanraja.wifiaptest1;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.example.rohanraja.wifiaptest1.hotspotUtils.ClientScanResult;
import com.example.rohanraja.wifiaptest1.hotspotUtils.FinishScanListener;
import com.example.rohanraja.wifiaptest1.hotspotUtils.WifiApManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rohanraja on 21/01/15.
 */
public class WifiHOTSPOTManager {

    WifiApManager wifiApManager;
    Context mContext;
    static String SSID_HOTSPOT = "InternetON";
    WifiManager wifiManager;
    int WAIT_TIME = 10000;
    boolean hasClients;

    public WifiHOTSPOTManager(Context pContext) {
        mContext = pContext;
        wifiApManager = new WifiApManager(mContext);
        wifiManager = (WifiManager) mContext.getSystemService(mContext.WIFI_SERVICE);
        EstablishConnection();
    }

    public int EstablishConnection() {

        if (isWifiConnected())
            return 1;

        if (isHotspotActive()) {
            if (isHotSpotHasClients())
                return 1;
            else
                destroyHotspot();
        }
        switchONWifi();
        if (isHubAvailable()) {
            connectToHub();
            return 1;
        }

        createHotSpot();
        WaitForUsersToConnect("10 seconds");
        return EstablishConnection();

    }

    private void destroyHotspot() {
        wifiApManager.setWifiApEnabled(null, false);
    }

    public void switchONWifi() {
        if (wifiManager.isWifiEnabled() == false)
            wifiManager.setWifiEnabled(true); // true or false to activate/deactivate wifi
    }

    public boolean isHotspotActive() {
        return wifiApManager.isWifiApEnabled();
    }


    public boolean isWifiConnected() {
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        Log.d("wifiInfo", "\"" + wifiInfo.toString() + "\"");
        Log.d("SSID", wifiInfo.getSSID());
        System.out.println(wifiInfo.getSSID().equals(SSID_HOTSPOT));
        return (wifiInfo.getSSID().equals("\"" + SSID_HOTSPOT + "\""));

    }


    public boolean isHubAvailable() {
        wifiManager.startScan();
        List<android.net.wifi.ScanResult> list = wifiManager.getScanResults();
        for (ScanResult i : list) {
            Log.d("WifiHubs", i.SSID);
            if (i.SSID.equals(SSID_HOTSPOT))
                return true;
        }
        return false;
    }


    public boolean isHotSpotHasClients() {

        BufferedReader br = null;
        final ArrayList<ClientScanResult> result = new ArrayList<ClientScanResult>();

        try {
            br = new BufferedReader(new FileReader("/proc/net/arp"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] splitted = line.split(" +");

                if ((splitted != null) && (splitted.length >= 4)) {
                    // Basic sanity check
                    String mac = splitted[3];

                    if (mac.matches("..:..:..:..:..:..")) {
                        boolean isReachable = InetAddress.getByName(splitted[0]).isReachable(10000);

                        if (isReachable) {
                            result.add(new ClientScanResult(splitted[0], splitted[3], splitted[5], isReachable));
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e(this.getClass().toString(), e.toString());
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                Log.e(this.getClass().toString(), e.getMessage());
            }
        }
    return result.size()>0;
    }


    public boolean connectToHub() {
        WifiConfiguration wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = String.format("\"%s\"", SSID_HOTSPOT);
        wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

        int netId = wifiManager.addNetwork(wifiConfig);
        wifiManager.disconnect();
        wifiManager.enableNetwork(netId, true);
        wifiManager.reconnect();

        return false;
    }


    public boolean createHotSpot()

    {

        WifiConfiguration wf = new WifiConfiguration();
        wf.SSID = SSID_HOTSPOT;
        wifiApManager.setWifiApEnabled(wf, true);
        return false;

    }


    public boolean WaitForUsersToConnect(String par) {

        try {
            Thread.sleep(WAIT_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }


}