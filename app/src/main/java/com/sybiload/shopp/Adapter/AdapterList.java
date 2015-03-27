package com.sybiload.shopp.Adapter;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.CalendarContract;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sybiload.shopp.ActivityShop;
import com.sybiload.shopp.FragmentList;
import com.sybiload.shopp.Item;
import com.sybiload.shopp.Misc;
import com.sybiload.shopp.R;
import com.sybiload.shopp.Static;

import java.util.ArrayList;

public class AdapterList extends RecyclerView.Adapter<AdapterList.ViewHolder>
{
    FragmentList fm;

    public AdapterList(FragmentList fm)
    {
        this.fm = fm;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView txtHeader;
        public TextView txtFooter;

        public ViewHolder(View v)
        {
            super(v);

            txtHeader = (TextView) v.findViewById(R.id.textViewListFirstLine);
            txtFooter = (TextView) v.findViewById(R.id.textViewListSecondLine);
        }
    }

    @Override
    public AdapterList.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position)
    {
        holder.txtHeader.setText(Static.allList.get(position).getName());
        holder.txtFooter.setText(Static.allList.get(position).getDescription());

        holder.itemView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE)
                    v.setBackgroundColor(Color.parseColor("#C3C3C3"));
                else
                    v.setBackgroundColor(Color.TRANSPARENT);

                return false;
            }
        });
        holder.itemView.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                ((FragmentList)fm).enterList(position);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return Static.allList.size();
    }

}
