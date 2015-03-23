package com.sybiload.shopp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.sybiload.shopp.Adapter.EditTextAdapter;
import com.sybiload.shopp.Database.List.DatabaseList;


public class ActivityNewList extends ActionBarActivity
{
    public ImageButton fabImageButton;
    public EditTextAdapter editTextNewListName;
    public EditText editTextNewListDescription;

    protected void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_newlist);

        editTextNewListName = (EditTextAdapter)findViewById(R.id.editTextNewListName);
        editTextNewListDescription = (EditText)findViewById(R.id.editTextNewListDescription);
        fabImageButton = (ImageButton) findViewById(R.id.imageButtonNewListFab);

        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });



        editTextNewListName.addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable s)
            {
                boolean error = false;
                String text = editTextNewListName.getText().toString();

                if (text.toLowerCase().equals("main"))
                    error = true;
                else
                {
                    for (List l : Static.allList)
                    {
                        if (text.toLowerCase().equals(l.getName().toLowerCase()))
                        {
                            error = true;
                            break;
                        }
                    }
                }

                if (error)
                {
                    editTextNewListName.setError("");

                    fabImageButton.setClickable(false);
                    fabImageButton.setColorFilter(Color.parseColor("#90CAF9"));
                }
                else if (text.length() == 0)
                {
                    fabImageButton.setClickable(false);
                    fabImageButton.setColorFilter(Color.parseColor("#90CAF9"));
                }
                else
                {
                    editTextNewListName.setError(null);
                    fabImageButton.setClickable(true);
                    fabImageButton.setColorFilter(Color.TRANSPARENT);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            public void onTextChanged(CharSequence s, int start, int before, int count) { }
        });

        fabImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseList databaseList = new DatabaseList(getApplicationContext());
                databaseList.open();

                List myList = new List(editTextNewListName.getText().toString(), editTextNewListDescription.getText().toString(), editTextNewListName.getText().toString().toLowerCase().replaceAll(" ", "_") + ".db");

                new Misc().addList(getApplicationContext(), myList);


                finish();
            }
        });

        // disable the button to avoid null input
        fabImageButton.setClickable(false);
        fabImageButton.setColorFilter(Color.parseColor("#90CAF9"));


    }

    public void onBackPressed()
    {
        finish();
    }
}