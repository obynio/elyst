package com.sybiload.elyst.Receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.widget.Toast;

import com.sybiload.elyst.ActivityMain;
import com.sybiload.elyst.ActivityShop;
import com.sybiload.elyst.List;
import com.sybiload.elyst.Misc;
import com.sybiload.elyst.R;
import com.sybiload.elyst.Static;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        // fill the lists
        Misc.populateList(context);

        List myList = null;

        for (List thisList : Static.allList)
        {
            if (thisList.getIdDb().equals(intent.getAction()))
            {
                myList = thisList;
                break;
            }
        }

        if (myList != null)
        {
            Static.currentList = myList;
            Misc.populateItem(context, Static.currentList);

            Intent mainIntent = new Intent(context, ActivityShop.class);
            PendingIntent pIntent = PendingIntent.getActivity(context, 0, mainIntent, 0);

            Notification n;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                n = new Notification.Builder(context)
                        .setContentTitle(myList.getName())
                        .setContentText("Today at " + new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime()))
                        .setSmallIcon(R.mipmap.ic_shopping)
                        .setContentIntent(pIntent)
                        .setAutoCancel(true)
                        .setColor(context.getResources().getColor(R.color.colorPrimary))
                        .setTicker("Reminder for " + myList.getName())
                        .setVibrate(new long[] { 600 })
                        .setPriority(Notification.PRIORITY_DEFAULT)
                        .setLights(Color.WHITE, 500, 1000).build();
            }
            else
            {
                n = new Notification.Builder(context)
                        .setContentTitle(myList.getName())
                        .setContentText("Today at " + new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime()))
                        .setSmallIcon(R.mipmap.ic_shopping)
                        .setContentIntent(pIntent)
                        .setAutoCancel(true)
                        .setTicker("Reminder for " + myList.getName())
                        .setVibrate(new long[] { 600 })
                        .setPriority(Notification.PRIORITY_DEFAULT)
                        .setLights(Color.WHITE, 500, 1000).build();
            }


            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0, n);
        }
    }
}