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
import de.codingforce.wad.fragment.adapter.RecylerAdapterShoppinglist;
import de.codingforce.wad.item.layouts.ItemLayoutIngredients;
import de.codingforce.wad.item.ItemShoppinglists;
import de.codingforce.wad.item.ItemShoppinglistsIngredients;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Shoppinglist extends NameAwareFragment{
    private static final String LOG_TAG = "Shoppinglist";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    ArrayList<ItemLayoutIngredients> ingredients = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        Log.e(LOG_TAG, "--onCreatedView--");
        return inflater.inflate(R.layout.fragment_shoppinglist, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.e(LOG_TAG, "--onViewCreated--");
        MainActivity.main.change_title(MainActivity.shoppinglistName);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<ItemShoppinglists> call = jsonPlaceHolderApi.getShoppinglist(MainActivity.shoppinglistID);
        call.enqueue(new Callback<ItemShoppinglists>() {
            @Override
            public void onResponse(Call<ItemShoppinglists> call, Response<ItemShoppinglists> response) {

                if(!response.isSuccessful()) {
                    Toast toast = Toast.makeText(view.getContext(), "Code "+ response.code(), Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                ItemShoppinglists shoppinglists = response.body();

                for(ItemShoppinglistsIngredients ingredient : shoppinglists.getIngredients()){
                    boolean done = false;
                    if(ingredient.getDone().equals("true")){
                        done = true;
                    }
                    ingredients.add(new ItemLayoutIngredients(ingredient.getName(),ingredient.getAmount() + " " + ingredient.getUnit(),done));
                }

                //Set up Recyler View
                mRecyclerView = view.findViewById(R.id.shoppinglist_RecyclerView);
                mRecyclerView.setHasFixedSize(true);
                mLayoutManager = new LinearLayoutManager(view.getContext());
                mAdapter = new RecylerAdapterShoppinglist(ingredients);

                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<ItemShoppinglists> call, Throwable t) {
                Log.e(LOG_TAG, t.getMessage());
            }
        });
    }
}
