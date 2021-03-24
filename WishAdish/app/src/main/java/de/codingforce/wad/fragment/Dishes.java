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
import java.util.List;

import de.codeingforce.wad.R;
import de.codingforce.wad.activity.MainActivity;
import de.codingforce.wad.api.JsonPlaceHolderApi;
import de.codingforce.wad.fragment.adapter.RecylerAdapterOnClick;
import de.codingforce.wad.item.ItemDish;
import de.codingforce.wad.item.layouts.ItemLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Dishes extends NameAwareFragment {
    private static final String LOG_TAG = "Dishes";

    private RecyclerView mRecyclerView;
    private RecylerAdapterOnClick mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    ArrayList<ItemLayout> list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        Log.e(LOG_TAG, "--onCreatedView--");
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_dishes, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(LOG_TAG, "--onViewCreated--");
        MainActivity.main.change_title("Gerichte");

        list.clear();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<List<ItemDish>> call = jsonPlaceHolderApi.getDishes(MainActivity.token,MainActivity.userID);
        call.enqueue(new Callback<List<ItemDish>>() {
            @Override
            public void onResponse(Call<List<ItemDish>> call, Response<List<ItemDish>> response) {
                if(!response.isSuccessful()) {
                    Toast toast = Toast.makeText(view.getContext(), "Code "+ response.code(), Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                List<ItemDish> dishes = response.body();

                for(ItemDish dish : dishes){
                    list.add(new ItemLayout(dish.getName(),dish.getId()));
                }

                //Set up Recycler
                mRecyclerView = view.findViewById(R.id.RecylerView_dishes);
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
                        MainActivity.dishName = text1;
                        MainActivity.dishID = text2;

                        Class Dish = Dish.class;
                        MainActivity.main.placeFragment(Dish, R.id.mainFrame);
                    }
                });
            }

            @Override
            public void onFailure(Call<List<ItemDish>> call, Throwable t) {
                Log.e(LOG_TAG, t.getMessage());
            }
        });
    }
}
