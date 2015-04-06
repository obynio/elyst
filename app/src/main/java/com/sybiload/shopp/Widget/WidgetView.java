package com.sybiload.shopp.Widget;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.view.View;
import android.widget.RemoteViews;

import com.sybiload.shopp.Database.Item.DatabaseItem;
import com.sybiload.shopp.Item;
import com.sybiload.shopp.Misc;
import com.sybiload.shopp.R;
import com.sybiload.shopp.Static;

public class WidgetView extends AppWidgetProvider
{
    public static final String IS_DONE = "com.sybiload.shopp.action.IS_DONE";
    public static final String REFRESH = "com.sybiload.shopp.action.REFRESH";
    private static int currently = 0;
    private static SharedPreferences mainPref;

    static RemoteViews updateTheWidget(Context context, boolean done)
    {
        mainPref = context.getSharedPreferences("main", 0);

        // get layout view
        RemoteViews views;
        if (mainPref.getString("listPreferenceWidgetTheme", "light").equals("light"))
            views = new RemoteViews(context.getPackageName(), R.layout.widget_white);
        else
            views = new RemoteViews(context.getPackageName(), R.layout.widget_dark);

        new Misc().populateList(context);

        if (Static.allList.isEmpty())
        {
            views.setTextViewText(R.id.textViewWidgetFirstLine, "No list available");
            views.setImageViewResource(R.id.imageViewWidget, R.drawable.ic_icon);

            views.setViewVisibility(R.id.textViewWidgetNumber, View.GONE);
            views.setViewVisibility(R.id.textViewWidgetSecondLine, View.GONE);
        }
        else
        {
            new Misc().populateItem(context, Static.allList.get(0));

            if (Static.widgetList.itemShop.isEmpty())
            {
                views.setTextViewText(R.id.textViewWidgetFirstLine, "Your current list is empty");
                views.setImageViewResource(R.id.imageViewWidget, R.drawable.ic_icon);

                views.setViewVisibility(R.id.textViewWidgetNumber, View.GONE);
                views.setViewVisibility(R.id.textViewWidgetSecondLine, View.GONE);
            }
            else
            {
                views.setViewVisibility(R.id.textViewWidgetNumber, View.VISIBLE);
                views.setViewVisibility(R.id.textViewWidgetSecondLine, View.VISIBLE);

                Item currentItem = Static.widgetList.itemShop.get(currently);

                if (done)
                {
                    if (currentItem.isDone())
                        currentItem.done(false);
                    else
                        currentItem.done(true);


                    DatabaseItem database = new DatabaseItem(context, Static.allList.get(0).getDatabase());

                    // update database
                    database.open();
                    database.updateByName(currentItem.getName(), currentItem);
                    database.close();

                    // make vibration
                    Vibrator vbr = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                    if (mainPref.getBoolean("checkBoxWidgetVibration", true) && vbr.hasVibrator())
                    {
                        vbr.vibrate(14);
                    }
                }

                currentItem = Static.widgetList.itemShop.get(currently);

                views.setTextViewText(R.id.textViewWidgetFirstLine, currentItem.getName());

                // count item remaining and total items in the list
                int total = Static.widgetList.itemShop.size();
                int countDone = 0;
                int countRemaining = 0;
                for (Item it : Static.widgetList.itemShop)
                {
                    if (it.isDone())
                        countDone++;
                    else
                        countRemaining++;
                }

                if (mainPref.getString("listPreferenceWidgetCount", "done").equals("done"))
                    views.setTextViewText(R.id.textViewWidgetNumber, countDone + "/" + total);
                else if (mainPref.getString("listPreferenceWidgetCount", "done").equals("remaining"))
                    views.setTextViewText(R.id.textViewWidgetNumber, countRemaining + "/" + total);
                else
                    views.setTextViewText(R.id.textViewWidgetNumber, (currently + 1) + "/" + total);

                if (currentItem.getDescription() != null && !currentItem.getDescription().equals(""))
                {
                    views.setTextViewText(R.id.textViewWidgetSecondLine, currentItem.getDescription());
                    views.setViewVisibility(R.id.textViewWidgetSecondLine, View.VISIBLE);
                }
                else
                {
                    views.setViewVisibility(R.id.textViewWidgetSecondLine, View.GONE);
                }

                if (!currentItem.isDone())
                {
                    if (currentItem.getColor() == 1)
                    {
                        views.setImageViewResource(R.id.imageViewWidget, R.drawable.ic_icon_green);
                    }
                    else if (currentItem.getColor() == 2)
                    {
                        views.setImageViewResource(R.id.imageViewWidget, R.drawable.ic_icon_orange);
                    }
                    else if (currentItem.getColor() == 3)
                    {
                        views.setImageViewResource(R.id.imageViewWidget, R.drawable.ic_icon_red);
                    }
                    else if (currentItem.getColor() == 4)
                    {
                        views.setImageViewResource(R.id.imageViewWidget, R.drawable.ic_icon_purple);
                    }
                    else if (currentItem.getColor() == 5)
                    {
                        views.setImageViewResource(R.id.imageViewWidget, R.drawable.ic_icon_blue);
                    }
                }
                else
                {
                    if (mainPref.getBoolean("checkBoxWidgetGreyIcon", false))
                    {
                        views.setImageViewResource(R.id.imageViewWidget, R.drawable.ic_nicon);
                    }
                    else
                    {
                        if (currentItem.getColor() == 1)
                        {
                            views.setImageViewResource(R.id.imageViewWidget, R.drawable.ic_nicon_green);
                        }
                        else if (currentItem.getColor() == 2)
                        {
                            views.setImageViewResource(R.id.imageViewWidget, R.drawable.ic_nicon_orange);
                        }
                        else if (currentItem.getColor() == 3)
                        {
                            views.setImageViewResource(R.id.imageViewWidget, R.drawable.ic_nicon_red);
                        }
                        else if (currentItem.getColor() == 4)
                        {
                            views.setImageViewResource(R.id.imageViewWidget, R.drawable.ic_nicon_purple);
                        }
                        else if (currentItem.getColor() == 5)
                        {
                            views.setImageViewResource(R.id.imageViewWidget, R.drawable.ic_nicon_blue);
                        }
                        else
                        {
                            views.setImageViewResource(R.id.imageViewWidget, R.drawable.ic_nicon);
                        }
                    }

                }
            }
        }
        return views;
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId)
    {
        RemoteViews views = updateTheWidget(context, false);

        // Prepare intent to launch on widget_white click
        Intent intentRefresh = new Intent(context, WidgetView.class);
        intentRefresh.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intentRefresh.setAction(REFRESH);

        // Launch intent on widget_white click
        PendingIntent pendingIntentRefresh = PendingIntent.getBroadcast(context, 0, intentRefresh, 0);
        views.setOnClickPendingIntent(R.id.llWidget, pendingIntentRefresh);

        // Prepare intent to launch on widget_white click
        Intent intentIsDone = new Intent(context, WidgetView.class);
        intentIsDone.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intentIsDone.setAction(IS_DONE);

        // Launch intent on widget_white click
        PendingIntent pendingIntentIsDone = PendingIntent.getBroadcast(context, 0, intentIsDone, 0);
        views.setOnClickPendingIntent(R.id.imageViewWidget, pendingIntentIsDone);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        final int N = appWidgetIds.length;

        // update each widgets
        for (int i = 0; i < N; i++)
        {
            int appWidgetId = appWidgetIds[i];
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        super.onReceive(context, intent);
        if (REFRESH.equals(intent.getAction()))
        {
            if (!Static.allList.isEmpty() && currently < Static.widgetList.itemShop.size() - 1)
            {
                currently++;
            }
            else
            {
                currently = 0;
            }

            RemoteViews views = updateTheWidget(context, false);
            ComponentName thisWidget = new ComponentName(context, WidgetView.class);
            AppWidgetManager.getInstance(context).updateAppWidget(thisWidget, views);
        }
        else if (IS_DONE.equals(intent.getAction()))
        {
            RemoteViews views = updateTheWidget(context, true);
            ComponentName thisWidget = new ComponentName(context, WidgetView.class);
            AppWidgetManager.getInstance(context).updateAppWidget(thisWidget, views);
        }
    }
}