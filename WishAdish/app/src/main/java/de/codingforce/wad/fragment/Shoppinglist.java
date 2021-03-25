package de.codingforce.wad.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import de.codeingforce.wad.R;
import de.codingforce.wad.activity.MainActivity;
import de.codingforce.wad.api.JsonPlaceHolderApi;
import de.codingforce.wad.fragment.adapter.RecylerAdapterShoppinglist;
import de.codingforce.wad.item.ItemIngredient;
import de.codingforce.wad.item.ItemIngredients;
import de.codingforce.wad.item.ItemMessage;
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
    private RecylerAdapterShoppinglist mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button saveButton;

    ArrayList<ItemLayoutIngredients> ingredients = new ArrayList<>();
    ItemShoppinglists shoppinglists;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        Log.e(LOG_TAG, "--onCreatedView--");
        return inflater.inflate(R.layout.fragment_shoppinglist, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.e(LOG_TAG, "--onViewCreated--");
        MainActivity.main.change_title(MainActivity.shoppinglistName);

        ingredients.clear();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<ItemShoppinglists> call = jsonPlaceHolderApi.getShoppinglist(MainActivity.token,MainActivity.shoppinglistID);
        call.enqueue(new Callback<ItemShoppinglists>() {
            @Override
            public void onResponse(Call<ItemShoppinglists> call, Response<ItemShoppinglists> response) {

                shoppinglists = response.body();

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

                //Buttons
                saveButton = view.findViewById(R.id.shoppinglist_save);
                saveButton.setOnClickListener(new Shoppinglist.ButtonListener());
            }

            @Override
            public void onFailure(Call<ItemShoppinglists> call, Throwable t) {
                Log.e(LOG_TAG, t.getMessage());
            }
        });
    }
    private class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Log.e(LOG_TAG,"Button Clicked");
            //get infos about all Checkboxes and changes done value of Ingredients
            ArrayList<Boolean> booleanlist = mAdapter.isCheckedList();
            for(int i = 0;i < shoppinglists.getIngredients().size();i++){
                try {
                    Boolean bool = false;
                    if(shoppinglists.getIngredients().get(i).getDone().equals("true")){
                        bool = true;
                    }
                    if (booleanlist.get(i) != bool) {
                        shoppinglists.getIngredients().get(i).setDone(booleanlist.get(i).toString());
                    }
                }
                catch (IndexOutOfBoundsException iobe){
                }
            }

            //create json for API call
            JsonArray ingrArray = new JsonArray();
            for(ItemShoppinglistsIngredients i : shoppinglists.getIngredients())
            {
                JsonObject ingr_Json = new JsonObject();
                Boolean bool = false;
                if(i.getDone().equals("true")) {
                    bool = true;
                }

                ingr_Json.addProperty("id",i.getId());
                ingr_Json.addProperty("amount",Integer.parseInt(i.getAmount()));
                ingr_Json.addProperty("unit",i.getUnit());
                ingr_Json.addProperty("done",bool);


                ingrArray.add(ingr_Json);
            }
            JsonObject shopping_Json = new JsonObject();
            shopping_Json.addProperty("id",shoppinglists.getId());
            shopping_Json.addProperty("ingredients",new Gson().toJson(ingrArray));



            //create put call
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(MainActivity.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

            Log.e(LOG_TAG,new Gson().toJson(shopping_Json).replace("\\","").replaceAll("\"\\[","[").replaceAll("\\]\"","]"));

            Call<ItemMessage> call = jsonPlaceHolderApi.changeShoppinglist(MainActivity.token,new Gson().toJson(shopping_Json).replace("\\","").replaceAll("\"\\[","[").replaceAll("\\]\"","]"));
            call.enqueue(new Callback<ItemMessage>() {
                @Override
                public void onResponse(Call<ItemMessage> call, Response<ItemMessage> response) {
                    if (!response.isSuccessful()) {
                        Toast toast = Toast.makeText(v.getContext(), "Code " + response.code() + " : " +response.message(), Toast.LENGTH_SHORT);
                        toast.show();
                        return;
                    }

                    for(ItemShoppinglistsIngredients ingr : shoppinglists.getIngredients()) {
                        Log.e(LOG_TAG, "Name :" + ingr.getName() + "   DONE :" + ingr.getDone());
                    }

                    //Shoppinglist
                    Class Shoppinglists = Shoppinglists.class;
                    MainActivity.main.placeFragment(Shoppinglists, R.id.mainFrame);
                }

                @Override
                public void onFailure(Call<ItemMessage> call, Throwable t) {
                    Log.e(LOG_TAG, t.getMessage());
                }
            });
        }
    }
}
