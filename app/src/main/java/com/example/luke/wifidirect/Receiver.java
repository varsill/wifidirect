package com.example.luke.wifidirect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;

public class Receiver extends BroadcastReceiver {
    private WifiP2pManager mWifiP2pManager;
    private WifiP2pManager.Channel mChannel;
    boolean isReady;
    Receiver(WifiP2pManager m, WifiP2pManager.Channel c)
    {
        this.mWifiP2pManager=m;
        this.mChannel=c;
        isReady = false;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            int state  = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if(state==WifiP2pManager.WIFI_P2P_STATE_ENABLED)
            {
                isReady=true;
            }
            else if(state==WifiP2pManager.WIFI_P2P_STATE_DISABLED)
            {
                //disabled
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {

        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {

        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {

        }

    }
}
