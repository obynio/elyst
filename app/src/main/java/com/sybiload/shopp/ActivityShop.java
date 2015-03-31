package com.sybiload.shopp;

import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.sybiload.shopp.Adapter.AdapterShop;
import com.sybiload.shopp.Adapter.EditTextAdapter;
import com.sybiload.shopp.Database.Item.DatabaseItem;


public class ActivityShop extends ActionBarActivity
{
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    Toolbar toolbar;
    private EditTextAdapter editTextName;
    private EditText editTextDescription;
    private TextView textViewBarcode;

    public static Item currentItem;

    AdapterShop currAdap = null;

    ImageButton fabImageButton;

    public static boolean toolbarOpened = false;
    public static boolean selection = false;
    private String barType = null;
    private String barCode = null;

    protected void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_item);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        editTextName = (EditTextAdapter)findViewById(R.id.editTextItemName);
        editTextDescription = (EditText)findViewById(R.id.editTextItemDescription);
        textViewBarcode = (TextView)findViewById(R.id.textViewItemBarcode);

        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.inflateMenu(R.menu.barcode);
        toolbar.setTitle(Static.currentList.getName());
        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (toolbarOpened)
                {
                    barAction();
                }
                else if (AdapterShop.selectedHolder.size() > 0)
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
                        Item newItem = new Item(editTextName.getText().toString(), editTextDescription.getText().toString(), currentItem.getIcon(), barType, barCode, currentItem.isToShop(), currentItem.isDone());

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
        currAdap = new AdapterShop(ActivityShop.this);
        recyclerView.setAdapter(currAdap);

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

            // reset text fields
            editTextName.setText(null);
            editTextDescription.setText(null);
            textViewBarcode.setText(null);

            // erase previous layout and replace it
            toolbar.getMenu().clear();
            toolbar.inflateMenu(R.menu.barcode);

            fabImageButton.setClickable(true);


            Animation scaleAnim = AnimationUtils.loadAnimation(this, R.anim.scale_up);
            scaleAnim.setFillAfter(true);
            fabImageButton.startAnimation(scaleAnim);

            toolbarOpened = false;

            // reset barType, barCode, currentItem and currentAdapter
            barType = null;
            barCode = null;
            currentItem = null;
        }
        else
        {
            expand(toolbar);

            // unselect all selected items
            currAdap.clearSelected();

            llEditItem.setVisibility(View.VISIBLE);

            // set name and description editText
            editTextName.setText(currentItem.getName());
            editTextDescription.setText(currentItem.getDescription());

            if (currentItem.getBarType() != null && currentItem.getBarCode() != null)
                textViewBarcode.setText(currentItem.getBarType() + " - " + currentItem.getBarCode());

            // erase previous layout and replace it
            toolbar.getMenu().clear();
            toolbar.inflateMenu(R.menu.done);

            fabImageButton.setClickable(false);

            Animation scaleAnim = AnimationUtils.loadAnimation(this, R.anim.scale_down);
            scaleAnim.setFillAfter(true);

            fabImageButton.startAnimation(scaleAnim);

            toolbarOpened = true;
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
                    myItem.toShop(true);
                    myItem.done(false);

                    DatabaseItem databaseItem = new DatabaseItem(getApplicationContext(), Static.currentList.getDatabase());

                    // update database
                    databaseItem.open();
                    databaseItem.updateByName(myItem.getName(), myItem);
                    databaseItem.close();

                    // get position of our item in the availableItem and remove it
                    int position = Static.currentList.itemAvailable.indexOf(myItem);
                    Static.currentList.itemAvailable.remove(position);

                    // copy the item to shopItem
                    Static.currentList.itemShop.add(myItem);

                    Static.currentList.sortShop();
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
        if (AdapterShop.selectedHolder.size() == 0)
        {
            toolbar.getMenu().clear();
            toolbar.inflateMenu(R.menu.barcode);
        }
        else if (AdapterShop.selectedHolder.size() == 1)
        {
            toolbar.getMenu().clear();
            toolbar.inflateMenu(R.menu.edit_remove);

            toolbar.showOverflowMenu();

        }
        else
        {
            toolbar.getMenu().clear();
            toolbar.inflateMenu(R.menu.remove);
        }

    }

    public void onBackPressed()
    {
        if (toolbarOpened)
        {
            barAction();
        }
        else if (AdapterShop.selectedHolder.size() > 0)
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