package com.sybiload.elyst.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.sybiload.elyst.Database.List.DatabaseList;
import com.sybiload.elyst.FragmentList;
import com.sybiload.elyst.List;
import com.sybiload.elyst.Misc;
import com.sybiload.elyst.R;
import com.sybiload.elyst.Static;

public class AdapterNewList extends RecyclerView.Adapter<AdapterNewList.ViewHolder>
{
    private Context ctx;
    private SharedPreferences mainPref;

    public int selectedIndex = 0;
    public ViewHolder selectedHolder = null;

    public AdapterNewList(Context ctx)
    {
        this.ctx = ctx;
        mainPref = ctx.getSharedPreferences("main", 0);

        selectedIndex = (Static.currentList == null) ? 0 : Static.currentList.getBackground();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView imageView;

        public ViewHolder(View v)
        {
            super(v);

            imageView = (ImageView) v.findViewById(R.id.imageViewList);
        }
    }

    @Override
    public AdapterNewList.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position)
    {
        if (selectedIndex == holder.getPosition())
        {
            holder.imageView.setColorFilter(Color.parseColor("#66FFFFFF"));
            selectedHolder = holder;
            selectedIndex = holder.getPosition();
        }

        holder.setIsRecyclable(false);
        new updateBmpAsync().execute(Static.cardDrw[holder.getPosition()], holder);

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // avoid this fucking not already shown bug
                if (selectedHolder != null)
                    selectedHolder.imageView.setColorFilter(Color.TRANSPARENT);

                holder.imageView.setColorFilter(Color.parseColor("#66FFFFFF"));

                selectedHolder = holder;
                selectedIndex = holder.getPosition();
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return Static.cardDrw.length;
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
            bitmap = BitmapFactory.decodeResource(ctx.getResources(), (Integer) params[0], options);

            return null;
        }

        @Override
        protected void onPostExecute(Void unused)
        {
            Animation fadeInAnimation = AnimationUtils.loadAnimation(ctx, R.anim.fade_in);
            myHolder.imageView.startAnimation(fadeInAnimation);
            myHolder.imageView.setImageBitmap(bitmap);
        }
    }

}
