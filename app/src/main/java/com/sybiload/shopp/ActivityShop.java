package com.sybiload.shopp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.sybiload.shopp.Adapter.AdapterShop;


public class ActivityShop extends ActionBarActivity
{
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    public static EditText editTextName;
    public static EditText editTextDescription;

    ImageButton fabImageButton;
    int listNumber = 0;

    List newList;

    public static boolean toolbarOpened = false;

    protected void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_item);

        editTextName = (EditText)findViewById(R.id.editTextItemName);
        editTextDescription = (EditText)findViewById(R.id.editTextItemDescription);

        Intent intent = getIntent();

        if (intent != null)
            listNumber = intent.getIntExtra("LIST_NUMBER", 0);

        newList = Static.allList.get(listNumber);

        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setTitle(newList.getName());
        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (toolbarOpened)
                    barAction();
                else
                {
                    finish();
                    new Misc().rightTransition(ActivityShop.this);
                }
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {

                switch (item.getItemId())
                {
                    case R.id.action_done:
                        barAction();



                        return true;
                }

                return false;
            }
        });

        fabImageButton = (ImageButton) findViewById(R.id.imageButtonItemFab);

        fabImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivityAdd.class);
                intent.putExtra("LIST_NUMBER", listNumber);
                startActivity(intent);
            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewItem);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onResume()
    {

        // add all items to shop
        AdapterShop adapterShop = new AdapterShop(ActivityShop.this, newList);
        recyclerView.setAdapter(adapterShop);

        // testing
        List l = newList;

        super.onResume();
    }

    public static void expand(final View v) {
        final int targetHeight = 400;
        final int startHeight = v.getHeight();

        Animation a = new Animation()
        {

            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1 ? v.getHeight() : (int)(targetHeight * interpolatedTime + startHeight);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration(200);
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int targetHeight = 168;
        final int initialHeight = v.getHeight() - targetHeight;

        Animation a = new Animation()
        {

            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1 ? targetHeight : targetHeight + initialHeight -  (int)(initialHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration(200);
        v.startAnimation(a);
    }

    public void barAction()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        RelativeLayout llEditItem = (RelativeLayout)findViewById(R.id.llEditItem);

        if (toolbarOpened)
        {
            collapse(toolbar);
            llEditItem.setVisibility(View.GONE);
            toolbar.getMenu().clear();

            fabImageButton.setClickable(true);


            Animation scaleAnim = AnimationUtils.loadAnimation(this, R.anim.scale_up);
            scaleAnim.setFillAfter(true);
            fabImageButton.startAnimation(scaleAnim);

            toolbarOpened = false;
        }
        else
        {
            expand(toolbar);
            llEditItem.setVisibility(View.VISIBLE);
            toolbar.inflateMenu(R.menu.done);

            fabImageButton.setClickable(false);

            Animation scaleAnim = AnimationUtils.loadAnimation(this, R.anim.scale_down);
            scaleAnim.setFillAfter(true);

            fabImageButton.startAnimation(scaleAnim);

            toolbarOpened = true;
        }
    }

    public void onBackPressed()
    {
        if (toolbarOpened)
            barAction();
        else
        {
            finish();
            new Misc().rightTransition(ActivityShop.this);
        }
    }
}