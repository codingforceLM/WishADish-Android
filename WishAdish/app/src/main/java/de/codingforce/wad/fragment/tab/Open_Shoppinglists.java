package de.codingforce.wad.fragment.tab;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.codeingforce.wad.R;
import de.codingforce.wad.fragment.NameAwareFragment;
import de.codingforce.wad.fragment.adapter.RecylerAdapterOnClick;
import de.codingforce.wad.item.item_layout;

public class Open_Shoppinglists extends NameAwareFragment {
    private RecyclerView mRecyclerView;
    private RecylerAdapterOnClick mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private static final String LOG_TAG = "Open_Shoppinglists";

    ArrayList<item_layout> list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        Log.e(LOG_TAG, "--onCreatedView--");
        return inflater.inflate(R.layout.fragment_open_shoppinglists, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(LOG_TAG, "--onViewCreated--");

        list.add(new item_layout("Test","Best"));
        list.add(new item_layout("Test2","Best2"));
        list.add(new item_layout("Test3","Best3"));
        list.add(new item_layout("Test4","Best4"));
        list.add(new item_layout("Test","Best"));
        list.add(new item_layout("Test2","Best2"));
        list.add(new item_layout("Test3","Best3"));
        list.add(new item_layout("Test4","Best4"));
        list.add(new item_layout("Test","Best"));
        list.add(new item_layout("Test2","Best2"));
        list.add(new item_layout("Test3","Best3"));
        list.add(new item_layout("Test4","Best4"));

        //Set up morning Recycler
        mRecyclerView = view.findViewById(R.id.open_shoppinglists);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(view.getContext());
        mAdapter = new RecylerAdapterOnClick(list);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new RecylerAdapterOnClick.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String text = list.get(position).getmText1();
                Log.e(LOG_TAG, text);
            }
        });

    }

}
