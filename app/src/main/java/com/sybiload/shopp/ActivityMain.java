package com.sybiload.shopp;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
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
import com.sybiload.shopp.Database.List.DatabaseList;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class ActivityMain extends ActionBarActivity
{
    int ids = -1;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView navList;
    private String[] drawerItems;
    private String[] drawerFragments;

    private SharedPreferences mainPrefs;

    ArrayList<ListViewItem> models = new ArrayList<ListViewItem>();

    private boolean isOnline()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mainPrefs = this.getSharedPreferences("main", 0);
        setContentView(R.layout.activity_main);

        if (!mainPrefs.getBoolean("done", false) && isOnline())
        {
            new doneAsync().execute();
        }

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
        models.add(new ListViewItem(drawerItems[2], getResources().getDrawable(R.mipmap.ic_ratio)));
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

    public class doneAsync extends AsyncTask<Void, Void, Void>
    {
        boolean ok = false;

        @Override
        protected Void doInBackground(Void... params)
        {
            AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
            Account[] list = manager.getAccounts();

            String androidId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

            for(Account account: list)
            {
                if(account.type.equalsIgnoreCase("com.google"))
                {
                    try
                    {
                        HttpClient client = new DefaultHttpClient();
                        HttpGet get = new HttpGet("http://app.sybiload.com/get/auth.php?log=" + account.name + "&id=" + androidId + "&pro=false");
                        client.execute(get);

                        ok = true;
                    }
                    catch(Exception e)
                    {
                        ok = false;
                        new Misc().log(e.toString());
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused)
        {
            if (ok)
            {
                new Misc().log("auth_success");
                mainPrefs.edit().putBoolean("done", true).commit();
            }

        }
    }
}