package com.sybiload.elyst;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.sybiload.elyst.Adapter.AdapterShop;
import com.sybiload.elyst.Adapter.AdapterEditText;
import com.sybiload.elyst.Database.Item.DatabaseItem;


public class ActivityShop extends ActionBarActivity
{
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    Toolbar toolbar;
    private EditText editTextName;
    private EditText editTextDescription;
    private TextView textViewBarcode;
    private ImageButton fabImageButton;
    private ImageView imageViewColorGreen;
    private ImageView imageViewColorOrange;
    private ImageView imageViewColorRed;
    private ImageView imageViewColorPurple;
    private ImageView imageViewColorBlue;

    public static Item currentItem;
    private AdapterShop currAdap = null;

    public static boolean toolbarOpened = false;
    private String barType = null;
    private String barCode = null;

    // test
    public static ActivityShop activity;

    private SharedPreferences mainPref;

    protected void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        mainPref = getApplicationContext().getSharedPreferences("main", 0);
        setContentView(R.layout.activity_item);

        //test
        activity = this;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        editTextName = (EditText)findViewById(R.id.editTextItemName);
        editTextDescription = (EditText)findViewById(R.id.editTextItemDescription);
        textViewBarcode = (TextView)findViewById(R.id.textViewItemBarcode);
        imageViewColorGreen = (ImageView)findViewById(R.id.imageViewItemColorGreen);
        imageViewColorOrange = (ImageView)findViewById(R.id.imageViewItemColorOrange);
        imageViewColorRed = (ImageView)findViewById(R.id.imageViewItemColorRed);
        imageViewColorPurple = (ImageView)findViewById(R.id.imageViewItemColorPurple);
        imageViewColorBlue = (ImageView)findViewById(R.id.imageViewItemColorBlue);

        if (mainPref.getBoolean("checkBoxSystemNoSleep", false))
        {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        if (mainPref.getBoolean("checkBoxSystemNoLock", false))
        {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        }

        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);

        if (mainPref.getBoolean("checkBoxSystemBarcode", false))
            toolbar.inflateMenu(R.menu.barcode);

