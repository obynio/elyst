package com.sybiload.elyst.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Vibrator;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sybiload.elyst.ActivityShop;
import com.sybiload.elyst.Database.Item.DatabaseItem;
import com.sybiload.elyst.Item;
import com.sybiload.elyst.List;
import com.sybiload.elyst.Misc;
import com.sybiload.elyst.R;
import com.sybiload.elyst.Static;

import java.util.ArrayList;

public class AdapterShop extends RecyclerView.Adapter<AdapterShop.ViewHolder>
{
    private Context ctx;

    public static ArrayList<ViewHolder> selectedHolder = new ArrayList<>();
    public static ArrayList<Integer> selectedIndex = new ArrayList<>();
    public static ArrayList<Item> selectedItem = new ArrayList<>();

    private SharedPreferences mainPref;

    public AdapterShop(Context ctx)
    {
        this.ctx = ctx;

        mainPref = ctx.getSharedPreferences("main", 0);

        Static.currentList.sortShop(ctx);
        Static.currentList.sortShopDone();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView txtHeader;
        public TextView txtFooter;
        public ImageView imageViewItemIcon;
        public LinearLayout llPrice;
        public LinearLayout llQuantity;
        public TextView textViewPrice;
        public TextView textViewQuantity;
        public ImageView imageViewPrice;
        public ImageView imageViewQuantity;

        public ViewHolder(View v)
        {
            super(v);

            txtHeader = (TextView) v.findViewById(R.id.textViewShopFirstLine);
            txtFooter = (TextView) v.findViewById(R.id.textViewShopSecondLine);
            imageViewItemIcon = (ImageView) v.findViewById(R.id.imageViewShopItemIcon);
            llPrice = (LinearLayout) v.findViewById(R.id.llShopPrice);
            llQuantity = (LinearLayout) v.findViewById(R.id.llShopQuantity);
            textViewPrice = (TextView) v.findViewById(R.id.textViewShopPrice);
            textViewQuantity = (TextView) v.findViewById(R.id.textViewShopQuantity);
            imageViewPrice = (ImageView) v.findViewById(R.id.imageViewShopPrice);
            imageViewQuantity = (ImageView) v.findViewById(R.id.imageViewShopQuantity);
        }
    }

    public void remove()
    {
        for (Item myItem : selectedItem)
        {
            // set new attributes
            myItem.setDescription(null);

            // update database
            Misc.removeItem(ctx, myItem);

            // remove item from itemShop
            int position = Static.currentList.itemShop.indexOf(myItem);
            Static.currentList.itemShop.remove(position);

            // add item to itemAvailable
            Static.currentList.itemAvailable.add(myItem);

            notifyItemRemoved(position);
        }
    }

    public void done(Item myItem)
    {
        // set new attributes
        if (!myItem.getDone())
            myItem.setDone(true);
        else
            myItem.setDone(false);

        // update database
        Misc.updateItem(ctx, myItem);

        // get old pos
        int oldpos = 0;
        for (int i = 0; i < Static.currentList.itemShop.size(); i++)
        {
            if (Static.currentList.itemShop.get(i).getIdItem().equals(myItem.getIdItem()))
            {
                oldpos = i;
                break;
            }
        }

        // sort itemShop
        Static.currentList.sortShop(ctx);
        Static.currentList.sortShopDone();

        // get new pos
        int newpos = 0;
        for (int i = 0; i < Static.currentList.itemShop.size(); i++)
        {
            if (Static.currentList.itemShop.get(i).getIdItem().equals(myItem.getIdItem()))
            {
                newpos = i;
                break;
            }
        }

        // move the item to the new position thanks to the previous position that we stored
        notifyItemMoved(oldpos, newpos);
    }


