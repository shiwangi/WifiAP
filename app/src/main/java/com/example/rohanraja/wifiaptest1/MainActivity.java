package com.example.rohanraja.wifiaptest1;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.rohanraja.wifiaptest1.hotspotUtils.ClientScanResult;
import com.example.rohanraja.wifiaptest1.hotspotUtils.FinishScanListener;
import com.example.rohanraja.wifiaptest1.hotspotUtils.WifiApManager;


public class MainActivity extends Activity {
    static String SSID_HOTSPOT = "InternetON";
    TextView textView1;
    WifiApManager wifiApManager;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView1 = (TextView) findViewById(R.id.textView1);
        wifiApManager = new WifiApManager(this);
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        int flag = 0;
        wifiManager.startScan();
        List<android.net.wifi.ScanResult> listIP = wifiManager.getScanResults();
        for(android.net.wifi.ScanResult i : listIP){
            if(i.SSID==SSID_HOTSPOT){
                flag=1;

            }
        }
        if(flag==0){
            WifiConfiguration wf = new WifiConfiguration();
            wf.SSID = SSID_HOTSPOT;
            wifiApManager.setWifiApEnabled(wf, true);
        }
        else{
            WifiConfiguration conf = new WifiConfiguration();
            conf.SSID = "\""+SSID_HOTSPOT+"\"";
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            int netid = wifiManager.addNetwork(conf);
            wifiManager.disconnect();
            wifiManager.enableNetwork(netid,true);
            wifiManager.reconnect();
        }

    }

    private void scan() {
        wifiApManager.getClientList(false, new FinishScanListener() {

            @Override
            public void onFinishScan(final ArrayList<ClientScanResult> clients) {

                textView1.setText("WifiApState: " + wifiApManager.getWifiApState() + "\n\n");
                textView1.append("Clients: \n");
                for (ClientScanResult clientScanResult : clients) {
                    textView1.append("####################\n");
                    textView1.append("IpAddr: " + clientScanResult.getIpAddr() + "\n");
                    textView1.append("Device: " + clientScanResult.getDevice() + "\n");
                    textView1.append("HWAddr: " + clientScanResult.getHWAddr() + "\n");
                    textView1.append("isReachable: " + clientScanResult.isReachable() + "\n");
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, "Get Clients");
        menu.add(0, 1, 0, "Open AP");
        menu.add(0, 2, 0, "Close AP");
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                scan();
                break;
            case 1:

                WifiConfiguration wf = new WifiConfiguration();

                wf.SSID = SSID_HOTSPOT;

                wf.SSID = "InternetON";

                wifiApManager.setWifiApEnabled(wf, true);
                break;
            case 2:
                wifiApManager.setWifiApEnabled(null, false);
                break;
        }

        return super.onMenuItemSelected(featureId, item);
    }

}
