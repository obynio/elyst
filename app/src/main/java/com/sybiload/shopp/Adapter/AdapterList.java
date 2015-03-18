package com.sybiload.shopp.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sybiload.shopp.ActivityShop;
import com.sybiload.shopp.Misc;
import com.sybiload.shopp.R;
import com.sybiload.shopp.Static;

public class AdapterList extends RecyclerView.Adapter<AdapterList.ViewHolder>
{
    Activity activity;

    public AdapterList(Activity act)
    {
        activity = act;
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
        holder.itemView.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(v.getContext(), ActivityShop.class);
                intent.putExtra("LIST_NUMBER", position);
                v.getContext().startActivity(intent);
                new Misc().leftTransition(activity);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return Static.allList.size();
    }

}
