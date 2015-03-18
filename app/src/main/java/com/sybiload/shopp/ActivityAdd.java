package com.sybiload.shopp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.sybiload.shopp.Adapter.AdapterAdd;


public class ActivityAdd extends ActionBarActivity
{
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

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
                finish();
            }
        });

        // change searchView text color
        SearchView searchView = (SearchView)toolbar.findViewById(R.id.searchViewAdd);
        SearchView.SearchAutoComplete theTextArea = (SearchView.SearchAutoComplete)searchView.findViewById(R.id.search_src_text);
        theTextArea.setTextColor(Color.WHITE);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);


        // adding all items available
        AdapterAdd adapterAdd = new AdapterAdd(getApplicationContext(), newList);
        recyclerView.setAdapter(adapterAdd);
    }
    public void onBackPressed()
    {
        finish();
    }
}