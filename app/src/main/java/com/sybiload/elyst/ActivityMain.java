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
import android.content.pm.ResolveInfo;
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
import android.support.v7.app.AppCompatActivity;
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

public class ActivityMain extends AppCompatActivity
{
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private ListView navList;
    private String[] drawerItems;
    private String[] drawerFragments;

    private SharedPreferences mainPref;

    private ArrayList<ListViewItem> models = new ArrayList<ListViewItem>();

    private boolean isPremium = false;
    private boolean manualCheck = false;
    private IabHelper helper;

    IabHelper.QueryInventoryFinishedListener receivedInventoryListener = new IabHelper.QueryInventoryFinishedListener()
    {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory)
        {

            if (result.isFailure())
            {
                Misc.log("In App query inventory failed");
            }
            else
            {
                isPremium = inventory.hasPurchase(IabHelper.getSku());
                Misc.log("In App query inventory successfull");
                Misc.log("In App user " + (isPremium ? "has full version" : "has free version"));

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
                Misc.log("In App payment failed");
            }
            else if (purchase.getSku().equals(IabHelper.getSku()))
            {
                Misc.log("In App payment successfull");
                helper.queryInventoryAsync(receivedInventoryListener);
            }
        }
    };

    private String getAccount()
    {
        AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
        Account[] users = manager.getAccountsByType("com.google");

        return (users.length > 0) ? users[0].name : null;
    }

    private boolean isDeveloper()
    {
        // debug purpose TO REMOVE! TO REMOVE! TO REMOVE! TO REMOVE! TO REMOVE! TO REMOVE! TO REMOVE! TO REMOVE! TO REMOVE! TO REMOVE! TO REMOVE! TO REMOVE!
        // TO REMOVE! TO REMOVE! TO REMOVE! TO REMOVE! TO REMOVE! TO REMOVE! TO REMOVE! TO REMOVE! TO REMOVE! TO REMOVE! TO REMOVE! TO REMOVE! TO REMOVE!
        // TO REMOVE! TO REMOVE! TO REMOVE! TO REMOVE! TO REMOVE! TO REMOVE! TO REMOVE! TO REMOVE! TO REMOVE! TO REMOVE! TO REMOVE! TO REMOVE! TO REMOVE!
        // TO REMOVE! TO REMOVE! TO REMOVE! TO REMOVE! TO REMOVE! TO REMOVE! TO REMOVE! TO REMOVE! TO REMOVE! TO REMOVE! TO REMOVE! TO REMOVE! TO REMOVE!
        // TO REMOVE! TO REMOVE! TO REMOVE! TO REMOVE! TO REMOVE! TO REMOVE! TO REMOVE! TO REMOVE! TO REMOVE! TO REMOVE! TO REMOVE! TO REMOVE! TO REMOVE!

        // getAccount().equals("sybiload@gmail.com")
        if (false)
        {
            isPremium = true;
            mainPref.edit().putInt("deviceLoop", isPremium ? 8238 : 2895).commit();

            Misc.log("Bypass InApp query, user is developer");
            Misc.log("In App user " + (isPremium ? "has full version" : "has free version"));


            return true;
        }
        else
            return false;
    }

    private boolean isOnline()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public boolean isPro()
    {
        return mainPref.getInt("deviceLoop", 2895) == 8238;
    }

    public void buy()
    {
        manualCheck = true;
        new checkIntegrity().execute();
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
        if (!isPro())
            toolbar.inflateMenu(R.menu.buy);
        else
            toolbar.getMenu().clear();

        return true;
    }

    @Override
    public void onResume()
    {
        invalidateOptionsMenu();

        super.onResume();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mainPref = this.getSharedPreferences("main", 0);
        setContentView(R.layout.activity_main);

        // check for hacking apps
        new checkIntegrity().execute();

        helper = new IabHelper(this, IabHelper.getPublicKey());

        helper.startSetup(new IabHelper.OnIabSetupFinishedListener()
        {
            public void onIabSetupFinished(IabResult result)
            {

                if (!result.isSuccess())
                {
                    Misc.log("In App setup failed (" + result + ")");
                }
                else
                {
                    Misc.log("In App setup successful");

                    if (!isDeveloper() && isOnline())
                        helper.queryInventoryAsync(receivedInventoryListener);
                    else
                    {
                        isPremium = isPro();
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

        models.add(new ListViewItem(null, null));
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
                            Misc.leftTransition(ActivityMain.this);
                        }
                        else
                        {
                            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
                            tx.replace(R.id.main, Fragment.instantiate(ActivityMain.this, drawerFragments[pos - 1]));
                            tx.commit();
                        }
                    }
                }, 240);

                drawerLayout.closeDrawer(navList);
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {

                switch (item.getItemId())
                {
                    case R.id.action_buy:
                        buy();
                        return true;

                    default:
                        return false;
                }
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
        try {
            if (helper != null)
            {
                helper.dispose();
            }
            helper = null;
        }
        catch(Exception e)
        {
            // avoid a bug with billing library which cause a crash if google play has never been launched
            Misc.log("InApp billing onDestroy exception raised");
        }

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
            String androidId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

            try
            {
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet("http://app.sybiload.com/get/auth.php?log=" + getAccount() + "&id=" + androidId + "&pro=" + Boolean.toString(isPremium));
                client.execute(get);

                ok = true;
            }
            catch(Exception e)
            {
                ok = false;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused)
        {
            if (ok)
            {
                Misc.log("auth_success");
                mainPref.edit().putBoolean("done", true).commit();
            }
        }
    }

    // check if there is no hacking apps installed
    public class checkIntegrity extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params)
        {
            Intent main = new Intent(Intent.ACTION_MAIN, null);
            main.addCategory(Intent.CATEGORY_LAUNCHER);

            boolean hackPresent = false;

            try
            {
                java.util.List<ResolveInfo> pkg = getApplicationContext().getPackageManager().queryIntentActivities(main, 0);

                for (ResolveInfo info : pkg)
                {
                    if (info.activityInfo.packageName.equals("com.dimonvideo.luckypatcher") || info.activityInfo.packageName.equals("com.chelpus.lackypatch") || info.activityInfo.packageName.equals("com.forpda.lp") || info.activityInfo.packageName.equals("cc.madkite.freedom"))
                    {
                        hackPresent = true;
                        break;
                    }
                }
            }
            catch (Exception e)
            {
                Misc.log(e.toString());
            }

            // if there is an hacking app, make the activity crash
            if (hackPresent)
            {
                Integer i = null;
                i.byteValue();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused)
        {
            if (manualCheck)
            {
                manualCheck = false;
                helper.launchPurchaseFlow(ActivityMain.this, IabHelper.getSku(), 10001, purchaseFinishedListener, "utask.fullversion");
            }
        }
    }
}