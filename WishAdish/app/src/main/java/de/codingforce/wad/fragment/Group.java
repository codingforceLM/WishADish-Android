package de.codingforce.wad.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.codeingforce.wad.R;
import de.codingforce.wad.activity.MainActivity;
import de.codingforce.wad.api.JsonPlaceHolderApi;
import de.codingforce.wad.fragment.adapter.RecylerAdapter;
import de.codingforce.wad.fragment.adapter.RecylerAdapterOnClick;
import de.codingforce.wad.fragment.adapter.RecylerAdapterShoppinglist;
import de.codingforce.wad.item.ItemGroup;
import de.codingforce.wad.item.ItemGroups;
import de.codingforce.wad.item.ItemInvite;
import de.codingforce.wad.item.ItemShoppinglists;
import de.codingforce.wad.item.layouts.ItemLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Group extends NameAwareFragment{
    private static final String LOG_TAG = "Group";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button invite_Button;
    private TextView invite_text;

    ArrayList<ItemLayout> list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        Log.e(LOG_TAG, "--onCreatedView--");
        return inflater.inflate(R.layout.fragment_group, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(LOG_TAG, "--onViewCreated--");
        MainActivity.main.change_title(MainActivity.groupName);

        invite_Button = view.findViewById(R.id.invite_button);
        invite_Button.setOnClickListener(new Group.ButtonListener());

        invite_text = view.findViewById(R.id.textView_link);

        list.clear();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<ItemGroups> call = jsonPlaceHolderApi.getGroup(MainActivity.token,MainActivity.groupID);
        call.enqueue(new Callback<ItemGroups>() {
            @Override
            public void onResponse(Call<ItemGroups> call, Response<ItemGroups> response) {
                if(!response.isSuccessful()) {
                    Toast toast = Toast.makeText(view.getContext(), "Code "+ response.code(), Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                ItemGroups groups = response.body();
                for(ItemGroup group : groups.getUser()){
                    list.add(new ItemLayout(group.getName(),group.getRole()));
                }
                //Set up Recyler View
                mRecyclerView = view.findViewById(R.id.RecylerView_Group);
                mRecyclerView.setHasFixedSize(true);
                mLayoutManager = new LinearLayoutManager(view.getContext());
                mAdapter = new RecylerAdapter(list);

                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onFailure(Call<ItemGroups> call, Throwable t) {
                Log.e(LOG_TAG, t.getMessage());
            }
        });
    }

    private class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(MainActivity.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

            Call<ItemInvite> call = jsonPlaceHolderApi.getInvite(MainActivity.token,MainActivity.groupID);
            call.enqueue(new Callback<ItemInvite>() {
                @Override
                public void onResponse(Call<ItemInvite> call, Response<ItemInvite> response) {
                    if(!response.isSuccessful()) {
                        Toast toast = Toast.makeText(v.getContext(), "Code "+ response.code(), Toast.LENGTH_SHORT);
                        toast.show();
                        return;
                    }

                    ItemInvite invite = response.body();
                    String link = "http://localhost:8080/#/invite/" + invite.getId();
                    invite_text.setText(link);

                }

                @Override
                public void onFailure(Call<ItemInvite> call, Throwable t) {
                    Log.e(LOG_TAG, t.getMessage());
                }
            });
        }
    }
}
