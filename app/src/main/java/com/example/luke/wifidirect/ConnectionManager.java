package com.example.luke.wifidirect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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
}
public class ConnectionManager {
}
