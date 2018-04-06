package com.example.luke.wifidirect;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Receiver extends BroadcastReceiver {

    private ConnectionManager connectionManager;
    Receiver(ConnectionManager  connectionManager)
    {
        this.connectionManager = connectionManager;
    }
    @Override
    public void onReceive(final Context con, Intent intent) {
        String action = intent.getAction();
        final Context context = con;

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            int state  = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if(state==WifiP2pManager.WIFI_P2P_STATE_ENABLED)
            {
               connectionManager.setready(true);
               Log.d("Receiver", "Wifi P2P enabled");
            }
            else if(state==WifiP2pManager.WIFI_P2P_STATE_DISABLED)
            {
                connectionManager.setready(false);
                Log.d("Receiver", "Wifi P2P disabled");
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
                connectionManager.peersChanged();

        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            Toast.makeText(context, "Połączono", Toast.LENGTH_LONG);
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {

        }

    }
}