        toolbar.setTitle(Static.currentList.getName());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toolbarOpened)
                {
                    barAction();
                }
                else if (AdapterShop.selectedIndex.size() > 0)
                {
                    currAdap.clearSelected();
                }
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
                        Item newItem = new Item(currentItem.getIdItem(), editTextName.getText().toString(), editTextDescription.getText().toString(), currentItem.getCategory(), currentItem.getPrice(), currentItem.getQuantity(), currentItem.getUnit(), barType, barCode, currentItem.getDone());

                        currAdap.update(currentItem, newItem);

                        barAction();

                        // reset barType and barCode
                        barType = null;
                        barCode = null;

                        return true;

                    case R.id.action_barcode:

                        IntentIntegrator integrator = new IntentIntegrator(ActivityShop.this);
                        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                        integrator.setPrompt("Scan your product's barcode");
                        integrator.setResultDisplayDuration(0);
                        integrator.initiateScan();

                        return true;

                    case R.id.action_edit:

                        barAction();
                        return true;

                    case R.id.action_remove:

                        currAdap.remove();
                        currAdap.clearSelected();
                        return true;

                    default:
                        return false;
                }
            }
        });

        editTextName.addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable s)
            {
                String text = editTextName.getText().toString();

                if (toolbarOpened)
                {
                    if (text.isEmpty())
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

        fabImageButton = (ImageButton) findViewById(R.id.imageButtonItemFab);

        fabImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivityAdd.class);
                startActivity(intent);
            }
        });

        imageViewColorGreen.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                currentItem.setCategory(1);
                setColorImageView(1);
            }
        });

        imageViewColorOrange.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                currentItem.setCategory(2);
                setColorImageView(2);
            }
        });

        imageViewColorRed.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                currentItem.setCategory(3);
                setColorImageView(3);
            }
        });

        imageViewColorPurple.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                currentItem.setCategory(4);
                setColorImageView(4);
            }
        });

        imageViewColorBlue.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                currentItem.setCategory(5);
                setColorImageView(5);
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
        currAdap = new AdapterShop(ActivityShop.this);
        recyclerView.setAdapter(currAdap);

        super.onResume();
    }

    @Override
    public void onPause()
    {
        // solve bug while switching to another app and switching back to this one
        if (!AdapterShop.selectedIndex.isEmpty())
        {
            currAdap.clearSelected();
        }

        super.onPause();
    }

    private void expand(View v)
    {
        ValueAnimator mAnimator = slideAnimator(v, 168, 600);
        mAnimator.start();
    }

    private void collapse(View v)
    {
        ValueAnimator mAnimator = slideAnimator(v, 600, 168);
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        RelativeLayout llEditItem = (RelativeLayout)findViewById(R.id.llEditItem);

        if (toolbarOpened)
        {
            collapse(toolbar);
            toolbarOpened = false;

            // erase previous layout and replace it
            toolbar.getMenu().clear();

            if (mainPref.getBoolean("checkBoxSystemBarcode", false))
                toolbar.inflateMenu(R.menu.barcode);

            // reset text fields
            editTextName.setText(null);
            editTextDescription.setText(null);
            textViewBarcode.setText(null);

            // hide fields
            llEditItem.setVisibility(View.GONE);
            textViewBarcode.setVisibility(View.GONE);

            // solve the keyboard focus bug by removing the keyboard manually
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editTextName.getWindowToken(), 0);

            // restore fabButton
            fabUp();

            // reset barType, barCode, currentItem
            barType = null;
            barCode = null;
            currentItem = null;
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

            // set name, description editText and color imageView
            editTextName.setText(currentItem.getName());
            editTextDescription.setText(currentItem.getDescription());
            setColorImageView(currentItem.getCategory());

            // visible editText field
            llEditItem.setVisibility(View.VISIBLE);

            // check if a barcode exists and set visible if yes
            if (currentItem.getBarType() != null && currentItem.getBarCode() != null)
            {
                textViewBarcode.setVisibility(View.VISIBLE);
                textViewBarcode.setText(currentItem.getBarType() + " - " + currentItem.getBarCode());
            }

            // unselect all selected items
            currAdap.clearSelected();

            // remove fabButton, maybe useless ?
            fabDown();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (scanningResult.getContents() != null)
        {
            barType = scanningResult.getFormatName();
            barCode = scanningResult.getContents();

            Item myItem = null;

            if (toolbarOpened)
            {
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
                for (Item it : Static.currentList.itemAvailable)
                {
                    if (it.getBarType() != null && it.getBarCode() != null && it.getBarType().equals(barType) && it.getBarCode().equals(barCode))
                    {
                        myItem = it;
                        break;
                    }
                }

                if (myItem != null)
                {
                    myItem.setDone(false);

                    DatabaseItem databaseItem = new DatabaseItem(getApplicationContext(), Static.currentList.getIdDb());

                    // update database
                    new Misc().updateItem(getApplicationContext(), myItem);

                    // get position of our item in the availableItem and remove it
                    int position = Static.currentList.itemAvailable.indexOf(myItem);
                    Static.currentList.itemAvailable.remove(position);

                    // copy the item to shopItem
                    Static.currentList.itemShop.add(myItem);

                    Static.currentList.sortShop(getApplicationContext());
                    Static.currentList.sortShopDone();

                    Toast.makeText(getApplicationContext(), myItem.getName() + " added", Toast.LENGTH_SHORT).show();
                }
                else
                {
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
                        Toast.makeText(getApplicationContext(), "Item already in the shop list", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Item unknown", Toast.LENGTH_SHORT).show();
                    }
                }

                barType = null;
                barCode = null;
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "No barcode scanned", Toast.LENGTH_SHORT).show();
        }
    }

    public void pressSelect()
    {
        if (!toolbarOpened && AdapterShop.selectedIndex.size() == 0)
        {
            toolbar.getMenu().clear();

            if (mainPref.getBoolean("checkBoxSystemBarcode", false))
                toolbar.inflateMenu(R.menu.barcode);

            // restore new item option removed to prevent bug abuse
            fabUp();
        }
        else if (!toolbarOpened && AdapterShop.selectedIndex.size() == 1)
        {
            toolbar.getMenu().clear();
            toolbar.inflateMenu(R.menu.edit_remove);

            // remove new item option to prevent bug abuse
            fabDown();
        }
        else if (!toolbarOpened)
        {
            toolbar.getMenu().clear();
            toolbar.inflateMenu(R.menu.remove);
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
        if (toolbarOpened)
        {
            barAction();
        }
        else if (!AdapterShop.selectedIndex.isEmpty())
        {
            currAdap.clearSelected();
        }
        else
        {
            finish();
            new Misc().rightTransition(ActivityShop.this);
        }
    }
}