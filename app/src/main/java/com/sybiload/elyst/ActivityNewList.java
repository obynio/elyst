package com.sybiload.elyst;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;

import com.sybiload.elyst.Adapter.AdapterEditText;
import com.sybiload.elyst.Database.List.DatabaseList;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;


public class ActivityNewList extends ActionBarActivity
{
    public ImageButton fabImageButton;
    public AdapterEditText editTextNewListName;
    public EditText editTextNewListDescription;

    private List selectedList;

    protected void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_newlist);

        editTextNewListName = (AdapterEditText)findViewById(R.id.editTextNewListName);
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

        Intent intent = getIntent();

        if (intent != null)
        {
            int index = intent.getIntExtra("LIST_NB", -1);

            if (index >= 0)
            {
                selectedList = Static.allList.get(index);

                editTextNewListName.setText(selectedList.getName());
                editTextNewListDescription.setText(selectedList.getDescription());
            }
        }

        editTextNewListName.addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable s)
            {
                boolean error = false;
                String text = editTextNewListName.getText().toString();

                if (text.toLowerCase().equals("main"))
                    error = true;

                // hot job dude..
                if (error)
                {
                    editTextNewListName.setError("");

                    fabImageButton.setClickable(false);
                    fabImageButton.setColorFilter(Color.parseColor("#90CAF9"));
                }
                else if (text.isEmpty())
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

        fabImageButton.setVisibility(View.GONE);
        fabImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedList == null)
                    new createListAsync().execute();
                else
                {
                    new updateListAsync().execute();
                }
            }
        });

        // disable the button to avoid null input
        if (selectedList == null)
        {
            fabImageButton.setClickable(false);
            fabImageButton.setColorFilter(Color.parseColor("#90CAF9"));
        }

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

    public void onBackPressed()
    {
        finish();
    }

    public class createListAsync extends AsyncTask<Void, Void, Void>
    {
        protected ProgressDialog Dialog;

        @Override
        protected void onPreExecute()
        {
            Dialog = new ProgressDialog(ActivityNewList.this);
            Dialog.setMessage("Just a moment..");
            Dialog.setCancelable(false);
            Dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            DatabaseList databaseList = new DatabaseList(getApplicationContext());
            databaseList.open();

            List myList = new List(editTextNewListName.getText().toString(), editTextNewListDescription.getText().toString(), new Misc().generateSeed() + ".db");

            new Misc().addList(getApplicationContext(), myList);

            databaseList.close();



            return null;
        }

        @Override
        protected void onPostExecute(Void unused)
        {
            Dialog.dismiss();

            finish();
        }
    }


    public class updateListAsync extends AsyncTask<Void, Void, Void>
    {
        protected ProgressDialog Dialog;

        @Override
        protected void onPreExecute()
        {
            Dialog = new ProgressDialog(ActivityNewList.this);
            Dialog.setMessage("Just a moment..");
            Dialog.setCancelable(false);
            Dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            DatabaseList databaseList = new DatabaseList(getApplicationContext());
            databaseList.open();

            String name = selectedList.getName();

            selectedList.setName(editTextNewListName.getText().toString());
            selectedList.setDescription(editTextNewListDescription.getText().toString());

            databaseList.updateByName(name, selectedList);

            databaseList.close();

            return null;
        }

        @Override
        protected void onPostExecute(Void unused)
        {
            Dialog.dismiss();

            finish();
        }
    }
}