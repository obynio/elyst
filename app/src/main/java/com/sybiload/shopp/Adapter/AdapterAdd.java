package com.sybiload.shopp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sybiload.shopp.Database.Item.DatabaseItem;
import com.sybiload.shopp.Item;
import com.sybiload.shopp.List;
import com.sybiload.shopp.Misc;
import com.sybiload.shopp.R;
import com.sybiload.shopp.Static;

import java.util.ArrayList;

public class AdapterAdd extends RecyclerView.Adapter<AdapterAdd.ViewHolder>
{
    DatabaseItem databaseItem;
    ArrayList<Item> item;


    public AdapterAdd(Context ctx, List list)
    {
        databaseItem = new DatabaseItem(ctx, list.getDatabase());

        ArrayList<Item> tmpList = list.getItem();
        item = new ArrayList<Item>();

        for (Item it : tmpList)
        {
            if (!it.isToShop())
                item.add(it);
        }
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

    public void remove(Item myItem)
    {
        myItem.toShop(true);

        int position = item.indexOf(myItem);
        item.remove(position);

        databaseItem.open();
        databaseItem.updateByName(myItem.getName(), myItem);
        databaseItem.close();

        notifyItemRemoved(position);

    }


    @Override
    public AdapterAdd.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position)
    {
        if (!item.get(position).isToShop())
        {
            final Item myItem = item.get(position);

            holder.txtHeader.setText(item.get(position).getName());
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
                    remove(myItem);
                }
            });
        }
    }

    @Override
    public int getItemCount()
    {
        return item.size();
    }

}