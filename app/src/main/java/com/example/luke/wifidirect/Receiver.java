package com.example.luke.wifidirect;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
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
    private WifiP2pManager mWifiP2pManager;
    private WifiP2pManager.Channel mChannel;
    boolean isReady;
    public WifiP2pDeviceList devicelist;
    public  ArrayList<Peer> peerNameList;
    private Collection<WifiP2pDevice> oldcollection;
    Receiver(WifiP2pManager m, WifiP2pManager.Channel c)
    {
        this.mWifiP2pManager=m;
        this.mChannel=c;
        isReady = false;


    }
    @Override
    public void onReceive(final Context con, Intent intent) {
        String action = intent.getAction();
        final Context context = con;
        peerNameList=new ArrayList<Peer>();
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

                   final Button button =  (Button)((Activity)context).findViewById(R.id.button2);

                    mWifiP2pManager.requestPeers(mChannel, new WifiP2pManager.PeerListListener() {
                        @Override
                        public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
                            if(wifiP2pDeviceList.getDeviceList().size()==0)
                            {
                                button.setText("Brak dostepnych urządzeń. Skanuj jeszcze raz:");
                            }
                            if(wifiP2pDeviceList.getDeviceList()==oldcollection)
                            {
                                return ;
                            }
                           oldcollection= wifiP2pDeviceList.getDeviceList();
                          for(WifiP2pDevice device: wifiP2pDeviceList.getDeviceList())
                          {
                              peerNameList.add(new Peer(device.deviceName, device.deviceAddress));
                          }
                            devicelist=wifiP2pDeviceList;
                            ListView listView = (ListView)((Activity)context).findViewById(R.id.listview);
                            PeerAdapter adapter = new PeerAdapter(context, peerNameList);
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            Toast.makeText(context, "Połączono", Toast.LENGTH_LONG);
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {

        }

    }
}
