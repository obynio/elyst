package com.sybiload.shopp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sybiload.shopp.ActivityShop;
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

    Context ctx;

    public AdapterShop(Context ctx, List list)
    {
        this.ctx = ctx;
        databaseItem = new DatabaseItem(ctx, list.getDatabase());

        ArrayList<Item> tmpList = list.getItem();
        item = new ArrayList<Item>();



        for (Item it : tmpList)
        {
            if (it.isToShop())
                item.add(it);
        }

        new Misc().sortItemByDone(item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView txtHeader;
        public TextView txtFooter;
        public ImageView imageViewItemIcon;

        public ViewHolder(View v)
        {
            super(v);

            txtHeader = (TextView) v.findViewById(R.id.textViewShopFirstLine);
            txtFooter = (TextView) v.findViewById(R.id.textViewShopSecondLine);
            imageViewItemIcon = (ImageView) v.findViewById(R.id.imageViewShopItemIcon);
        }
    }

    public void remove(Item myItem)
    {
        myItem.toShop(false);

        int position = item.indexOf(myItem);
        item.remove(position);

        databaseItem.open();
        databaseItem.updateByName(myItem.getName(), myItem);
        databaseItem.close();

        notifyItemRemoved(position);
    }

    public void done(Item myItem)
    {
        if (!myItem.isDone())
            myItem.done(true);
        else
            myItem.done(false);


        int position = item.indexOf(myItem);

        new Misc().sortItem(item);
        new Misc().sortItemByDone(item);



        notifyItemMoved(position, item.indexOf(myItem));

        databaseItem.open();
        databaseItem.updateByName(myItem.getName(), myItem);
        databaseItem.close();
    }

    public void update(Item oldItem, Item newItem)
    {
        // update database

        int position = item.indexOf(oldItem);
        item.set(position, newItem);

        notifyItemChanged(position);

        databaseItem.open();
        databaseItem.updateByName(oldItem.getName(), newItem);
        databaseItem.close();
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
    public void onBindViewHolder(final ViewHolder holder, final int position)
    {
        if (item.get(position).isToShop())
        {
            new Misc().log("bindView");

            final Item myItem = item.get(position);

            holder.txtHeader.setText(myItem.getName());

            Item it = myItem;

            if (it.getDescription() == null || it.getDescription().equals(""))
            {
                holder.txtFooter.setVisibility(View.GONE);
                holder.txtHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            }
            else
            {
                holder.txtFooter.setText(myItem.getDescription());
                holder.txtHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                holder.txtFooter.setVisibility(View.VISIBLE);
            }


            holder.itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {

                    // prevent user from deleting item and editing it at the same time
                    if (!ActivityShop.toolbarOpened)
                        remove(myItem);
                    else
                    {
                        if(ctx instanceof ActivityShop)
                            ((ActivityShop)ctx).barAction();
                    }

                    return true;
                }
            });

            if (myItem.isDone())
            {
                holder.txtHeader.setPaintFlags(holder.txtHeader.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.txtFooter.setPaintFlags(holder.txtFooter.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.imageViewItemIcon.setColorFilter(Color.parseColor("#78909C"));
            }
            else
            {
                holder.txtHeader.setPaintFlags(holder.txtHeader.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                holder.txtFooter.setPaintFlags(holder.txtFooter.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                holder.imageViewItemIcon.setColorFilter(Color.parseColor("#2196F3"));
            }

            holder.itemView.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    done(myItem);

                    if (myItem.isDone())
                    {
                        holder.txtHeader.setPaintFlags(holder.txtHeader.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        holder.txtFooter.setPaintFlags(holder.txtFooter.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        holder.imageViewItemIcon.setColorFilter(Color.parseColor("#78909C"));
                    }
                    else
                    {
                        holder.txtHeader.setPaintFlags(holder.txtHeader.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                        holder.txtFooter.setPaintFlags(holder.txtFooter.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                        holder.imageViewItemIcon.setColorFilter(Color.parseColor("#2196F3"));
                    }
                }
            });

            holder.imageViewItemIcon.setImageDrawable(ctx.getResources().getDrawable(myItem.getIcon()));

            holder.imageViewItemIcon.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    if(ctx instanceof ActivityShop){
                        // absolute bullshit
                        ActivityShop.currentItem = myItem;
                        ActivityShop.currentAdapter = AdapterShop.this;
                        ((ActivityShop)ctx).barAction();
                    }

                    return true;
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
