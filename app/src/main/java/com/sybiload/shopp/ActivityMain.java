package com.sybiload.shopp;


import android.content.Context;
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

import com.sybiload.shopp.Adapter.AdapterListView;
import com.sybiload.shopp.Adapter.ListViewItem;

import java.util.ArrayList;


public class ActivityMain extends ActionBarActivity
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

        drawerItems = getResources().getStringArray(R.array.navdrawer_items);
        drawerFragments = getResources().getStringArray(R.array.navdrawer_views);

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

        models.add(new ListViewItem(drawerItems[0], getResources().getDrawable(R.mipmap.ic_shop)));
        models.add(new ListViewItem(drawerItems[1], getResources().getDrawable(R.mipmap.ic_schedule)));
        models.add(new ListViewItem(drawerItems[2], getResources().getDrawable(R.mipmap.ic_pin)));
        models.add(new ListViewItem(drawerItems[3], getResources().getDrawable(R.mipmap.ic_settings)));

        navList.setAdapter(new AdapterListView(this, models));

        navList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int pos, long id)
            {

                FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
                tx.replace(R.id.main, Fragment.instantiate(ActivityMain.this, drawerFragments[pos]));
                tx.commit();

                drawerLayout.closeDrawer(navList);
            }
        });

        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.main, Fragment.instantiate(ActivityMain.this, "com.sybiload.shopp.FragmentList"));
        tx.commit();
    }
}