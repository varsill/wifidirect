package com.example.luke.wifidirect;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.net.Uri;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements ActivityContextInterface
{
    WifiP2pManager mWifiP2pManager;
    WifiP2pManager.Channel mChannel;
    Receiver mReceiver;
    IntentFilter filter;
    final int PORT = 8888;
    @Override
     public boolean setText(String text, int id)
    {
       TextView textView = findViewById(id);
       if(textView==null) return false;
       textView.setText(text);
       return true;
    }
    @Override
    public boolean addItemToPeerListView(Peer item, int id)
    {
        ListView listView = findViewById(id);
        if(listView==null) return false;
        ArrayList<Peer> list = new ArrayList<Peer>();
        for(int i=0; i<listView.getAdapter().getCount(); i++)
        {
            list.add((Peer)listView.getItemAtPosition(i));
        }
        list.add(item);
        PeerAdapter adapter = new PeerAdapter(this, list);
        listView.setAdapter(adapter);


        return true;
    }
    @Override
    public boolean addItemToPeerListView(ArrayList<Peer> listtobeadded, int id)
    {
        ListView listView = findViewById(id);
        if(listView==null) return false;
        ArrayList<Peer> list = new ArrayList<Peer>();
        ListAdapter oldadapter = listView.getAdapter();
        if(oldadapter!=null) {
            for (int i = 0; i <oldadapter.getCount(); i++) {
                list.add((Peer) listView.getItemAtPosition(i));
            }
        }
        for(int i=0; i<listtobeadded.size(); i++)
        {
            list.add(listtobeadded.get(i));
        }

        PeerAdapter adapter = new PeerAdapter(this, list);
        listView.setAdapter(adapter);
        return true;
    }
    @Override
    public boolean reloadPeerListView (ArrayList<Peer> listtobeadded, int id)
    {
        ListView listView = findViewById(id);
        if(listView==null) return false;
        PeerAdapter adapter = new PeerAdapter(this, listtobeadded);
        listView.setAdapter(adapter);
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWifiP2pManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mWifiP2pManager.initialize(this, getMainLooper(), null);
        mReceiver = new Receiver(mWifiP2pManager, mChannel);

        filter = new IntentFilter();
        filter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        filter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        filter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        filter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        final Button button = findViewById(R.id.button2);
        final ListView listView = findViewById(R.id.listview);

        button.setText("Znajdź urządzenia w pobliżu");
        button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                mWifiP2pManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                      //  listView.setAdapter(new PeerAdapter(getApplicationContext(),new ArrayList<Peer>()));
                    }

                    @Override
                    public void onFailure(int i) {
                        Toast.makeText(getApplicationContext(), "Dupa, nie dziala", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

      //  listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        listView.setFocusable(false);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                button.setText("Trwa  łączenie");
                try{
                  final Peer dev = (Peer) listView.getItemAtPosition(i);

                    WifiP2pDevice device =   mReceiver.devicelist.get(dev.address);
                    WifiP2pConfig config = new WifiP2pConfig();
                    config.deviceAddress=dev.address;
                    mWifiP2pManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
                        @Override
                        public void onSuccess() {

                            Log.d("Dziala", "Polaczono z adresem "+dev.address);
                        Socket socket =  new Socket();
                        byte buf[]=new byte[1024];
                        try
                        {

                            socket.bind(null);//bo to client
                            socket.connect( new InetSocketAddress(dev.address, PORT));

                            OutputStream outputStream = socket.getOutputStream();
                            InputStream inputStream = socket.getInputStream();
                            boolean istoread = false;
                            while(istoread=inputStream.read(buf)!= -1)
                            {
                                outputStream.write(buf, 0, buf.length);
                            }

                        }catch (Exception e)
                        {
                            Log.e("Błąd", "Problem z przesyłaniem plików");
                        }
                        }

                        @Override
                        public void onFailure(int i) {
                            Log.e("Błąd", "Nie można połączyć");
                        }
                    });
                } catch(Exception e)
                {
                    Log.e("Blad", "Nie mozna pobrac nazwy");
                }

            }
        });
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        registerReceiver(mReceiver, filter);
    }
    @Override
    protected void onPause()
    {
        super.onPause();
        unregisterReceiver(mReceiver);
    }


}
