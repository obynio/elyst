package com.sybiload.shopp.Widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.sybiload.shopp.Misc;
import com.sybiload.shopp.R;
import com.sybiload.shopp.Static;

public class WidgetConfigure extends Activity
{
    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // If the user closes window, don't create the widget
        setResult(RESULT_CANCELED);

        // Find widget id from launching intent
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null)
        {
            appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If they gave us an intent without the widget id, just bail.
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID)
        {
            new Misc().log("No appWidgetId provided");
            finish();
        }

        configureWidget(getApplicationContext());

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_OK, resultValue);

        finish();
    }

    public void configureWidget(Context context)
    {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        WidgetView.updateAppWidget(context, appWidgetManager, appWidgetId);
    }
}
