package com.sybiload.elyst.Adapter;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.CalendarContract;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
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

import com.sybiload.elyst.ActivityNewList;
import com.sybiload.elyst.ActivityShop;
import com.sybiload.elyst.Database.Item.DatabaseItem;
import com.sybiload.elyst.Database.List.DatabaseList;
import com.sybiload.elyst.FragmentList;
import com.sybiload.elyst.Item;
import com.sybiload.elyst.List;
import com.sybiload.elyst.Misc;
import com.sybiload.elyst.Pref.ActivityAppPref;
import com.sybiload.elyst.R;
import com.sybiload.elyst.Static;

import java.util.ArrayList;

public class AdapterList extends RecyclerView.Adapter<AdapterList.ViewHolder>
{
    private FragmentList fm;
    private SharedPreferences mainPref;

    public AdapterList(FragmentList fm)
    {
        this.fm = fm;
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

    public void update(List myList)
    {
        // update database
        Misc.updateList(fm.getActivity(), myList);

        // get oldpos in recyclerview
        int oldpos = 0;
        for (int i = 0; i < Static.allList.size(); i++)
        {
            if (Static.allList.get(i).getIdDb().equals(myList.getIdDb()))
            {
                oldpos = i;
                break;
            }
        }

        Misc.sortList(Static.allList);

        // change data in the fragmentlist recyclerview
        int newpos = 0;
        for (int i = 0; i < Static.allList.size(); i++)
        {
            if (Static.allList.get(i).getIdDb().equals(myList.getIdDb()))
            {
                newpos = i;
                break;
            }
        }

        // change data
        notifyItemChanged(oldpos);
        // reorganize list
        notifyItemMoved(oldpos, newpos);
    }

    public void delete(List myList)
    {
        // update database
        Misc.deleteList(fm.getActivity(), myList);

        // delete database
        fm.getActivity().deleteDatabase(myList.getIdDb());

        // remove item from list
        for (int i = 0; i < Static.allList.size(); i++)
        {
            if (Static.allList.get(i).getIdDb().equals(myList.getIdDb()))
            {
                notifyItemRemoved(i);
                Static.allList.remove(i);
                break;
            }
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
        List myList = Static.allList.get(holder.getLayoutPosition());

        new updateBmpAsync().execute(Static.cardDrw[Static.allList.get(holder.getLayoutPosition()).getBackground()], holder);
        holder.txtHeader.setText(Static.allList.get(holder.getLayoutPosition()).getName());

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
                AlertDialog.Builder alert = new AlertDialog.Builder(fm.getActivity(), R.style.AppCompatAlertDialogStyle);

                alert.setTitle(Static.allList.get(holder.getLayoutPosition()).getName());

                alert.setItems(new String[]{"Edit", "Delete"}, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        switch (which)
                        {
                            case 0:
                                Static.currentList = Static.allList.get(holder.getLayoutPosition());

                                Intent intent = new Intent(fm.getActivity(), ActivityNewList.class);
                                fm.startActivity(intent);
                                break;
                            case 1:
                                delete(Static.allList.get(holder.getLayoutPosition()));
                                break;
                            default:
                                break;
                        }
                    }
                });

                alert.show();

                return true;
            }
        });

        holder.itemView.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                fm.enterList(holder.getLayoutPosition());
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

            BitmapFactory.Options options = new BitmapFactory.Options();
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
