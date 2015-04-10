package com.sybiload.elyst.Pref;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.sybiload.elyst.Misc;
import com.sybiload.elyst.R;

public class ActivityAppPref extends PreferenceActivity
{
    PreferenceScreen prefBootBehaviour;
    PreferenceScreen prefUi;
    PreferenceScreen prefWidget;

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.app_pref);

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
                new Misc().rightTransition(ActivityAppPref.this);
            }
        });

        prefUi = (PreferenceScreen) findPreference("prefUi");
        prefUi.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            public boolean onPreferenceClick(Preference preference)
            {
                Intent intent = new Intent(getApplicationContext(), ActivityUiPref.class);
                startActivity(intent);
                new Misc().leftTransition(ActivityAppPref.this);
                return true;
            }
        });

        prefBootBehaviour = (PreferenceScreen) findPreference("prefSystem");
        prefBootBehaviour.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            public boolean onPreferenceClick(Preference preference)
            {
                Intent intent = new Intent(getApplicationContext(), ActivitySystemPref.class);
                startActivity(intent);
                new Misc().leftTransition(ActivityAppPref.this);
                return true;
            }
        });

        prefWidget = (PreferenceScreen) findPreference("prefWidget");
        prefWidget.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            public boolean onPreferenceClick(Preference preference)
            {
                Intent intent = new Intent(getApplicationContext(), ActivityWidgetPref.class);
                startActivity(intent);
                new Misc().leftTransition(ActivityAppPref.this);
                return true;
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        finish();
        new Misc().rightTransition(ActivityAppPref.this);
    }
}