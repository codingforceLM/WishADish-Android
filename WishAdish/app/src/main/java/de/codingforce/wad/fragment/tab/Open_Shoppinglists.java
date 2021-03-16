package de.codingforce.wad.fragment.tab;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import de.codeingforce.wad.R;
import de.codingforce.wad.activity.MainActivity;
import de.codingforce.wad.api.JsonPlaceHolderApi;
import de.codingforce.wad.fragment.NameAwareFragment;
import de.codingforce.wad.fragment.adapter.RecylerAdapterOnClick;
import de.codingforce.wad.item.Item_layout;
import de.codingforce.wad.item.Item_shoppinglists;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Open_Shoppinglists extends NameAwareFragment {
    private RecyclerView mRecyclerView;
    private RecylerAdapterOnClick mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private static final String LOG_TAG = "Open_Shoppinglists";

    ArrayList<Item_layout> list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        Log.e(LOG_TAG, "--onCreatedView--");
        return inflater.inflate(R.layout.fragment_open_shoppinglists, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(LOG_TAG, "--onViewCreated--");
        /*
        list.add(new Item_layout("Test","Best"));
        list.add(new Item_layout("Test2","Best2"));
        list.add(new Item_layout("Test3","Best3"));
        list.add(new Item_layout("Test4","Best4"));
        list.add(new Item_layout("Test","Best"));
        list.add(new Item_layout("Test2","Best2"));
        list.add(new Item_layout("Test3","Best3"));
        list.add(new Item_layout("Test4","Best4"));
        list.add(new Item_layout("Test","Best"));
        list.add(new Item_layout("Test2","Best2"));
        list.add(new Item_layout("Test3","Best3"));
        list.add(new Item_layout("Test4","Best4"));*/

        //API get list
        ArrayList<String> group = MainActivity.groups_main;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<List<Item_shoppinglists>> call = jsonPlaceHolderApi.getShoppinglists(group.get(0));

        call.enqueue(new Callback<List<Item_shoppinglists>>() {
            @Override
            public void onResponse(Call<List<Item_shoppinglists>> call, Response<List<Item_shoppinglists>> response) {

                if(!response.isSuccessful()) {
                    Toast toast = Toast.makeText(view.getContext(), "Code "+ response.code(), Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                List<Item_shoppinglists> shoppinglists = response.body();

                for(Item_shoppinglists shopping_list : shoppinglists)
                {
                    list.add(new Item_layout(shopping_list.getName(),shopping_list.getId()));
                    Log.e(LOG_TAG,shopping_list.getId() + shopping_list.getName());
                }
                //Set up Recycler
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

            @Override
            public void onFailure(Call<List<Item_shoppinglists>> call, Throwable t) {
                Log.e(LOG_TAG, t.getMessage());
            }
        });

    }

}
