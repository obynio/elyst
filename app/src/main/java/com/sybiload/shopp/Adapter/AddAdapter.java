package com.sybiload.shopp.Adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sybiload.shopp.R;
import com.sybiload.shopp.Static;

public class AddAdapter extends RecyclerView.Adapter<AddAdapter.ViewHolder>
{
    SharedPreferences itemPref;

    public AddAdapter(Context ctx)
    {
        itemPref = ctx.getSharedPreferences("item", 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView txtHeader;
        public ImageView imageView;

        public ViewHolder(View v)
        {
            super(v);

            txtHeader = (TextView) v.findViewById(R.id.textViewAddFirstLine);
            imageView = (ImageView) v.findViewById(R.id.imageViewAdd);
        }
    }

    public void remove(String name)
    {
        int position = Static.itemAvailable.indexOf(name);
        Static.itemShop.add(Static.itemAvailable.get(position));
        Static.itemAvailable.remove(position);
        Collections.sort(Static.itemShop);

        Set<String> set;

        set = new HashSet<String>();
        set.addAll(Static.itemAvailable);
        itemPref.edit().putStringSet("itemAvailable", set).commit();

        set = new HashSet<String>();
        set.addAll(Static.itemShop);
        itemPref.edit().putStringSet("itemShop", set).commit();

        notifyItemRemoved(position);
    }



    @Override
    public AddAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position)
    {
        final String name = Static.itemAvailable.get(position);
        holder.txtHeader.setText(Static.itemAvailable.get(position));
        holder.itemView.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                System.out.println("hemmp");
            }
        });
        holder.imageView.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                remove(name);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return Static.itemAvailable.size();
    }

}
