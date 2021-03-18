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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.codeingforce.wad.R;
import de.codingforce.wad.activity.MainActivity;
import de.codingforce.wad.api.JsonPlaceHolderApi;
import de.codingforce.wad.fragment.NameAwareFragment;
import de.codingforce.wad.fragment.adapter.RecylerAdapter;
import de.codingforce.wad.item.layouts.ItemLayout;
import de.codingforce.wad.item.ItemWish;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DishesToday extends NameAwareFragment {

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

    private ArrayList<ItemLayout> list_morning = new ArrayList<>();
    private ArrayList<ItemLayout> list_lunch = new ArrayList<>();
    private ArrayList<ItemLayout> list_evening = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        Log.e(LOG_TAG, "--onCreatedView--");
        return inflater.inflate(R.layout.fragment_dishes_today, parent, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(LOG_TAG, "--onViewCreated--");

        //Heutigen Tag bekommen
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String month;
        if(calendar.get(Calendar.MONTH) + 1 < 10)
        {
            month ="0"+Integer.toString(calendar.get(Calendar.MONTH) + 1);
        }else{
            month = Integer.toString(calendar.get(Calendar.MONTH) + 1);
        }
        String day = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));

        Log.e(LOG_TAG,"Month"+month);
        //API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<List<ItemWish>> call = jsonPlaceHolderApi.getWish(MainActivity.userID,month,day);
        call.enqueue(new Callback<List<ItemWish>>() {
            @Override
            public void onResponse(Call<List<ItemWish>> call, Response<List<ItemWish>> response) {
                if(!response.isSuccessful()) {
                    Toast toast = Toast.makeText(view.getContext(), "Code "+ response.code(), Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                List<ItemWish> wishes = response.body();

                for(ItemWish wish : wishes){
                    if(wish.getDaytime().equals("morning")) {
                        list_morning.add(new ItemLayout(wish.getName(),wish.getGroupname()));
                    }else if(wish.getDaytime().equals("lunch")){
                        list_lunch.add(new ItemLayout(wish.getName(),wish.getGroupname()));
                    }else if(wish.getDaytime().equals("evening")){
                        list_evening.add(new ItemLayout(wish.getName(),wish.getGroupname()));
                    }
                }


                //Set up morning Recycler
                mRecyclerView_morning = view.findViewById(R.id.recycler_morning);
                mRecyclerView_morning.setHasFixedSize(true);
                mLayoutManager_morning = new LinearLayoutManager(view.getContext());
                mAdapter_morning = new RecylerAdapter(list_morning);

                mRecyclerView_morning.setLayoutManager(mLayoutManager_morning);
                mRecyclerView_morning.setAdapter(mAdapter_morning);

                //Set up day Recycler
                mRecyclerView_day = view.findViewById(R.id.recycler_day);
                mRecyclerView_day.setHasFixedSize(true);
                mLayoutManager_day = new LinearLayoutManager(view.getContext());
                mAdapter_day = new RecylerAdapter(list_lunch);

                mRecyclerView_day.setLayoutManager(mLayoutManager_day);
                mRecyclerView_day.setAdapter(mAdapter_day);

                //Set up evening Recycler
                mRecyclerView_evening = view.findViewById(R.id.recycler_evening);
                mRecyclerView_evening.setHasFixedSize(true);
                mLayoutManager_evening = new LinearLayoutManager(view.getContext());
                mAdapter_evening = new RecylerAdapter(list_evening);

                mRecyclerView_evening.setLayoutManager(mLayoutManager_evening);
                mRecyclerView_evening.setAdapter(mAdapter_evening);
            }

            @Override
            public void onFailure(Call<List<ItemWish>> call, Throwable t) {
                Log.e(LOG_TAG, t.getMessage());
            }
        });
    }
}
