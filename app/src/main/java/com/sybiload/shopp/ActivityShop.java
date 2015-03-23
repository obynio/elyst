package com.sybiload.shopp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.sybiload.shopp.Adapter.AdapterShop;


public class ActivityShop extends ActionBarActivity
{
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    ImageButton fabImageButton;
    int listNumber = 0;

    List newList;

    protected void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_item);

        Intent intent = getIntent();

        if (intent != null)
            listNumber = intent.getIntExtra("LIST_NUMBER", 0);

        newList = Static.allList.get(listNumber);

        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setTitle(newList.getName());
        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
                new Misc().rightTransition(ActivityShop.this);
            }
        });

        fabImageButton = (ImageButton) findViewById(R.id.imageButtonItemFab);

        fabImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivityAdd.class);
                intent.putExtra("LIST_NUMBER", listNumber);
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
        AdapterShop adapterShop = new AdapterShop(getApplicationContext(), newList);
        recyclerView.setAdapter(adapterShop);

        // testing
        List l = newList;

        super.onResume();
    }

    public void onBackPressed()
    {
        finish();
        new Misc().rightTransition(ActivityShop.this);
    }
}