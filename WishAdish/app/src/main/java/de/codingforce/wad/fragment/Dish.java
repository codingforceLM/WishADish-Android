package de.codingforce.wad.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.codeingforce.wad.R;
import de.codingforce.wad.activity.MainActivity;
import de.codingforce.wad.api.JsonPlaceHolderApi;
import de.codingforce.wad.fragment.adapter.RecylerAdapter;
import de.codingforce.wad.fragment.adapter.RecylerAdapter_Shoppinglist;
import de.codingforce.wad.item.Item_dish;
import de.codingforce.wad.item.Item_shoppinglists;
import de.codingforce.wad.item.Item_shoppinglists_ingredients;
import de.codingforce.wad.item.layouts.Item_layout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Dish extends NameAwareFragment{
    private static final String LOG_TAG = "Dish";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    ArrayList<Item_layout> list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        Log.e(LOG_TAG, "--onCreatedView--");
        return inflater.inflate(R.layout.fragment_dish, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(LOG_TAG, "--onViewCreated--");
        MainActivity.main.change_title(MainActivity.dishName);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<Item_dish> call = jsonPlaceHolderApi.getDish(MainActivity.dishID);
        call.enqueue(new Callback<Item_dish>() {
            @Override
            public void onResponse(Call<Item_dish> call, Response<Item_dish> response) {
                if(!response.isSuccessful()) {
                    Toast toast = Toast.makeText(view.getContext(), "Code "+ response.code(), Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                Item_dish dish = response.body();

                for(Item_shoppinglists_ingredients ingredient : dish.getIngredients()){
                    list.add(new Item_layout(ingredient.getName(),ingredient.getAmount() + " " + ingredient.getUnit()));
                }

                //Set up Recyler View
                mRecyclerView = view.findViewById(R.id.Recylerview_dish);
                mRecyclerView.setHasFixedSize(true);
                mLayoutManager = new LinearLayoutManager(view.getContext());
                mAdapter = new RecylerAdapter(list);

                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<Item_dish> call, Throwable t) {
                Log.e(LOG_TAG, t.getMessage());
            }
        });
    }
}
