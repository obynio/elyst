package com.sybiload.shopp.Adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sybiload.shopp.Item;
import com.sybiload.shopp.Misc;
import com.sybiload.shopp.R;
import com.sybiload.shopp.Static;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ViewHolder>
{
    SharedPreferences itemPref;

    public ShopAdapter(Context ctx)
    {
        itemPref = ctx.getSharedPreferences("item", 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView txtHeader;
        public TextView txtFooter;
        public ImageView imageView;

        public ViewHolder(View v)
        {
            super(v);

            txtHeader = (TextView) v.findViewById(R.id.textViewShopFirstLine);
            txtFooter = (TextView) v.findViewById(R.id.textViewShopSecondLine);
            imageView = (ImageView) v.findViewById(R.id.imageViewShop);
        }
    }

    public void remove(Item myItem)
    {

        int position = Static.itemShop.indexOf(myItem);
        Static.itemAvailable.add(Static.itemShop.get(position));
        Static.itemShop.remove(position);
        new Misc().sortItems(Static.itemAvailable);

        Set<String> set;

        set = new HashSet<String>();
        for (Item it : Static.itemShop)
            set.add(it.getName());
        itemPref.edit().putStringSet("itemShop", set).commit();

        set = new HashSet<String>();
        for (Item it : Static.itemAvailable)
            set.add(it.getName());
        itemPref.edit().putStringSet("itemAvailable", set).commit();

        notifyItemRemoved(position);
    }


    @Override
    public ShopAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position)
    {
        final Item myItem = Static.itemShop.get(position);

        holder.txtHeader.setText(Static.itemShop.get(position).getName());
        holder.imageView.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                remove(myItem);
            }
        });

        holder.txtFooter.setText("Footer: " + Static.itemShop.get(position).getName());

    }

    @Override
    public int getItemCount()
    {
        return Static.itemShop.size();
    }

}
