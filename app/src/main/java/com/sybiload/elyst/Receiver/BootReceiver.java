package com.sybiload.elyst.Receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.sybiload.elyst.List;
import com.sybiload.elyst.Misc;
import com.sybiload.elyst.Static;

public class BootReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        // fill the list item
        Misc.populateList(context);

        for (List myList : Static.allList)
        {
            if (myList.getReminder() != null)
            {
                Intent alarmIntent = new Intent(context, AlarmReceiver.class);
                alarmIntent.setAction(myList.getIdDb());

                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                PendingIntent displayIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);

                alarmManager.set(AlarmManager.RTC_WAKEUP, myList.getReminder().getTimeInMillis(), displayIntent);
            }
        }
    }
}
