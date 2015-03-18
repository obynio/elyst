package com.sybiload.shopp.Adapter;

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

public class AdapterShop extends RecyclerView.Adapter<AdapterShop.ViewHolder>
{
    DatabaseItem databaseItem;
    ArrayList<Item> item;

    public AdapterShop(Context ctx, List list)
    {
        databaseItem = new DatabaseItem(ctx, list.getDatabase());
        this.item = list.getItem();
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
        myItem.toShop(false);

        /*
        int position = Static.itemShop.indexOf(myItem);
        Static.itemAvailable.add(Static.itemShop.get(position));
        Static.itemShop.remove(position);

        new Misc().sortItem(Static.itemAvailable);

        databaseItem.open(Static.list.get(0).getTable());
        databaseItem.updateByName(myItem.getName(), myItem);
        databaseItem.close();


        notifyItemRemoved(position);
        */
    }


    @Override
    public AdapterShop.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position)
    {
        final Item myItem = item.get(position);

        holder.txtHeader.setText(item.get(position).getName());
        holder.imageView.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                remove(myItem);
            }
        });

        holder.txtFooter.setText("Footer: " + item.get(position).getName());

    }

    @Override
    public int getItemCount()
    {
        return item.size();
    }

}
