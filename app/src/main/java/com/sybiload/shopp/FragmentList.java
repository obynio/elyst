package com.sybiload.shopp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.sybiload.shopp.Adapter.ShopAdapter;

import java.util.ArrayList;

public class FragmentList extends Fragment
{
    SharedPreferences shopItemPref;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    ImageButton fabImageButton;

    private Misc misc = new Misc();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        fabImageButton = (ImageButton) view.findViewById(R.id.fab_image_button);

        fabImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityAdd.class);
                startActivity(intent);
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }

    @Override
    public void onResume()
    {
        // add all items to shop
        ShopAdapter shopAdapter = new ShopAdapter(getActivity());
        recyclerView.setAdapter(shopAdapter);
        super.onResume();
    }
}
