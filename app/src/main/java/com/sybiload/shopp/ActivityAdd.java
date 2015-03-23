package com.sybiload.shopp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;

import com.sybiload.shopp.Adapter.AdapterAdd;
import com.sybiload.shopp.Database.Item.DatabaseItem;
import com.sybiload.shopp.Database.List.DatabaseList;

import java.util.ArrayList;


public class ActivityAdd extends ActionBarActivity
{
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private SearchView searchView;


    int listNumber = 0;

    List newList;


    protected void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_additem);

        Intent intent = getIntent();

        if (intent != null)
            listNumber = intent.getIntExtra("LIST_NUMBER", 0);

        newList = Static.allList.get(listNumber);

        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setTitle("Add items");
        //toolbar.setSubtitle("Let's dance !");
        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (searchView.isIconified())
                    finish();
                else
                {
                    searchView.setQuery("", false);
                    searchView.clearFocus();
                    searchView.setIconified(true);
                }
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler);
        searchView = (SearchView) toolbar.findViewById(R.id.searchViewAdd);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        // adding all items available
        AdapterAdd adapterAdd = new AdapterAdd(getApplicationContext(), newList);
        recyclerView.setAdapter(adapterAdd);


        // change searchView text color
        SearchView.SearchAutoComplete searchViewText = (SearchView.SearchAutoComplete) searchView.findViewById(R.id.search_src_text);
        searchViewText.setTextColor(Color.WHITE);
        searchViewText.setHint("Search an item");
        searchViewText.setHintTextColor(Color.parseColor("#C5CAE9"));


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
                ArrayList<Item> arrayItem = new ArrayList<Item>();

                DatabaseItem database = new DatabaseItem(getApplicationContext(), newList.getDatabase());

                database.open();
                ArrayList<Item> ite = database.searchItem(s.toLowerCase());
                database.close();

                newList.setItem(ite);



                AdapterAdd adapterAdd = new AdapterAdd(getApplicationContext(), newList);
                recyclerView.setAdapter(adapterAdd);

                return false;
            }
        });
    }
    public void onBackPressed()
    {

        if (searchView.isIconified())
            finish();
        else
        {
            searchView.setQuery("", false);
            searchView.clearFocus();
            searchView.setIconified(true);
        }

    }
}