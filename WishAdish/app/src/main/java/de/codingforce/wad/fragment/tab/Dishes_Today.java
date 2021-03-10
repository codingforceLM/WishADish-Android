package de.codingforce.wad.fragment.tab;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.codeingforce.wad.R;
import de.codingforce.wad.fragment.NameAwareFragment;
import de.codingforce.wad.fragment.adapter.RecylerAdapter;
import de.codingforce.wad.item.item_layout;

public class Dishes_Today extends NameAwareFragment {

    private static final String LOG_TAG = "Dishes_Today";

    private RecyclerView mRecyclerView_morning;
    private RecyclerView.Adapter mAdapter_morning;
    private RecyclerView.LayoutManager mLayoutManager_morning;

    private RecyclerView mRecyclerView_day;
    private RecyclerView.Adapter mAdapter_day;
    private RecyclerView.LayoutManager mLayoutManager_day;

    private RecyclerView mRecyclerView_evening;
    private RecyclerView.Adapter mAdapter_evening;
    private RecyclerView.LayoutManager mLayoutManager_evening;

    ArrayList<item_layout> list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        Log.e(LOG_TAG, "--onCreatedView--");
        return inflater.inflate(R.layout.fragment_dishes_today, parent, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(LOG_TAG, "--onViewCreated--");

        list.add(new item_layout("Test","Best"));
        list.add(new item_layout("Test2","Best2"));
        list.add(new item_layout("Test3","Best3"));
        list.add(new item_layout("Test4","Best4"));

        //Set up morning Recycler
        mRecyclerView_morning = view.findViewById(R.id.recycler_morning);
        mRecyclerView_morning.setHasFixedSize(true);
        mLayoutManager_morning = new LinearLayoutManager(view.getContext());
        mAdapter_morning = new RecylerAdapter(list);

        mRecyclerView_morning.setLayoutManager(mLayoutManager_morning);
        mRecyclerView_morning.setAdapter(mAdapter_morning);

        //Set up day Recycler
        mRecyclerView_day = view.findViewById(R.id.recycler_day);
        mRecyclerView_day.setHasFixedSize(true);
        mLayoutManager_day = new LinearLayoutManager(view.getContext());
        mAdapter_day = new RecylerAdapter(list);

        mRecyclerView_day.setLayoutManager(mLayoutManager_day);
        mRecyclerView_day.setAdapter(mAdapter_day);

        //Set up evening Recycler
        mRecyclerView_evening = view.findViewById(R.id.recycler_evening);
        mRecyclerView_evening.setHasFixedSize(true);
        mLayoutManager_evening = new LinearLayoutManager(view.getContext());
        mAdapter_evening = new RecylerAdapter(list);

        mRecyclerView_evening.setLayoutManager(mLayoutManager_evening);
        mRecyclerView_evening.setAdapter(mAdapter_evening);


    }
}
