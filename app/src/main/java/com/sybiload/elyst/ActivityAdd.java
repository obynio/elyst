package com.sybiload.elyst;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Explode;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import com.sybiload.elyst.Adapter.AdapterAdd;
import com.sybiload.elyst.Database.Item.DatabaseItem;

import java.util.ArrayList;

public class ActivityAdd extends AppCompatActivity
{
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private SearchView searchView;
    private ImageButton fabImageButton;
    private TextView textViewBarcode;
    private TextView textViewCurrency;
    private ImageView imageViewColorGreen;
    private ImageView imageViewColorOrange;
    private ImageView imageViewColorRed;
    private ImageView imageViewColorPurple;
    private ImageView imageViewColorBlue;
    private EditText editTextName;
    private EditText editTextPrice;

    public static Item currentItem;

    Toolbar toolbar;


    private AdapterAdd currAdap = null;

    public static boolean toolbarOpened = false;
    private String barType = null;
    private String barCode = null;
    private int color = 1;

    private SharedPreferences mainPref;

    protected void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);

        mainPref = getApplicationContext().getSharedPreferences("main", 0);
        setContentView(R.layout.activity_additem);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        editTextName = (EditText)findViewById(R.id.editTextAddItemName);
        editTextPrice = (EditText)findViewById(R.id.editTextAddItemPrice);
        fabImageButton = (ImageButton) findViewById(R.id.imageButtonAddItemFab);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler);
        searchView = (SearchView) toolbar.findViewById(R.id.searchViewAdd);
        textViewBarcode = (TextView) findViewById(R.id.textViewAddItemBarcode);
        textViewCurrency = (TextView)findViewById(R.id.textViewAddItemCurrency);
        imageViewColorGreen = (ImageView)findViewById(R.id.imageViewAddItemColorGreen);
        imageViewColorOrange = (ImageView)findViewById(R.id.imageViewAddItemColorOrange);
        imageViewColorRed = (ImageView)findViewById(R.id.imageViewAddItemColorRed);
        imageViewColorPurple = (ImageView)findViewById(R.id.imageViewAddItemColorPurple);
        imageViewColorBlue = (ImageView)findViewById(R.id.imageViewAddItemColorBlue);



        if (mainPref.getBoolean("checkBoxSystemNoSleep", false))
        {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        if (mainPref.getBoolean("checkBoxSystemNoLock", false))
        {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        }

        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (toolbarOpened)
                {
                    barAction();
                }
                else if (!AdapterAdd.selectedIndex.isEmpty())
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
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {

                switch (item.getItemId())
                {
                    case R.id.action_done:

                        if (currentItem == null)
                        {
                            String priceStr = editTextPrice.getText().toString();
                            double price = (priceStr != null && !priceStr.equals("")) ? Double.parseDouble(priceStr) : 0.0;

                            // if there is a bug, it's here
                            Item newItem = new Item(Misc.generateSeed(), editTextName.getText().toString(), null, color, price, 0.0, 0, barType, barCode, false);

                            // add new item to the itemAvailable and sort the list
                            Static.currentList.itemAvailable.add(newItem);
                            Static.currentList.sortAvailable(getApplicationContext());

                            // update database with the new item
                            Misc.createItem(getApplicationContext(), newItem);

                            // update the recyclerView with the new item
                            currAdap = new AdapterAdd(ActivityAdd.this);
                            recyclerView.setAdapter(currAdap);

                            // reset barType and barCode
                            barType = null;
                            barCode = null;
                        }
                        else
                        {
                            String priceStr = editTextPrice.getText().toString();
                            double price = (priceStr != null && !priceStr.equals("")) ? Double.parseDouble(priceStr) : 0.0;

                            Item newItem = new Item(currentItem.getIdItem(), editTextName.getText().toString(), null, currentItem.getCategory(), price, 0.0, 0, barType, barCode, currentItem.getDone());

                            currAdap.update(newItem);

                            // reset barType and barCode
                            barType = null;
                            barCode = null;
                        }

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

                    case R.id.action_edit:

                        barAction();
                        return true;

                    default:
                        return false;
                }
            }
        });

        // listener to show the currency edit text when text is entered
        editTextPrice.addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable s)
            {
                if (s.toString().isEmpty())
                    textViewCurrency.setVisibility(View.GONE);
                else
                    textViewCurrency.setVisibility(View.VISIBLE);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            public void onTextChanged(CharSequence s, int start, int before, int count) { }
        });

        fabImageButton.setVisibility(View.GONE);
        fabImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentItem = null;
                barAction();
            }
        });

        imageViewColorGreen.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (currentItem != null)
                    currentItem.setCategory(1);
                else
                    color = 1;

                setColorImageView(1);
            }
        });

        imageViewColorOrange.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (currentItem != null)
                    currentItem.setCategory(2);
                else
                    color = 2;

                setColorImageView(2);
            }
        });

        imageViewColorRed.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (currentItem != null)
                    currentItem.setCategory(3);
                else
                    color = 3;

                setColorImageView(3);
            }
        });

        imageViewColorPurple.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (currentItem != null)
                    currentItem.setCategory(4);
                else
                    color = 4;

                setColorImageView(4);
            }
        });

        imageViewColorBlue.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (currentItem != null)
                    currentItem.setCategory(5);
                else
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
        searchViewText.setHint("Search something..");
        searchViewText.setHintTextColor(Color.parseColor("#C5CAE9"));

        editTextName.addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable s)
            {
                if (toolbarOpened)
                {
                    if (s.toString().isEmpty())
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
                DatabaseItem database = new DatabaseItem(getApplicationContext(), Static.currentList.getIdDb());
                database.open();

                // search item into the whole database
                ArrayList<Item> searchItems = database.searchItem(s.toLowerCase());

                database.dbIndex.close();
                database.dbChild.close();

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
        if (!AdapterAdd.selectedIndex.isEmpty())
        {
            currAdap.clearSelected();
        }

        super.onPause();
    }

    private void expand(View v)
    {
        ValueAnimator mAnimator = slideAnimator(v, (int)Misc.dpToPx(getApplicationContext(), 56.0), (int)Misc.dpToPx(getApplicationContext(), 233.0));
        mAnimator.start();
    }

    private void collapse(View v)
    {
        ValueAnimator mAnimator = slideAnimator(v, (int)Misc.dpToPx(getApplicationContext(), 233.0), (int)Misc.dpToPx(getApplicationContext(), 56.0));
        mAnimator.start();
    }

    private ValueAnimator slideAnimator(final View v, int start, int end) {

        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator)
            {
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
                layoutParams.height = value;
                v.setLayoutParams(layoutParams);
            }
        });

        return animator;
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

            if (mainPref.getBoolean("checkBoxSystemBarcode", false))
                toolbar.inflateMenu(R.menu.barcode_done);
            else
                toolbar.inflateMenu(R.menu.done);

            if (AdapterAdd.selectedIndex.isEmpty())
            {
                CharSequence[] currencyEntries = getApplicationContext().getResources().getStringArray(R.array.ui_pref_currency_entries);
                currencyEntries[0] = "";

                // reset text fields and color
                editTextName.setText(null);
                editTextPrice.setText(null);
                textViewBarcode.setText(null);
                textViewCurrency.setText(currencyEntries[Integer.parseInt(mainPref.getString("listPreferenceUiCurrency", "0"))]);
                setColorImageView(color);
            }
            else
            {
                CharSequence[] currencyEntries = getApplicationContext().getResources().getStringArray(R.array.ui_pref_currency_entries);
                currencyEntries[0] = "";

                // reset text fields and color
                editTextName.setText(currentItem.getName());
                editTextPrice.setText(currentItem.getPrice() == 0.0 ? null : Double.toString(currentItem.getPrice()));
                textViewCurrency.setText(currencyEntries[Integer.parseInt(mainPref.getString("listPreferenceUiCurrency", "0"))]);

                setColorImageView(currentItem.getCategory());

                // check if a barcode exists and set visible if yes
                if (currentItem.getBarType() != null && currentItem.getBarCode() != null)
                {
                    textViewBarcode.setVisibility(View.VISIBLE);
                    textViewBarcode.setText(currentItem.getBarType() + " - " + currentItem.getBarCode());
                }
            }

            // hide fields
            searchView.setVisibility(View.GONE);
            llEditItem.setVisibility(View.VISIBLE);

            // unselect all selected items
            currAdap.clearSelected();

            // remove fabButton
            fabDown();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (scanningResult.getContents() != null)
        {
            Item myItem = null;

            barType = scanningResult.getFormatName();
            barCode = scanningResult.getContents();


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
                Toast.makeText(getApplicationContext(), "Barcode already assigned to " + myItem.getName(), Toast.LENGTH_SHORT).show();

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
        if (!toolbarOpened && AdapterAdd.selectedIndex.size() == 0)
        {
            toolbar.getMenu().clear();
            searchView.setVisibility(View.VISIBLE);

            // restore new item option removed to prevent bug abuse
            fabUp();
        }
        else if (!toolbarOpened && AdapterAdd.selectedIndex.size() == 1)
        {
            searchView.setVisibility(View.GONE);
            toolbar.getMenu().clear();
            toolbar.inflateMenu(R.menu.edit_delete);

            // remove new item option to prevent bug abuse
            fabDown();
        }
        else if (!toolbarOpened)
        {
            toolbar.getMenu().clear();
            toolbar.inflateMenu(R.menu.delete);
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
        imageViewColorGreen.setImageDrawable(getResources().getDrawable(R.mipmap.ic_icon));
        imageViewColorOrange.setImageDrawable(getResources().getDrawable(R.mipmap.ic_icon));
        imageViewColorRed.setImageDrawable(getResources().getDrawable(R.mipmap.ic_icon));
        imageViewColorPurple.setImageDrawable(getResources().getDrawable(R.mipmap.ic_icon));
        imageViewColorBlue.setImageDrawable(getResources().getDrawable(R.mipmap.ic_icon));

        switch (id)
        {
            case 1:
                imageViewColorGreen.setImageDrawable(getResources().getDrawable(R.mipmap.ic_nicon));
                break;
            case 2:
                imageViewColorOrange.setImageDrawable(getResources().getDrawable(R.mipmap.ic_nicon));
                break;
            case 3:
                imageViewColorRed.setImageDrawable(getResources().getDrawable(R.mipmap.ic_nicon));
                break;
            case 4:
                imageViewColorPurple.setImageDrawable(getResources().getDrawable(R.mipmap.ic_nicon));
                break;
            case 5:
                imageViewColorBlue.setImageDrawable(getResources().getDrawable(R.mipmap.ic_nicon));
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
        else if (!AdapterAdd.selectedIndex.isEmpty())
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