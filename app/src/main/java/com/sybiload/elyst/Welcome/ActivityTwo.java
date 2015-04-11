package com.sybiload.elyst.Welcome;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import com.sybiload.elyst.Misc;
import com.sybiload.elyst.R;

public class ActivityTwo extends Activity
{
    ImageButton fabImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcometwo);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = getWindow();
            window.setStatusBarColor(Color.BLACK);
        }

        fabImageButton = (ImageButton) findViewById(R.id.imageButtonFabWTwo);
        fabImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), ActivityThree.class);
                startActivity(intent);
                new Misc().leftTransition(ActivityTwo.this);
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        finish();
        new Misc().rightTransition(ActivityTwo.this);
    }
}
