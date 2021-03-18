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
import de.codingforce.wad.item.layouts.ItemLayout;
import de.codingforce.wad.item.ItemShoppinglists;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OpenShoppinglists extends NameAwareFragment {
    private RecyclerView mRecyclerView;
    private RecylerAdapterOnClick mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private static final String LOG_TAG = "Open_Shoppinglists";

    ArrayList<ItemLayout> list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        Log.e(LOG_TAG, "--onCreatedView--");
        return inflater.inflate(R.layout.fragment_open_shoppinglists, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(LOG_TAG, "--onViewCreated--");

        list.clear();

        //API get list

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<List<ItemShoppinglists>> call = jsonPlaceHolderApi.getShoppinglists(MainActivity.userID);

        call.enqueue(new Callback<List<ItemShoppinglists>>() {
            @Override
            public void onResponse(Call<List<ItemShoppinglists>> call, Response<List<ItemShoppinglists>> response) {

                if(!response.isSuccessful()) {
                    Toast toast = Toast.makeText(view.getContext(), "Code "+ response.code(), Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                List<ItemShoppinglists> shoppinglists = response.body();

                for(ItemShoppinglists shopping_list : shoppinglists)
                {
                    list.add(new ItemLayout(shopping_list.getName(),shopping_list.getId()));
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
                        String text1 = list.get(position).getmText1();
                        String text2 = list.get(position).getmText2();
                        MainActivity.shoppinglistName = text1;
                        MainActivity.shoppinglistID = text2;

                        Class Shoppinglist = de.codingforce.wad.fragment.Shoppinglist.class;
                        MainActivity.main. placeFragment(Shoppinglist, R.id.mainFrame);
                    }
                });
            }

            @Override
            public void onFailure(Call<List<ItemShoppinglists>> call, Throwable t) {
                Log.e(LOG_TAG, t.getMessage());
            }
        });

    }

}
