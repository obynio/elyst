package com.sybiload.shopp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sybiload.shopp.R;

import java.util.ArrayList;

public class AdapterListView extends ArrayAdapter<ListViewItem>
{
    private final Context context;
    private final ArrayList<ListViewItem> values;

    public AdapterListView(Context context, ArrayList<ListViewItem> values)
    {
        super(context, R.layout.header, values);

        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView;
        if (position == 0)
        {
            rowView = inflater.inflate(R.layout.header, parent, false);

            TextView mailView = (TextView) rowView.findViewById(R.id.email);
            TextView titleView = (TextView) rowView.findViewById(R.id.name);

            mailView.setText(values.get(position).getMail());
            titleView.setText(values.get(position).getTitle());
        }
        else
        {
            rowView = inflater.inflate(R.layout.item_nav, parent, false);

            TextView titleView = (TextView) rowView.findViewById(R.id.textViewNavTitle);
            titleView.setText(values.get(position).getTitle());
        }

        return rowView;
    }
}