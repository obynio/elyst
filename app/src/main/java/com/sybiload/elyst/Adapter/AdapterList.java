package com.sybiload.elyst.Adapter;

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

import com.sybiload.elyst.ActivityShop;
import com.sybiload.elyst.Database.Item.DatabaseItem;
import com.sybiload.elyst.Database.List.DatabaseList;
import com.sybiload.elyst.FragmentList;
import com.sybiload.elyst.Item;
import com.sybiload.elyst.List;
import com.sybiload.elyst.Misc;
import com.sybiload.elyst.R;
import com.sybiload.elyst.Static;

import java.util.ArrayList;

public class AdapterList extends RecyclerView.Adapter<AdapterList.ViewHolder>
{
    private FragmentList fm;
    private DatabaseList database;

    public static List selectedList;
    public static ViewHolder selectedHolder;

    public AdapterList(FragmentList fm)
    {
        this.fm = fm;
        database = new DatabaseList(fm.getActivity());
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

    public void clearSelected()
    {
        if (selectedHolder != null && selectedList != null)
        {
            selectedHolder.itemView.setBackgroundColor(Color.TRANSPARENT);
            selectedHolder = null;
            selectedList = null;
        }

    }

    public void delete(List myList)
    {
        if (Static.allList.contains(myList))
        {
            // update database
            database.open();
            database.deleteItem(myList);
            database.close();

            // delete database
            fm.getActivity().deleteDatabase(myList.getDatabase());

            // remove item from list
            int position = Static.allList.indexOf(myList);
            Static.allList.remove(position);

            notifyItemRemoved(position);
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
        final List myList = Static.allList.get(position);

        holder.txtHeader.setText(Static.allList.get(position).getName());

        // hide footer if no description
        if (myList.getDescription() == null || myList.getDescription().equals(""))
        {
            holder.txtFooter.setVisibility(View.GONE);
        }
        else
        {
            holder.txtFooter.setText(myList.getDescription());
            holder.txtFooter.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                if (selectedHolder != null)
                    clearSelected();

                holder.itemView.setBackgroundColor(Color.parseColor("#C3C3C3"));

                selectedHolder = holder;
                selectedList = myList;

                fm.pressSelect();

                return true;
            }
        });

        holder.itemView.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (selectedHolder == null)
                    fm.enterList(position);
                else
                {
                    clearSelected();
                    fm.pressSelect();
                }

            }
        });
    }

    @Override
    public int getItemCount()
    {
        return Static.allList.size();
    }

}
