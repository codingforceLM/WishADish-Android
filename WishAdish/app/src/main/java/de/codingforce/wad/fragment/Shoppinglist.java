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
import de.codingforce.wad.fragment.adapter.RecylerAdapterOnClick;
import de.codingforce.wad.item.Item_layout;
import de.codingforce.wad.item.Item_shoppinglists;
import de.codingforce.wad.item.Item_shoppinglists_ingredients;
import de.codingforce.wad.item.Item_user;
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

    ArrayList<Item_layout> ingredients = new ArrayList<>();

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

        Call<Item_shoppinglists> call = jsonPlaceHolderApi.getShoppinglist(MainActivity.shoppinglistID);
        call.enqueue(new Callback<Item_shoppinglists>() {
            @Override
            public void onResponse(Call<Item_shoppinglists> call, Response<Item_shoppinglists> response) {

                if(!response.isSuccessful()) {
                    Toast toast = Toast.makeText(view.getContext(), "Code "+ response.code(), Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                Item_shoppinglists shoppinglists = response.body();

                for(Item_shoppinglists_ingredients ingredient : shoppinglists.getIngredients()){
                    ingredients.add(new Item_layout(ingredient.getName(),ingredient.getAmount() + " " + ingredient.getUnit()));
                }

                //Set up Recyler View
                mRecyclerView = view.findViewById(R.id.shoppinglist_RecyclerView);
                mRecyclerView.setHasFixedSize(true);
                mLayoutManager = new LinearLayoutManager(view.getContext());
                mAdapter = new RecylerAdapterOnClick(ingredients);

                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<Item_shoppinglists> call, Throwable t) {
                Log.e(LOG_TAG, t.getMessage());
            }
        });
    }
}
