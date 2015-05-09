package com.sybiload.elyst.Adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.sybiload.elyst.Database.List.DatabaseList;
import com.sybiload.elyst.R;

import java.util.ArrayList;

public class AdapterListView extends ArrayAdapter<ListViewItem>
{
    private final Context context;
    private final ArrayList<ListViewItem> values;

    public AdapterListView(Context context, ArrayList<ListViewItem> values)
    {
        super(context, R.layout.item_nav, values);

        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = null;

        if (position == 0)
        {

            rowView = inflater.inflate(R.layout.item_header, parent, false);

        }
        else
        {
            rowView = inflater.inflate(R.layout.item_nav, parent, false);

            TextView titleView = (TextView) rowView.findViewById(R.id.textViewNavTitle);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.iconNav);

            titleView.setText(values.get(position).getTitle());
            imageView.setImageDrawable(values.get(position).getIcon());
        }




        return rowView;
    }
}