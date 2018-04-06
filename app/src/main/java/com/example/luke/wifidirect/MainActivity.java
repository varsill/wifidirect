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

    final ConnectionManager connectionManager = new ConnectionManager(this);

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
    public boolean makeToast(String text, int length)
    {
        if(length ==1 ) {
            try{
                Toast.makeText(this, text, Toast.LENGTH_LONG).show();
            }catch(Exception e)
            {
                Log.e("Problem with Toast", e.getMessage());
                return false;
            }

            return true;
        }
        if (length==0)
        {
            try{
                Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
            }catch(Exception e)
            {
                Log.e("Problem with Toast", e.getMessage());
                return false;
            }
            return true;
        }
        return false; //wrong length
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        final Button button = findViewById(R.id.button2);
        final ListView listView = findViewById(R.id.listview);
        button.setText("Znajdź urządzenia w pobliżu");

        button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {

            }
        });

      //  listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        listView.setFocusable(false);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                button.setText("Trwa  łączenie");
                try{
                  final Peer peer = (Peer) listView.getItemAtPosition(i);
                  connectionManager.connectWithDevice(peer);


                } catch(Exception e)
                {
                    Log.e("ListView", e.getMessage());
                }

            }
        });
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        connectionManager.reasumed();

    }
    @Override
    protected void onPause()
    {
        super.onPause();
        connectionManager.paused();
    }


}
