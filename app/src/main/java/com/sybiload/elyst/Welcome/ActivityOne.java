package com.sybiload.elyst.Welcome;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.*;

import com.sybiload.elyst.Misc;
import com.sybiload.elyst.R;

public class ActivityOne extends AppCompatActivity
{
    ImageButton fabImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcomeone);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = getWindow();
            window.setStatusBarColor(Color.BLACK);
        }

        fabImageButton = (ImageButton) findViewById(R.id.imageButtonFabWOne);
        fabImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), ActivityTwo.class);
                startActivity(intent);
                Misc.leftTransition(ActivityOne.this);
            }
        });
    }
}