    public void update(Item newItem)
    {
        // update database
        Misc.updateItem(ctx, newItem);

        // get old pos
        int oldpos = 0;
        for (int i = 0; i < Static.currentList.itemShop.size(); i++)
        {
            if (Static.currentList.itemShop.get(i).getIdItem().equals(newItem.getIdItem()))
            {
                oldpos = i;
                break;
            }
        }

        // prevent the 'not update on set list' bug
        Static.currentList.itemShop.set(oldpos, newItem);

        // if there is a bug somewhere, be sure it's here !
        // reorganize list
        Static.currentList.sortShop(ctx);
        Static.currentList.sortShopDone();

        // get new pos
        int newpos = 0;
        for (int i = 0; i < Static.currentList.itemShop.size(); i++)
        {
            if (Static.currentList.itemShop.get(i).getIdItem().equals(newItem.getIdItem()))
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

    public void clearSelected()
    {
        for (ViewHolder hold : selectedHolder)
        {
            hold.itemView.setBackgroundColor(Color.TRANSPARENT);
        }

        selectedHolder.clear();
        selectedIndex.clear();
        selectedItem.clear();
        ((ActivityShop)ctx).pressSelect();
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
        final Item myItem = Static.currentList.itemShop.get(holder.getLayoutPosition());

        if (!selectedIndex.contains(holder.getLayoutPosition()))
        {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }
        else
        {
            holder.itemView.setBackgroundColor(Color.parseColor("#C3C3C3"));
        }

        holder.txtHeader.setText(myItem.getName());

        // when adapter is refreshing, check if the current row has a description or not and adapt the layout
        if (myItem.getDescription() == null || myItem.getDescription().equals(""))
        {
            holder.txtFooter.setVisibility(View.GONE);
        }
        else
        {
            holder.txtFooter.setText(myItem.getDescription());
            holder.txtFooter.setVisibility(View.VISIBLE);
        }

        if (myItem.getPrice() != 0.0)
        {
            CharSequence[] entries = ctx.getResources().getStringArray(R.array.ui_pref_currency_entries);
            entries[0] = "";

            holder.llPrice.setVisibility(View.VISIBLE);
            holder.textViewPrice.setText(Double.toString(myItem.getPrice() * (myItem.getQuantity() == 0.0 ? 1.0 : myItem.getQuantity())) + " " + entries[Integer.parseInt(mainPref.getString("listPreferenceUiCurrency", "0"))]);
        }
        else
            holder.llPrice.setVisibility(View.GONE);

        if (myItem.getQuantity() != 0.0)
        {
            CharSequence[] entries = ctx.getResources().getStringArray(R.array.unit_entries);

            holder.llQuantity.setVisibility(View.VISIBLE);
            holder.textViewQuantity.setText(Double.toString(myItem.getQuantity()) + " " + entries[myItem.getUnit()]);
        }
        else
            holder.llQuantity.setVisibility(View.GONE);

        // when there is a long click on a row, either remove item if the toolbar of the activity is not opened, or hide the toolbar if it is opened. More details below
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {

                if (!selectedIndex.contains(holder.getLayoutPosition()) && !ActivityShop.toolbarOpened)
                {
                    ActivityShop.currentItem = myItem;

                    v.setBackgroundColor(Color.parseColor("#C3C3C3"));
                    selectedItem.add(myItem);
                    selectedHolder.add(holder);
                    selectedIndex.add(holder.getLayoutPosition());

                    ((ActivityShop)ctx).pressSelect();
                }

                return true;
            }
        });





        // if current row is barcode_done, change the color or the icon and the style of the text
        if (myItem.getDone())
        {
            holder.txtHeader.setPaintFlags(holder.txtHeader.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.txtFooter.setPaintFlags(holder.txtFooter.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.imageViewItemIcon.setColorFilter(ctx.getResources().getColor(R.color.grey));
            holder.imageViewItemIcon.setImageDrawable(ctx.getResources().getDrawable(R.mipmap.ic_nicon));
            holder.imageViewPrice.setColorFilter(ctx.getResources().getColor(R.color.grey));
            holder.imageViewQuantity.setColorFilter(ctx.getResources().getColor(R.color.grey));
            holder.textViewPrice.setTextColor(ctx.getResources().getColor(R.color.grey));
            holder.textViewQuantity.setTextColor(ctx.getResources().getColor(R.color.grey));
        }
        else
        {
            holder.txtHeader.setPaintFlags(holder.txtHeader.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
            holder.txtFooter.setPaintFlags(holder.txtFooter.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
            holder.imageViewItemIcon.setColorFilter(Misc.getColor(ctx, myItem.getCategory()));
            holder.imageViewItemIcon.setImageDrawable(ctx.getResources().getDrawable(R.mipmap.ic_icon));
            holder.imageViewPrice.setColorFilter(ctx.getResources().getColor(R.color.green));
            holder.imageViewQuantity.setColorFilter(ctx.getResources().getColor(R.color.orange));
            holder.textViewPrice.setTextColor(ctx.getResources().getColor(R.color.green));
            holder.textViewQuantity.setTextColor(ctx.getResources().getColor(R.color.orange));
        }



        // when doing a click on the row, put it barcode_done
        holder.itemView.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!ActivityShop.toolbarOpened)
                {
                    if (selectedIndex.contains(holder.getLayoutPosition()))
                    {
                        holder.itemView.setBackgroundColor(Color.TRANSPARENT);

                        selectedHolder.remove(holder);
                        selectedIndex.remove(selectedIndex.indexOf(holder.getLayoutPosition()));
                        selectedItem.remove(myItem);

                        ((ActivityShop)ctx).pressSelect();
                    }
                    else if (!selectedIndex.contains(holder.getLayoutPosition()) && selectedIndex.size() != 0)
                    {
                        holder.itemView.setBackgroundColor(Color.parseColor("#C3C3C3"));

                        selectedHolder.add(holder);
                        selectedIndex.add(holder.getLayoutPosition());
                        selectedItem.add(myItem);

                        ((ActivityShop)ctx).pressSelect();
                    }
                    else if (selectedIndex.size() == 0)
                    {
                        done(myItem);

                        // make vibration
                        Vibrator vbr = (Vibrator) ctx.getSystemService(Context.VIBRATOR_SERVICE);
                        if (mainPref.getBoolean("checkBoxSystemVibration", true) && vbr.hasVibrator())
                        {
                            vbr.vibrate(14);
                        }

                        if (myItem.getDone())
                        {
                            holder.txtHeader.setPaintFlags(holder.txtHeader.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            holder.txtFooter.setPaintFlags(holder.txtFooter.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            holder.imageViewItemIcon.setColorFilter(ctx.getResources().getColor(R.color.grey));
                            holder.imageViewItemIcon.setImageDrawable(ctx.getResources().getDrawable(R.mipmap.ic_nicon));
                            holder.imageViewPrice.setColorFilter(ctx.getResources().getColor(R.color.grey));
                            holder.imageViewQuantity.setColorFilter(ctx.getResources().getColor(R.color.grey));
                            holder.textViewPrice.setTextColor(ctx.getResources().getColor(R.color.grey));
                            holder.textViewQuantity.setTextColor(ctx.getResources().getColor(R.color.grey));
                        }
                        else
                        {
                            holder.txtHeader.setPaintFlags(holder.txtHeader.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                            holder.txtFooter.setPaintFlags(holder.txtFooter.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                            holder.imageViewItemIcon.setColorFilter(Misc.getColor(ctx, myItem.getCategory()));
                            holder.imageViewItemIcon.setImageDrawable(ctx.getResources().getDrawable(R.mipmap.ic_icon));
                            holder.imageViewPrice.setColorFilter(ctx.getResources().getColor(R.color.green));
                            holder.imageViewQuantity.setColorFilter(ctx.getResources().getColor(R.color.orange));
                            holder.textViewPrice.setTextColor(ctx.getResources().getColor(R.color.green));
                            holder.textViewQuantity.setTextColor(ctx.getResources().getColor(R.color.orange));
                        }
                    }
                }
            }
        });

    }


    @Override
    public int getItemCount()
    {
        return Static.currentList.itemShop.size();
    }
}
