package com.example.luke.wifidirect;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

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

interface ActivityContextInterface
{
    public boolean setText(String text, int id);
    public boolean  addItemToPeerListView  (Peer item, int id);
    public boolean addItemToPeerListView   (ArrayList <Peer> listtobeadded, int id);
    public boolean reloadPeerListView (ArrayList <Peer> listtobeadded, int id);
    public boolean makeToast(String text, int length);
}
public class ConnectionManager {
    private Context context;
    ActivityContextInterface activityContextInterface;
    private WifiP2pManager mWifiP2pManager;
    private WifiP2pManager.Channel mChannel;
    private Receiver mReceiver;
    private IntentFilter filter;
    private boolean success;
    private boolean isready=false;
    public ConnectionManager(Activity activity) {
        this.context = (Context)activity;
        activityContextInterface=(ActivityContextInterface) activity;
        mWifiP2pManager = (WifiP2pManager) context.getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mWifiP2pManager.initialize(context, context.getMainLooper(), null);
        mReceiver = new Receiver(this);
        filter = new IntentFilter();
        filter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        filter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        filter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        filter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        context.registerReceiver(mReceiver, filter);

    }
    public void setsuccess(boolean a)
    {
        success=a;
    }
    public void setready(boolean r)
    {
        isready=r;
    }
    private boolean getready()
    {
        return isready;
    }

    private boolean getsuccess()
    {
        return success;
    }
    public void paused()
    {
        context.registerReceiver(mReceiver, filter);
    }
    public void reasumed()
    {
        context.unregisterReceiver(mReceiver);
    }
    public boolean discoverPeers()
    {

        mWifiP2pManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
               Log.d("WifiP2pManager", "Discovering peers was successfull");
               setsuccess(true);

            }

            @Override
            public void onFailure(int i) {
                activityContextInterface.makeToast("Problem przy przeszukiwaniu", R.id.button2);
                Log.e("WifiP2pManager", "Problem with discovering peers");
                setsuccess(false);
            }
        });
        return getsuccess();
    }
    public boolean peersChanged()
    {
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
    }


}
