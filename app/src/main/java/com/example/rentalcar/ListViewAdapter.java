package com.example.rentalcar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewAdapter extends BaseAdapter {

    Context myContext;
    LayoutInflater inflater;
    private List<StationNames> listStation = null;
    private ArrayList<StationNames> arraylist;

    public ListViewAdapter(Context context, List<StationNames> listStation) {
        myContext = context;
        this.listStation = listStation;
        inflater = LayoutInflater.from(myContext);
        this.arraylist = new ArrayList<StationNames>();
        this.arraylist.addAll(listStation);
    }

    public class ViewHolder {
        TextView name;
    }

    @Override
    public int getCount() {
        return listStation.size();
    }

    @Override
    public StationNames getItem(int position) {
        return listStation.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.list_view_stations, null);
            // Locate the TextViews in listview_item.xml
            holder.name = (TextView) view.findViewById(R.id.name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText(listStation.get(position).getStationName());
        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        listStation.clear();
        if (charText.length() == 0) {
            listStation.addAll(arraylist);
        } else {
            for (StationNames wp : arraylist) {
                if (wp.getStationName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    listStation.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
