package com.sybiload.shopp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.sybiload.shopp.Adapter.AdapterAdd;
import com.sybiload.shopp.Adapter.AdapterShop;
import com.sybiload.shopp.Adapter.EditTextAdapter;
import com.sybiload.shopp.Database.Item.DatabaseItem;
import com.sybiload.shopp.Database.List.DatabaseList;

import java.util.ArrayList;


public class ActivityAdd extends ActionBarActivity
{
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private SearchView searchView;
    private ImageButton fabImageButton;

    Toolbar toolbar;
    private EditTextAdapter editTextName;
    private EditText editTextDescription;

    public static boolean toolbarOpened = false;

    protected void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_additem);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        editTextName = (EditTextAdapter)findViewById(R.id.editTextAddItemName);
        editTextDescription = (EditText)findViewById(R.id.editTextAddItemDescription);
        fabImageButton = (ImageButton) findViewById(R.id.imageButtonAddItemFab);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler);
        searchView = (SearchView) toolbar.findViewById(R.id.searchViewAdd);

        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setTitle("Add items");
        //toolbar.setSubtitle("Let's dance !");
        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (searchView.isIconified() && !toolbarOpened)
                    finish();
                else if (toolbarOpened)
                    barAction();
                else
                {
                    searchView.setQuery("", false);
                    searchView.clearFocus();
                    searchView.setIconified(true);
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
                        Item newItem = new Item(editTextName.getText().toString(), editTextDescription.getText().toString(), R.mipmap.ic_launcher, false, false);

                        // add new item to the itemAvailable and sort the list
                        Static.currentList.itemAvailable.add(newItem);
                        Static.currentList.sortAvailable();

                        // update database with the new item
                        DatabaseItem database = new DatabaseItem(getApplicationContext(), Static.currentList.getDatabase());
                        database.open();

                        database.insertItem(newItem);

                        database.close();

                        // update the recyclerView with the new item
                        AdapterAdd adapterAdd = new AdapterAdd(getApplicationContext());
                        recyclerView.setAdapter(adapterAdd);

                        barAction();

                        return true;
                }

                return false;
            }
        });

        fabImageButton.setVisibility(View.GONE);
        fabImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barAction();
            }
        });


        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        // adding all items available
        AdapterAdd adapterAdd = new AdapterAdd(getApplicationContext());
        recyclerView.setAdapter(adapterAdd);


        // change searchView text color
        SearchView.SearchAutoComplete searchViewText = (SearchView.SearchAutoComplete) searchView.findViewById(R.id.search_src_text);
        searchViewText.setTextColor(Color.WHITE);
        searchViewText.setHint("Search an item");
        searchViewText.setHintTextColor(Color.parseColor("#C5CAE9"));

        editTextName.addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable s)
            {
                boolean error = false;
                String text = editTextName.getText().toString();

                // check if an item to shop already exists
                for (Item it : Static.currentList.itemShop)
                {
                    if (text.toLowerCase().equals(it.getName().toLowerCase()))
                    {
                        error = true;
                        break;
                    }
                }

                // check if an item available already exists
                for (Item it : Static.currentList.itemAvailable)
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

        // listen to search text
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String s)
            {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s)
            {
                DatabaseItem database = new DatabaseItem(getApplicationContext(), Static.currentList.getDatabase());
                database.open();

                // search item into the whole database
                ArrayList<Item> searchItems = database.searchItem(s.toLowerCase());

                database.close();

                // add it to the itemAvailable list
                Static.currentList.itemAvailable = searchItems;

                // update the recyclerView with the results
                AdapterAdd adapterAdd = new AdapterAdd(getApplicationContext());
                recyclerView.setAdapter(adapterAdd);

                return false;
            }
        });

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                fabImageButton.setVisibility(View.VISIBLE);

                Animation scaleAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_up);
                scaleAnim.setFillAfter(true);
                fabImageButton.startAnimation(scaleAnim);
            }
        }, 200);
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
        RelativeLayout llEditItem = (RelativeLayout)findViewById(R.id.llEditAddItem);

        if (toolbarOpened)
        {
            collapse(toolbar);
            llEditItem.setVisibility(View.GONE);
            searchView.setVisibility(View.VISIBLE);

            // reset text fields
            editTextName.setText(null);
            editTextDescription.setText(null);

            // solve the keyboard focus bug by removing the keyboard manually
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editTextName.getWindowToken(), 0);

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

            searchView.setVisibility(View.GONE);
            llEditItem.setVisibility(View.VISIBLE);

            toolbar.inflateMenu(R.menu.done);
            toolbar.getMenu().findItem(R.id.action_done).setEnabled(false);

            fabImageButton.setClickable(false);

            Animation scaleAnim = AnimationUtils.loadAnimation(this, R.anim.scale_down);
            scaleAnim.setFillAfter(true);

            fabImageButton.startAnimation(scaleAnim);

            toolbarOpened = true;
        }
    }

    public void onBackPressed()
    {

        // if research is opened, reset the field and close it, otherwise finish the activity
        if (searchView.isIconified() && !toolbarOpened)
            finish();
        else if (toolbarOpened)
            barAction();
        else
        {
            searchView.setQuery("", false);
            searchView.clearFocus();
            searchView.setIconified(true);
        }

    }
}