package com.example.luke.wifidirect;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Collection;
import java.util.List;

public class Receiver extends BroadcastReceiver {
    private WifiP2pManager mWifiP2pManager;
    private WifiP2pManager.Channel mChannel;
    boolean isReady;
    public WifiP2pDeviceList devicelist;
    public  List<String> peerNameList;

    Receiver(WifiP2pManager m, WifiP2pManager.Channel c)
    {
        this.mWifiP2pManager=m;
        this.mChannel=c;
        isReady = false;


    }
    @Override
    public void onReceive(Context con, Intent intent) {
        String action = intent.getAction();
        final Context context = con;
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
                if(mWifiP2pManager!=null)
                {
                    mWifiP2pManager.requestPeers(mChannel, new WifiP2pManager.PeerListListener() {
                        @Override
                        public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
                          for(WifiP2pDevice device: wifiP2pDeviceList.getDeviceList())
                          {
                              peerNameList.add(device.deviceName);
                          }
                            ListView listView = (ListView)((Activity)context).findViewById(R.id.listview);
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.activity_main, peerNameList);
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {

        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {

        }

    }
}
