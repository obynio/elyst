package com.sybiload.elyst.Adapter;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.provider.CalendarContract;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
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
    private SharedPreferences mainPref;

    public static List selectedList;
    public static ViewHolder selectedHolder;

    public AdapterList(FragmentList fm)
    {
        this.fm = fm;
        database = new DatabaseList(fm.getActivity());
        mainPref = fm.getActivity().getSharedPreferences("main", 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView txtHeader;
        public TextView txtFooter;
        public ImageView imageView;

        public ViewHolder(View v)
        {
            super(v);

            imageView = (ImageView) v.findViewById(R.id.imageViewList);
            txtHeader = (TextView) v.findViewById(R.id.textViewListFirstLine);
            txtFooter = (TextView) v.findViewById(R.id.textViewListSecondLine);
        }
    }

    public void clearSelected()
    {
        if (selectedHolder != null && selectedList != null)
        {
            selectedHolder.imageView.setColorFilter(Color.TRANSPARENT);
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
            database.dbList.close();

            // delete database
            fm.getActivity().deleteDatabase(myList.getIdDb());

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

        holder.setIsRecyclable(false);
        new updateBmpAsync().execute(Static.cardDrw[Static.allList.get(position).getBackground()], holder);
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

                holder.imageView.setColorFilter(Color.parseColor("#66FFFFFF"));

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

    public class updateBmpAsync extends AsyncTask<Object, Void, Void>
    {
        Bitmap bitmap;
        ViewHolder myHolder;

        @Override
        protected Void doInBackground(Object... params)
        {
            myHolder = (ViewHolder) params[1];

            BitmapFactory.Options options=new BitmapFactory.Options();
            options.inSampleSize = Integer.parseInt(mainPref.getString("listPreferenceUiBgRes", "2")); //the higher this number goes, the smaller the image gets
            bitmap = BitmapFactory.decodeResource(fm.getResources(), (Integer) params[0], options);

            return null;
        }

        @Override
        protected void onPostExecute(Void unused)
        {
            Animation fadeInAnimation = AnimationUtils.loadAnimation(fm.getActivity(), R.anim.fade_in);
            myHolder.imageView.startAnimation(fadeInAnimation);
            myHolder.imageView.setImageBitmap(bitmap);
        }
    }

}
