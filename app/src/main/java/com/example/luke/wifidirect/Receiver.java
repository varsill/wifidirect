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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
class Peer {
    String name;
    String address;
    Peer(String n, String a)
    {
        name=n;
        address=a;
        }
}
class PeerAdapter extends BaseAdapter
{
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Peer> mDataSource;

    public PeerAdapter(Context context, ArrayList<Peer> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return mDataSource.size();
    }

    //2
    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    //3
    @Override
    public long getItemId(int position) {
        return position;
    }

    //4
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get view for row item
        View rowView = mInflater.inflate(R.layout.row, parent, false);
        TextView textView = rowView.findViewById(R.id.textview);
        textView.setText(mDataSource.get(position).name);
        return rowView;
    }
}
public class Receiver extends BroadcastReceiver {
    private WifiP2pManager mWifiP2pManager;
    private WifiP2pManager.Channel mChannel;
    boolean isReady;
    public WifiP2pDeviceList devicelist;
    public  ArrayList<Peer> peerNameList;

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

                    Button button =  (Button)((Activity)context).findViewById(R.id.button2);
                    button.setText("Dostępne urządzenia:");
                    mWifiP2pManager.requestPeers(mChannel, new WifiP2pManager.PeerListListener() {
                        @Override
                        public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
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

        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {

        }

    }
}
