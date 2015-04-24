package com.sybiload.elyst;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import com.google.zxing.integration.android.IntentIntegrator;
import com.sybiload.elyst.Adapter.AdapterList;
import com.sybiload.elyst.Adapter.AdapterShop;
import com.sybiload.elyst.Util.IabHelper;

public class FragmentList extends Fragment
{
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    public static AdapterList currAdapter;

    ImageButton fabImageButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        // populate items
        new Misc().populateList(getActivity());

        ActivityMain.drawerToggle.setToolbarNavigationClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                currAdapter.clearSelected();
                pressSelect();
            }
        });

        ActivityMain.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {

                switch (item.getItemId())
                {
                    case R.id.action_edit:

                        Static.currentList = currAdapter.selectedList;

                        Intent intent = new Intent(getActivity(), ActivityNewList.class);
                        startActivity(intent);

                        return true;

                    case R.id.action_delete:

                        currAdapter.delete(currAdapter.selectedList);

                        currAdapter.clearSelected();
                        pressSelect();

                        return true;

                    case R.id.action_buy:
                        ((ActivityMain)getActivity()).buy();
                        return true;

                    default:
                        return false;
                }
            }
        });

        fabImageButton = (ImageButton) view.findViewById(R.id.imageButtonListFab);


        fabImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Static.currentList = null;

                Intent intent = new Intent(getActivity(), ActivityNewList.class);
                startActivity(intent);
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewList);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // set adapter
        currAdapter = new AdapterList(this);
        recyclerView.setAdapter(currAdapter);

        return view;
    }

    public void enterList(final int position)
    {
        Static.currentList = Static.allList.get(position);
        new Misc().populateItem(getActivity(), Static.currentList);

        Intent intent = new Intent(getActivity(), ActivityShop.class);

        startActivity(intent);
        new Misc().leftTransition(getActivity());
    }

    public void pressSelect()
    {
        if (currAdapter.selectedHolder == null)
        {
            // check if isPro
            getActivity().invalidateOptionsMenu();

            ActivityMain.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

            ActivityMain.drawerToggle.setDrawerIndicatorEnabled(true);

            // restore new item option removed to prevent bug abuse
            fabUp();
        }
        else
        {
            ActivityMain.toolbar.getMenu().clear();
            ActivityMain.toolbar.inflateMenu(R.menu.edit_delete);

            ActivityMain.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

            ActivityMain.drawerToggle.setDrawerIndicatorEnabled(false);

            // remove new item option to prevent bug abuse
            fabDown();
        }

    }

    private void fabUp()
    {
        if (!fabImageButton.isClickable())
        {
            fabImageButton.setClickable(true);

            Animation scaleAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_up);
            scaleAnim.setFillAfter(true);
            fabImageButton.startAnimation(scaleAnim);
        }
    }

    private void fabDown()
    {
        if (fabImageButton.isClickable())
        {
            fabImageButton.setClickable(false);

            Animation scaleAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_down);
            scaleAnim.setFillAfter(true);
            fabImageButton.startAnimation(scaleAnim);
        }
    }

    @Override
    public void onPause()
    {
        currAdapter.clearSelected();
        pressSelect();

        super.onPause();
    }
}
