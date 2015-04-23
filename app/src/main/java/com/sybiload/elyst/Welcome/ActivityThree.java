package com.sybiload.elyst.Welcome;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.sybiload.elyst.ActivityMain;
import com.sybiload.elyst.Database.List.DatabaseList;
import com.sybiload.elyst.Misc;
import com.sybiload.elyst.R;

public class ActivityThree extends Activity
{
    SharedPreferences mainPref;
    ImageButton fabImageButton;

    RadioGroup radioGroup;

    CheckBox checkBoxBarcode;
    CheckBox checkBoxVibration;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mainPref = this.getSharedPreferences("main", 0);
        setContentView(R.layout.activity_welcomethree);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = getWindow();
            window.setStatusBarColor(Color.BLACK);
        }

        fabImageButton = (ImageButton) findViewById(R.id.imageButtonFabWThree);
        checkBoxBarcode = (CheckBox) findViewById(R.id.checkBoxWThreeBarcode);
        checkBoxVibration = (CheckBox) findViewById(R.id.checkBoxWThreeVibration);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroupSort);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                if (checkedId == R.id.radioButtonAlphabetical)
                {
                    mainPref.edit().putString("listPreferenceUiAddSort", "alphabetical").commit();
                    mainPref.edit().putString("listPreferenceUiShopSort", "alphabetical").commit();
                }
                else
                {
                    mainPref.edit().putString("listPreferenceUiAddSort", "category").commit();
                    mainPref.edit().putString("listPreferenceUiShopSort", "category").commit();
                }
            }
        });

        fabImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new newItemAsync().execute();
            }
        });

        checkBoxBarcode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                mainPref.edit().putBoolean("checkBoxSystemBarcode", isChecked).commit();
            }
        });

        checkBoxVibration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                mainPref.edit().putBoolean("checkBoxSystemVibration", isChecked).commit();
                mainPref.edit().putBoolean("checkBoxWidgetVibration", isChecked).commit();
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        finish();
        new Misc().rightTransition(ActivityThree.this);
    }

    public class newItemAsync extends AsyncTask<Void, Void, Void>
    {
        protected ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(ActivityThree.this);
            dialog.setMessage("Just a moment..");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            new Misc().populateDefaultItem(getApplicationContext());
            return null;
        }

        @Override
        protected void onPostExecute(Void unused)
        {
            mainPref.edit().putBoolean("firstLaunch", false).commit();
            dialog.dismiss();

            Intent intent = new Intent(getApplication(), ActivityMain.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }


}