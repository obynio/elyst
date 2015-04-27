package com.sybiload.elyst;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.sybiload.elyst.Adapter.AdapterList;
import com.sybiload.elyst.Adapter.AdapterNewList;
import com.sybiload.elyst.Database.List.DatabaseList;

import java.util.ArrayList;

public class ActivityNewList extends AppCompatActivity
{
    private ImageButton fabImageButton;
    private EditText editTextNewListName;
    private EditText editTextNewListDescription;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AdapterNewList currAdapter;

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

        if (Static.currentList != null)
        {
            editTextNewListName.setText(Static.currentList.getName());
            editTextNewListDescription.setText(Static.currentList.getDescription());
        }

        editTextNewListName.addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable s)
            {
                // hot job dude..
                if (s.toString().isEmpty())
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
                if (Static.currentList == null)
                    new createListAsync().execute();
                else
                {
                    new updateListAsync().execute();
                }
            }
        });

        // set up recyclerView parameters
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewNewList);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);


        // disable the button to avoid null input
        if (Static.currentList == null)
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

    @Override
    public void onResume()
    {
        // add all items to shop
        currAdapter = new AdapterNewList(this);
        recyclerView.setAdapter(currAdapter);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        super.onResume();
    }

    public void onBackPressed()
    {
        finish();
    }

    public class createListAsync extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params)
        {
            Static.currentList = new List(new Misc().generateSeed(), editTextNewListName.getText().toString(), editTextNewListDescription.getText().toString(), currAdapter.selectedIndex);
            new Misc().createList(getApplicationContext(), Static.currentList);

            // search and add data in the fragmentlist recyclerview
            for (int i = 0; i < Static.allList.size(); i++)
            {
                if (Static.allList.get(i).getIdDb().equals(Static.currentList.getIdDb()))
                {
                    FragmentList.currAdapter.notifyItemInserted(i);
                    break;
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused)
        {
            finish();
        }
    }


    public class updateListAsync extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params)
        {
            Static.currentList.setName(editTextNewListName.getText().toString());
            Static.currentList.setDescription(editTextNewListDescription.getText().toString());
            Static.currentList.setBackground(currAdapter.selectedIndex);

            FragmentList.currAdapter.update(Static.currentList);

            return null;
        }

        @Override
        protected void onPostExecute(Void unused)
        {
            finish();
        }
    }
}