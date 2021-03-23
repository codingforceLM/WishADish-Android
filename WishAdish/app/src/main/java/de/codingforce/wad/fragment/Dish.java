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
import de.codingforce.wad.item.ItemDish;
import de.codingforce.wad.item.ItemShoppinglistsIngredients;
import de.codingforce.wad.item.layouts.ItemLayout;
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

    ArrayList<ItemLayout> list = new ArrayList<>();

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

        Call<ItemDish> call = jsonPlaceHolderApi.getDish(MainActivity.token,MainActivity.dishID);
        call.enqueue(new Callback<ItemDish>() {
            @Override
            public void onResponse(Call<ItemDish> call, Response<ItemDish> response) {
                if(!response.isSuccessful()) {
                    Toast toast = Toast.makeText(view.getContext(), "Code "+ response.code(), Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                ItemDish dish = response.body();

                for(ItemShoppinglistsIngredients ingredient : dish.getIngredients()){
                    list.add(new ItemLayout(ingredient.getName(),ingredient.getAmount() + " " + ingredient.getUnit()));
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
            public void onFailure(Call<ItemDish> call, Throwable t) {
                Log.e(LOG_TAG, t.getMessage());
            }
        });
    }
}
