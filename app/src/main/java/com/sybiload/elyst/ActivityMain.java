package com.sybiload.elyst;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sybiload.elyst.Adapter.AdapterList;
import com.sybiload.elyst.Adapter.AdapterListView;
import com.sybiload.elyst.Adapter.AdapterShop;
import com.sybiload.elyst.Adapter.ListViewItem;
import com.sybiload.elyst.Database.List.DatabaseList;
import com.sybiload.elyst.Pref.ActivityAppPref;
import com.sybiload.elyst.Util.IabHelper;
import com.sybiload.elyst.Util.IabResult;
import com.sybiload.elyst.Util.Inventory;
import com.sybiload.elyst.Util.Purchase;
import com.sybiload.elyst.Welcome.ActivityOne;

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
    public static Toolbar toolbar;
    public static ActionBarDrawerToggle drawerToggle;
    public static DrawerLayout drawerLayout;
    private ListView navList;
    private String[] drawerItems;
    private String[] drawerFragments;

    private SharedPreferences mainPref;

    ArrayList<ListViewItem> models = new ArrayList<ListViewItem>();

    private Boolean isPremium = false;
    IabHelper helper;

    IabHelper.QueryInventoryFinishedListener receivedInventoryListener = new IabHelper.QueryInventoryFinishedListener()
    {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory)
        {

            if (result.isFailure())
            {
                new Misc().log("In App query inventory failed");
            }
            else
            {
                isPremium = inventory.hasPurchase(IabHelper.getSku());
                new Misc().log("In App query inventory successfull");
                new Misc().log("In App user " + (isPremium ? "has full version" : "has free version"));

                mainPref.edit().putInt("deviceLoop", isPremium ? 8238 : 2895).commit();
                invalidateOptionsMenu();
            }
        }
    };

    IabHelper.OnIabPurchaseFinishedListener purchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener()
    {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase)
        {
            if (result.isFailure())
            {
                new Misc().log("In App payment failed");
            }
            else if (purchase.getSku().equals(IabHelper.getSku()))
            {
                new Misc().log("In App payment successfull");
                helper.queryInventoryAsync(receivedInventoryListener);
            }
        }
    };

    private boolean isOnline()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private boolean checkPrefs()
    {
        if (mainPref.getInt("deviceLoop", 2895) == 8238)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void buy()
    {
        helper.launchPurchaseFlow(ActivityMain.this, IabHelper.getSku(), 10001, purchaseFinishedListener, "utask.fullversion");
    }

    void rateDialog()
    {
        int appLaunch = mainPref.getInt("appLaunch", 0);
        mainPref.edit().putInt("appLaunch", appLaunch += 1).commit();

        if (!mainPref.getBoolean("appRated", false))
        {
            if (appLaunch % 100 == 0)
            {
                AlertDialog.Builder alert = new AlertDialog.Builder(ActivityMain.this);

                alert.setMessage("It would nice to rate this app as a reward for my hard work");

                alert.setPositiveButton("Rate", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("market://details?id=com.sybiload.elyst"));
                        startActivity(browserIntent);

                        mainPref.edit().putBoolean("appRated", true).commit();
                    }
                });

                alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        dialog.cancel();
                    }
                });

                alert.show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        toolbar.inflateMenu(R.menu.buy);
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mainPref = this.getSharedPreferences("main", 0);
        setContentView(R.layout.activity_main);

        helper = new IabHelper(this, IabHelper.getPublicKey());

        helper.startSetup(new IabHelper.OnIabSetupFinishedListener()
        {
            public void onIabSetupFinished(IabResult result)
            {

                if (!result.isSuccess())
                {
                    new Misc().log("In App setup failed (" + result + ")");
                }
                else
                {
                    new Misc().log("In App setup sucessfull");

                    if (isOnline())
                    {
                        helper.queryInventoryAsync(receivedInventoryListener);
                    }
                    else
                    {
                        isPremium = checkPrefs();
                        invalidateOptionsMenu();
                    }
                }
            }
        });

        // send data
        if (!mainPref.getBoolean("done", false) && isOnline())
        {
            new doneAsync().execute();
        }

        // if it's the first launch, start the intro
        if (mainPref.getBoolean("firstLaunch", true))
        {
            Intent intent = new Intent(getApplication(), ActivityOne.class);
            startActivity(intent);
            finish();
        }

        // show rate dialog
        rateDialog();

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

        drawerToggle.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        navList = (ListView) findViewById(R.id.drawer);

        models.add(new ListViewItem(drawerItems[0], getResources().getDrawable(R.mipmap.ic_shop)));
        models.add(new ListViewItem(drawerItems[1], getResources().getDrawable(R.mipmap.ic_settings)));

        navList.setAdapter(new AdapterListView(this, models));

        navList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int pos, long id)
            {

                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if (pos == 1)
                        {
                            Intent intent = new Intent(getApplicationContext(), ActivityAppPref.class);
                            startActivity(intent);
                            new Misc().leftTransition(ActivityMain.this);
                        }
                        else
                        {
                            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
                            tx.replace(R.id.main, Fragment.instantiate(ActivityMain.this, drawerFragments[pos]));
                            tx.commit();
                        }
                    }
                }, 240);

                drawerLayout.closeDrawer(navList);
            }
        });



        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.main, Fragment.instantiate(ActivityMain.this, "com.sybiload.elyst.FragmentList"));
        tx.commit();

    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (helper != null)
        {
            helper.dispose();
        }
        helper = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (!helper.handleActivityResult(requestCode, resultCode, data))
        {
            super.onActivityResult(requestCode, resultCode, data);
        }
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
                        break;
                    }
                    catch(Exception e)
                    {
                        ok = false;
                        break;
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
                mainPref.edit().putBoolean("done", true).commit();
            }

        }
    }
}