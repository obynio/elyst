package com.sybiload.shopp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.sybiload.shopp.Database.List.DatabaseList;


public class ActivityNewList extends ActionBarActivity
{
    public ImageButton fabImageButton;
    public EditText editTextNewListName;
    public EditText editTextNewListDescription;

    protected void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_newlist);

        editTextNewListName = (EditText)findViewById(R.id.editTextNewListName);
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


        fabImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseList databaseList = new DatabaseList(getApplicationContext());
                databaseList.open();

                List myList = new List(editTextNewListName.getText().toString(), editTextNewListDescription.getText().toString(), editTextNewListName.getText().toString().toLowerCase().replaceAll(" ", "_") + ".db");

                new Misc().testList(getApplicationContext());

                finish();
            }
        });


    }

    public void onBackPressed()
    {
        finish();
    }
}