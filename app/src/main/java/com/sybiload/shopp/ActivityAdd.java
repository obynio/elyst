package com.sybiload.shopp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.sybiload.shopp.Adapter.AddAdapter;
import com.sybiload.shopp.Adapter.ShopAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


public class ActivityAdd extends ActionBarActivity
{

    Misc misc = new Misc();
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;


    protected void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_additem);

        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        View content = root.getChildAt(0);
        LinearLayout toolbarContainer = (LinearLayout) View.inflate(this, R.layout.activity_toolbar, null);

        root.removeAllViews();
        toolbarContainer.addView(content);
        root.addView(toolbarContainer);

        Toolbar toolbar;
        toolbar = (Toolbar) toolbarContainer.findViewById(R.id.toolbar);
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
        AddAdapter addAdapter = new AddAdapter(getApplicationContext());
        recyclerView.setAdapter(addAdapter);
    }
    public void onBackPressed()
    {
        finish();
    }
}