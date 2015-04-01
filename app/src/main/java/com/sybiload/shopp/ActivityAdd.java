package com.sybiload.shopp;

import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
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
    private TextView textViewBarcode;
    private ImageView imageViewColorGreen;
    private ImageView imageViewColorOrange;
    private ImageView imageViewColorRed;
    private ImageView imageViewColorPurple;
    private ImageView imageViewColorBlue;

    Toolbar toolbar;
    private EditTextAdapter editTextName;

    private AdapterAdd currAdap = null;

    public static boolean toolbarOpened = false;
    public static boolean editMode = false;
    private String barType = null;
    private String barCode = null;
    private int color = 1;

    protected void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_additem);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        editTextName = (EditTextAdapter)findViewById(R.id.editTextAddItemName);
        fabImageButton = (ImageButton) findViewById(R.id.imageButtonAddItemFab);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler);
        searchView = (SearchView) toolbar.findViewById(R.id.searchViewAdd);
        textViewBarcode = (TextView) findViewById(R.id.textViewAddItemBarcode);
        imageViewColorGreen = (ImageView)findViewById(R.id.imageViewAddItemColorGreen);
        imageViewColorOrange = (ImageView)findViewById(R.id.imageViewAddItemColorOrange);
        imageViewColorRed = (ImageView)findViewById(R.id.imageViewAddItemColorRed);
        imageViewColorPurple = (ImageView)findViewById(R.id.imageViewAddItemColorPurple);
        imageViewColorBlue = (ImageView)findViewById(R.id.imageViewAddItemColorBlue);

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
                        // if there is a bug, it's here
                        Item newItem = new Item(editTextName.getText().toString(), null, color, barType, barCode, false, false);

                        // add new item to the itemAvailable and sort the list
                        Static.currentList.itemAvailable.add(newItem);
                        Static.currentList.sortAvailable();

                        // update database with the new item
                        DatabaseItem database = new DatabaseItem(getApplicationContext(), Static.currentList.getDatabase());
                        database.open();

                        database.insertItem(newItem);

                        database.close();

                        // update the recyclerView with the new item
                        currAdap = new AdapterAdd(ActivityAdd.this);
                        recyclerView.setAdapter(currAdap);

                        // reset barType and barCode
                        barType = null;
                        barCode = null;

                        barAction();

                        return true;
                    case R.id.action_barcode:

                        IntentIntegrator integrator = new IntentIntegrator(ActivityAdd.this);
                        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                        integrator.setPrompt("Scan your product's barcode");
                        integrator.setResultDisplayDuration(0);
                        integrator.initiateScan();

                        return true;

                    case R.id.action_delete:


                        currAdap.delete();
                        currAdap.clearSelected();
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

        imageViewColorGreen.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                color = 1;
                setColorImageView(1);
            }
        });

        imageViewColorOrange.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                color = 2;
                setColorImageView(2);
            }
        });

        imageViewColorRed.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                color = 3;
                setColorImageView(3);
            }
        });

        imageViewColorPurple.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                color = 4;
                setColorImageView(4);
            }
        });

        imageViewColorBlue.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                color = 5;
                setColorImageView(5);
            }
        });


        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        // adding all items available
        currAdap = new AdapterAdd(ActivityAdd.this);
        recyclerView.setAdapter(currAdap);


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

                if (toolbarOpened)
                {
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

                    if (error)
                    {
                        editTextName.setError("");

                        toolbar.getMenu().findItem(R.id.action_done).setEnabled(false);
                    }
                    else if (text.isEmpty())
                    {
                        toolbar.getMenu().findItem(R.id.action_done).setEnabled(false);
                    }
                    else
                    {
                        editTextName.setError(null);
                        toolbar.getMenu().findItem(R.id.action_done).setEnabled(true);
                    }
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
                currAdap = new AdapterAdd(ActivityAdd.this);
                recyclerView.setAdapter(currAdap);

                currAdap.clearSelected();

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

    @Override
    public void onPause()
    {
        // solve bug while switching to another app and switching back to this one
        if (!AdapterAdd.selectedHolder.isEmpty())
        {
            currAdap.clearSelected();
        }

        super.onPause();
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
            toolbarOpened = false;

            // erase previous layout
            toolbar.getMenu().clear();

            // hide fields
            llEditItem.setVisibility(View.GONE);
            searchView.setVisibility(View.VISIBLE);
            textViewBarcode.setVisibility(View.GONE);

            // solve the keyboard focus bug by removing the keyboard manually
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editTextName.getWindowToken(), 0);

            // restore fabButton
            fabUp();

            // reset barType, barCode and color
            barType = null;
            barCode = null;
            color = 1;
        }
        else
        {
            expand(toolbar);
            toolbarOpened = true;

            // erase previous layout and replace it
            toolbar.getMenu().clear();
            toolbar.inflateMenu(R.menu.done);
            toolbar.getMenu().findItem(R.id.action_done).setEnabled(false);

            // reset text fields and color
            editTextName.setText(null);
            textViewBarcode.setText(null);
            setColorImageView(color);

            // hide fields
            searchView.setVisibility(View.GONE);
            llEditItem.setVisibility(View.VISIBLE);

            // remove fabButton
            fabDown();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (scanningResult.getContents() != null)
        {
            Toast.makeText(getApplicationContext(), "Barcode scanned", Toast.LENGTH_SHORT).show();

            barType = scanningResult.getFormatName();
            barCode = scanningResult.getContents();
            Item myItem = null;

            for (Item it : Static.currentList.itemAvailable)
            {
                if (it.getBarType() != null && it.getBarCode() != null && it.getBarType().equals(barType) && it.getBarCode().equals(barCode))
                {
                    myItem = it;
                    break;
                }
            }

            for (Item it : Static.currentList.itemShop)
            {
                if (it.getBarType() != null && it.getBarCode() != null && it.getBarType().equals(barType) && it.getBarCode().equals(barCode))
                {
                    myItem = it;
                    break;
                }
            }

            if (myItem != null)
            {
                Toast.makeText(getApplicationContext(), "This barcode is already assigned", Toast.LENGTH_SHORT).show();

                // reset barcode strings
                barType = null;
                barCode = null;
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Barcode scanned", Toast.LENGTH_SHORT).show();
                textViewBarcode.setVisibility(View.VISIBLE);
                textViewBarcode.setText(barType + " - " + barCode);
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "No barcode scanned", Toast.LENGTH_SHORT).show();
        }
    }

    public void pressSelect()
    {
        if (!toolbarOpened && AdapterAdd.selectedHolder.size() == 0)
        {
            toolbar.getMenu().clear();

            // restore new item option removed to prevent bug abuse
            fabUp();
        }
        else if (!toolbarOpened)
        {
            toolbar.getMenu().clear();
            toolbar.inflateMenu(R.menu.delete);

            // remove new item option to prevent bug abuse
            fabDown();
        }
    }

    private void fabUp()
    {
        if (!fabImageButton.isClickable())
        {
            fabImageButton.setClickable(true);

            Animation scaleAnim = AnimationUtils.loadAnimation(this, R.anim.scale_up);
            scaleAnim.setFillAfter(true);
            fabImageButton.startAnimation(scaleAnim);
        }
    }

    private void fabDown()
    {
        if (fabImageButton.isClickable())
        {
            fabImageButton.setClickable(false);

            Animation scaleAnim = AnimationUtils.loadAnimation(this, R.anim.scale_down);
            scaleAnim.setFillAfter(true);
            fabImageButton.startAnimation(scaleAnim);
        }
    }

    private void setColorImageView(int id)
    {
        imageViewColorGreen.setImageDrawable(getResources().getDrawable(R.drawable.ic_icon));
        imageViewColorOrange.setImageDrawable(getResources().getDrawable(R.drawable.ic_icon));
        imageViewColorRed.setImageDrawable(getResources().getDrawable(R.drawable.ic_icon));
        imageViewColorPurple.setImageDrawable(getResources().getDrawable(R.drawable.ic_icon));
        imageViewColorBlue.setImageDrawable(getResources().getDrawable(R.drawable.ic_icon));

        switch (id)
        {
            case 1:
                imageViewColorGreen.setImageDrawable(getResources().getDrawable(R.drawable.ic_nicon));
                break;
            case 2:
                imageViewColorOrange.setImageDrawable(getResources().getDrawable(R.drawable.ic_nicon));
                break;
            case 3:
                imageViewColorRed.setImageDrawable(getResources().getDrawable(R.drawable.ic_nicon));
                break;
            case 4:
                imageViewColorPurple.setImageDrawable(getResources().getDrawable(R.drawable.ic_nicon));
                break;
            case 5:
                imageViewColorBlue.setImageDrawable(getResources().getDrawable(R.drawable.ic_nicon));
                break;
        }
    }

    public void onBackPressed()
    {

        // if research is opened, reset the field and close it, otherwise finish the activity
        if (toolbarOpened)
        {
            barAction();
        }
        else if (!AdapterAdd.selectedHolder.isEmpty())
        {
            currAdap.clearSelected();
        }
        else if (searchView.isIconified() && !toolbarOpened)
        {
            finish();
        }
        else
        {
            searchView.setQuery("", false);
            searchView.clearFocus();
            searchView.setIconified(true);
        }

    }
}