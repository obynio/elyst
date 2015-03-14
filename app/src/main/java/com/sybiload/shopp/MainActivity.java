package com.sybiload.shopp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sybiload.shopp.Adapter.ListViewAdapter;
import com.sybiload.shopp.Adapter.ListViewItem;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity
{
    int ids = -1;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView navList;
    private String[] drawerItems;
    private String[] drawerFragments;

    ArrayList<ListViewItem> models = new ArrayList<ListViewItem>();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // populate items
        new Misc().populateItems(getApplicationContext());

        drawerItems = getResources().getStringArray(R.array.navdrawer_items);
        drawerFragments = getResources().getStringArray(R.array.navdrawer_fragments);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        if (toolbar != null)
        {
            setSupportActionBar(toolbar);
        }

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.txt_open, R.string.txt_close)
        {

            @Override
            public void onDrawerClosed(View drawerView)
            {
                super.onDrawerClosed(drawerView);

            }

            @Override
            public void onDrawerOpened(View drawerView)
            {
                super.onDrawerOpened(drawerView);
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        navList = (ListView) findViewById(R.id.drawer);

        models.add(new ListViewItem("Carottes", "carottes@gmail.com"));
        for (String str : drawerItems)
        {
            models.add(new ListViewItem(str));
        }

        navList.setAdapter(new ListViewAdapter(this, models));

        navList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int pos, long id)
            {

                FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
                tx.replace(R.id.main, Fragment.instantiate(MainActivity.this, drawerFragments[pos]));
                tx.commit();

                drawerLayout.closeDrawer(navList);
            }
        });

        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.main, Fragment.instantiate(MainActivity.this, "com.sybiload.shopp.FragmentList"));
        tx.commit();
    }

}