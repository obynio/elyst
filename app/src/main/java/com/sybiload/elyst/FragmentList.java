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
        Misc.populateList(getActivity());

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
        Misc.populateItem(getActivity(), Static.currentList);

        Intent intent = new Intent(getActivity(), ActivityShop.class);

        startActivity(intent);
        Misc.leftTransition(getActivity());
    }
}
