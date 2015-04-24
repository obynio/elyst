package com.sybiload.elyst.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sybiload.elyst.ActivityAdd;
import com.sybiload.elyst.ActivityShop;
import com.sybiload.elyst.Database.Item.DatabaseItem;
import com.sybiload.elyst.Item;
import com.sybiload.elyst.List;
import com.sybiload.elyst.Misc;
import com.sybiload.elyst.R;
import com.sybiload.elyst.Static;

import java.util.ArrayList;

public class AdapterAdd extends RecyclerView.Adapter<AdapterAdd.ViewHolder>
{
    private Context ctx;

    private SharedPreferences mainPref;

    public static ArrayList<ViewHolder> selectedHolder = new ArrayList<>();
    public static ArrayList<Integer> selectedIndex = new ArrayList<>();
    public static ArrayList<Item> selectedItem = new ArrayList<>();

    public AdapterAdd(Context ctx)
    {
        this.ctx = ctx;
        mainPref = ctx.getSharedPreferences("main", 0);
        Static.currentList.sortAvailable(ctx);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView txtHeader;
        public ImageView imageViewItemIcon;

        public ViewHolder(View v)
        {
            super(v);

            txtHeader = (TextView) v.findViewById(R.id.textViewAddFirstLine);
            imageViewItemIcon = (ImageView) v.findViewById(R.id.imageViewAddItemIcon);
        }
    }

    public void delete()
    {
        for (Item myItem : selectedItem)
        {
            // update database
            new Misc().removeItem(ctx, myItem);

            // remove item from itemShop
            int position = Static.currentList.itemAvailable.indexOf(myItem);
            Static.currentList.itemAvailable.remove(position);

            notifyItemRemoved(position);
        }
    }

    public void clearSelected()
    {
        for (ViewHolder hold : selectedHolder)
        {
            hold.itemView.setBackgroundColor(Color.TRANSPARENT);
        }

        selectedIndex.clear();
        selectedHolder.clear();
        selectedItem.clear();
        ((ActivityAdd)ctx).pressSelect();
    }

    public void remove(Item myItem)
    {
        if (Static.currentList.itemAvailable.contains(myItem))
        {
            myItem.setDone(false);

            // update database
            new Misc().insertItem(ctx, myItem);

            // get position of our item in the availableItem and remove it
            int position = Static.currentList.itemAvailable.indexOf(myItem);
            Static.currentList.itemAvailable.remove(position);

            // copy the item to shopItem
            Static.currentList.itemShop.add(myItem);

            notifyItemRemoved(position);
        }
    }

    public void update(Item oldItem, Item newItem)
    {
        // update database
        new Misc().updateItem(ctx, newItem);

        // get old pos
        int oldpos = 0;
        for (int i = 0; i < Static.currentList.itemAvailable.size(); i++)
        {
            if (Static.currentList.itemAvailable.get(i).getIdItem().equals(oldItem.getIdItem()))
            {
                oldpos = i;
                break;
            }
        }

        // prevent the 'not update on set list' bug
        Static.currentList.itemAvailable.set(oldpos, newItem);

        // if there is a bug somewhere, be sure it's here !
        // sort data
        Static.currentList.sortAvailable(ctx);

        // get new pos
        int newpos = 0;
        for (int i = 0; i < Static.currentList.itemAvailable.size(); i++)
        {
            if (Static.currentList.itemAvailable.get(i).getIdItem().equals(oldItem.getIdItem()))
            {
                newpos = i;
                break;
            }
        }

        // change data
        notifyItemChanged(oldpos);
        // change position
        notifyItemMoved(oldpos, newpos);
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
    public void onBindViewHolder(final ViewHolder holder, final int position)
    {
        final Item myItem = Static.currentList.itemAvailable.get(holder.getPosition());

        if (!selectedIndex.contains(holder.getPosition()))
        {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }
        else
        {
            holder.itemView.setBackgroundColor(Color.parseColor("#C3C3C3"));
        }

        holder.txtHeader.setText(myItem.getName());

        // when there is a long click on a row, either remove item if the toolbar of the activity is not opened, or hide the toolbar if it is opened. More details below
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                if (!selectedIndex.contains(holder.getPosition()) && !ActivityAdd.toolbarOpened)
                {
                    ActivityAdd.currentItem = myItem;

                    holder.itemView.setBackgroundColor(Color.parseColor("#C3C3C3"));

                    selectedHolder.add(holder);
                    selectedIndex.add(holder.getPosition());
                    selectedItem.add(myItem);

                    ((ActivityAdd)ctx).pressSelect();
                }

                return true;
            }
        });

        holder.itemView.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!ActivityAdd.toolbarOpened)
                {
                    if (selectedIndex.contains(holder.getPosition()))
                    {
                        holder.itemView.setBackgroundColor(Color.TRANSPARENT);

                        selectedHolder.remove(holder);
                        selectedIndex.remove(selectedIndex.indexOf(holder.getPosition()));
                        selectedItem.remove(myItem);

                        ((ActivityAdd)ctx).pressSelect();
                    }
                    else if (!selectedIndex.contains(holder.getPosition()) && selectedIndex.size() != 0)
                    {
                        holder.itemView.setBackgroundColor(Color.parseColor("#C3C3C3"));

                        selectedHolder.add(holder);
                        selectedIndex.add(holder.getPosition());
                        selectedItem.add(myItem);

                        ((ActivityAdd)ctx).pressSelect();
                    }
                    else if (selectedIndex.size() == 0)
                    {
                        remove(myItem);

                        // make vibration
                        Vibrator vbr = (Vibrator) ctx.getSystemService(Context.VIBRATOR_SERVICE);
                        if (mainPref.getBoolean("checkBoxSystemVibration", true) && vbr.hasVibrator())
                        {
                            vbr.vibrate(14);
                        }
                    }
                }
            }
        });


        holder.imageViewItemIcon.setImageDrawable(ctx.getResources().getDrawable(R.mipmap.ic_icon));
        holder.imageViewItemIcon.setColorFilter(Color.parseColor(new Misc().getColor(ctx, myItem.getCategory())));
    }

    @Override
    public int getItemCount()
    {
        return Static.currentList.itemAvailable.size();
    }

}
