package com.sybiload.shopp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.sybiload.shopp.Adapter.EditTextAdapter;
import com.sybiload.shopp.Database.Item.DatabaseItem;

import java.util.ArrayList;


public class ActivityShop extends ActionBarActivity
{
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    Toolbar toolbar;
    private EditTextAdapter editTextName;
    private EditText editTextDescription;

    public static Item currentItem;
    public static AdapterShop currentAdapter;

    ImageButton fabImageButton;

    public static boolean toolbarOpened = false;

    protected void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_item);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        editTextName = (EditTextAdapter)findViewById(R.id.editTextItemName);
        editTextDescription = (EditText)findViewById(R.id.editTextItemDescription);

        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setTitle(Static.currentList.getName());
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
                        Item newItem = new Item(editTextName.getText().toString(), editTextDescription.getText().toString(), currentItem.getIcon(), currentItem.isToShop(), currentItem.isDone());


                        currentAdapter.update(currentItem, newItem);

                        barAction();

                        return true;
                }

                return false;
            }
        });

        editTextName.addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable s)
            {
                boolean error = false;
                String text = editTextName.getText().toString();

                for (Item it : Static.currentList.itemShop)
                {
                    if (text.toLowerCase().equals(it.getName().toLowerCase()))
                    {
                        error = true;
                        break;
                    }
                }

                if (toolbarOpened && error)
                {
                    editTextName.setError("");

                    toolbar.getMenu().findItem(R.id.action_done).setEnabled(false);
                }
                else if (toolbarOpened && text.length() == 0)
                {
                    toolbar.getMenu().findItem(R.id.action_done).setEnabled(false);
                }
                else if (toolbarOpened)
                {
                    editTextName.setError(null);
                    toolbar.getMenu().findItem(R.id.action_done).setEnabled(true);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            public void onTextChanged(CharSequence s, int start, int before, int count) { }
        });

        fabImageButton = (ImageButton) findViewById(R.id.imageButtonItemFab);

        fabImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivityAdd.class);
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
        AdapterShop adapterShop = new AdapterShop(ActivityShop.this);
        recyclerView.setAdapter(adapterShop);

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

            // solve the keyboard focus bug by removing the keyboard manually
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editTextName.getWindowToken(), 0);

            toolbar.getMenu().clear();

            fabImageButton.setClickable(true);


            Animation scaleAnim = AnimationUtils.loadAnimation(this, R.anim.scale_up);
            scaleAnim.setFillAfter(true);
            fabImageButton.startAnimation(scaleAnim);

            toolbarOpened = false;

            currentAdapter = null;
            currentItem = null;
        }
        else
        {
            expand(toolbar);
            llEditItem.setVisibility(View.VISIBLE);

            // set name and description editText
            editTextName.setText(currentItem.getName());
            editTextDescription.setText(currentItem.getDescription());

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