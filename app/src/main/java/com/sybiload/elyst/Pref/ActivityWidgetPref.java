package com.sybiload.elyst.Pref;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.sybiload.elyst.Misc;
import com.sybiload.elyst.R;

public class ActivityWidgetPref extends PreferenceActivity
{
    CheckBoxPreference prefVibration;

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesName("main");
        addPreferencesFromResource(R.xml.widget_pref);

        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        View content = root.getChildAt(0);
        LinearLayout toolbarContainer = (LinearLayout) View.inflate(this, R.layout.toolbar, null);

        root.removeAllViews();
        toolbarContainer.addView(content);
        root.addView(toolbarContainer);

        Toolbar toolbar;
        toolbar = (Toolbar) toolbarContainer.findViewById(R.id.toolbar);
        toolbar.setTitle(getTitle());
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
                Misc.rightTransition(ActivityWidgetPref.this);
            }
        });

        prefVibration = (CheckBoxPreference) findPreference("checkBoxWidgetVibration");

        Vibrator vbr = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        if (!vbr.hasVibrator())
        {
            prefVibration.setChecked(false);
            prefVibration.setEnabled(false);
        }
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        // Allow super to try and create a view first
        final View result = super.onCreateView(name, context, attrs);
        if (result != null) {
            return result;
        }

        /*
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            // If we're running pre-L, we need to 'inject' our tint aware Views in place of the
            // standard framework versions
            switch (name) {
                case "EditText":
                    return new TintEditText(this, attrs);
                case "Spinner":
                    return new TintSpinner(this, attrs);
                case "CheckBox":
                    return new TintCheckBox(this, attrs);
                case "RadioButton":
                    return new TintRadioButton(this, attrs);
                case "CheckedTextView":
                    return new TintCheckedTextView(this, attrs);
            }
        }
        */

        return null;
    }

    @Override
    public void onBackPressed()
    {
        finish();
        Misc.rightTransition(ActivityWidgetPref.this);
    }
}