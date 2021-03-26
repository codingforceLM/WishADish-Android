package de.codingforce.wad.fragment.add;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import de.codeingforce.wad.R;
import de.codingforce.wad.activity.MainActivity;
import de.codingforce.wad.api.JsonPlaceHolderApi;
import de.codingforce.wad.fragment.NameAwareFragment;
import de.codingforce.wad.fragment.adapter.RecylerAdapterShoppinglist;
import de.codingforce.wad.item.ItemDish;
import de.codingforce.wad.item.ItemIngredient;
import de.codingforce.wad.item.ItemIngredients;
import de.codingforce.wad.item.ItemMessage;
import de.codingforce.wad.item.ItemShoppinglists;
import de.codingforce.wad.item.ItemShoppinglistsIngredients;
import de.codingforce.wad.item.layouts.ItemLayoutIngredients;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddShoppinglistDish extends NameAwareFragment {
    private static final String LOG_TAG = "Add Shoppinglist Dish";
    private Spinner spinner;
    private ArrayAdapter adapter_dish;
    private Button button;
    private Button addButton;
    private RecyclerView mRecyclerView;
    private RecylerAdapterShoppinglist mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private EditText personcount;

    private List<ItemDish> dishes;
    private ItemDish dish;
    private View mainview;
    private ItemShoppinglists shoppinglists;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        Log.e(LOG_TAG, "--onCreatedView--");
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_add_shoppinglist_dish, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(LOG_TAG, "--onViewCreated--");
        MainActivity.main.change_title("Gericht hinzufügen");
        personcount = view.findViewById(R.id.add_shoppinglist_dish_person);
        mainview = view;
        if(dish != null){
            dish.getIngredients().clear();
        }
        //spinner ingredients

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<List<ItemDish>> call = jsonPlaceHolderApi.getDishes(MainActivity.token, MainActivity.userID);
        call.enqueue(new Callback<List<ItemDish>>() {
            @Override
            public void onResponse(Call<List<ItemDish>> call, Response<List<ItemDish>> response) {
                if (!response.isSuccessful()) {
                    Toast toast = Toast.makeText(view.getContext(), "Code " + response.code(), Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                dishes = response.body();
                ArrayList<String> dishes_List = new ArrayList<>();
                for (ItemDish dish : dishes) {
                    dishes_List.add(dish.getName());
                }


                spinner = view.findViewById(R.id.spinner_add_shoopinglist_dish);
                adapter_dish = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, dishes_List);
                adapter_dish.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinner.setAdapter(adapter_dish);
            }

            @Override
            public void onFailure(Call<List<ItemDish>> call, Throwable t) {
                Log.e(LOG_TAG, t.getMessage());
            }
        });

        button = view.findViewById(R.id.button_add_shoppinglist_dish);
        button.setOnClickListener(new AddShoppinglistDish.ButtonListener());

        addButton = view.findViewById((R.id.button_add_dish_shoppinglist));
        addButton.setOnClickListener(new AddShoppinglistDish.ButtonListener_add());

        //get all given shoppinglist ingr
        Call<ItemShoppinglists> call2 = jsonPlaceHolderApi.getShoppinglist(MainActivity.token,MainActivity.shoppinglistID);
        call2.enqueue(new Callback<ItemShoppinglists>() {
            @Override
            public void onResponse(Call<ItemShoppinglists> call, Response<ItemShoppinglists> response) {
                shoppinglists = response.body();
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
            Log.e(LOG_TAG, "--Button Clicked--");
            String dishID = dishes.get(spinner.getSelectedItemPosition()).getId();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(MainActivity.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

            Call<ItemDish> call = jsonPlaceHolderApi.getDish(MainActivity.token, dishID);
            call.enqueue(new Callback<ItemDish>() {
                @Override
                public void onResponse(Call<ItemDish> call, Response<ItemDish> response) {
                    if (!response.isSuccessful()) {
                        Toast toast = Toast.makeText(v.getContext(), "Code " + response.code(), Toast.LENGTH_SHORT);
                        toast.show();
                        return;
                    }
                    ArrayList<ItemLayoutIngredients> ingredients = new ArrayList<>();
                    dish = response.body();
                    for(int i = 0; i< dish.getIngredients().size();i++){
                        dish.getIngredients().get(i).setAmount(Double.toString(Double.parseDouble(dish.getIngredients().get(i).getAmount()) * Double.parseDouble(personcount.getText().toString())));
                        if(Double.parseDouble(dish.getIngredients().get(i).getAmount()) >= 1000)
                        {
                            switch(dish.getIngredients().get(i).getUnit()){
                                case "g":
                                    dish.getIngredients().get(i).setUnit("kg");
                                    dish.getIngredients().get(i).setAmount(Double.toString(Double.parseDouble(dish.getIngredients().get(i).getAmount()) / 1000));
                                    break;
                                case "ml":
                                    dish.getIngredients().get(i).setUnit("l");
                                    dish.getIngredients().get(i).setAmount(Double.toString(Double.parseDouble(dish.getIngredients().get(i).getAmount()) / 1000));
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                    for(ItemShoppinglistsIngredients ingr : dish.getIngredients()){
                        ingredients.add(new ItemLayoutIngredients(ingr.getName(),ingr.getAmount() + " " + ingr.getUnit(),false));
                    }

                    //Set up Recyler View
                    mRecyclerView = mainview.findViewById(R.id.recyler_add_dish);
                    mRecyclerView.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(mainview.getContext());
                    mAdapter = new RecylerAdapterShoppinglist(ingredients);

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

    private class ButtonListener_add implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            JsonArray ingrArray = new JsonArray();
            ArrayList<Boolean> bool_list = mAdapter.isCheckedList();
            //get dish ingr
            for(int i = 0; i< dish.getIngredients().size(); i++)
            {
                JsonObject ingr_Json = new JsonObject();

                if(bool_list.size()>i) {
                    if(!bool_list.get(i)) {
                        ingr_Json.addProperty("id", dish.getIngredients().get(i).getId());
                        ingr_Json.addProperty("amount", Double.parseDouble(dish.getIngredients().get(i).getAmount()));
                        ingr_Json.addProperty("unit", dish.getIngredients().get(i).getUnit());
                        ingr_Json.addProperty("done", false);

                        ingrArray.add(ingr_Json);
                    }
                }else {
                    ingr_Json.addProperty("id", dish.getIngredients().get(i).getId());
                    ingr_Json.addProperty("amount", Double.parseDouble(dish.getIngredients().get(i).getAmount()));
                    ingr_Json.addProperty("unit", dish.getIngredients().get(i).getUnit());
                    ingr_Json.addProperty("done", false);

                    ingrArray.add(ingr_Json);
                }
            }

            //get all given Ingredients from shoppinglist
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

            //create full json
            JsonObject shopping_Json = new JsonObject();
            shopping_Json.addProperty("id",MainActivity.shoppinglistID);
            shopping_Json.addProperty("ingredients",new Gson().toJson(ingrArray));

            //Log.e(LOG_TAG,new Gson().toJson(shopping_Json).replace("\\","").replaceAll("\"\\[","[").replaceAll("\\]\"","]"));

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(MainActivity.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
            Call<ItemMessage> call = jsonPlaceHolderApi.changeShoppinglist(MainActivity.token,new Gson().toJson(shopping_Json).replace("\\","").replaceAll("\"\\[","[").replaceAll("\\]\"","]"));
            call.enqueue(new Callback<ItemMessage>() {
                @Override
                public void onResponse(Call<ItemMessage> call, Response<ItemMessage> response) {
                    if (!response.isSuccessful()) {
                        Toast toast = Toast.makeText(v.getContext(), "Code " + response.code() + " : " +response.message(), Toast.LENGTH_SHORT);
                        toast.show();
                        return;
                    }
                    if (response.code() != 200) {
                        Toast toast = Toast.makeText(v.getContext(), "Fehler " + response.code() + "daten konnten nicht gespeichert werden", Toast.LENGTH_SHORT);
                        toast.show();
                        return;
                    }
                    ItemMessage message = response.body();
                    Toast toast = Toast.makeText(v.getContext(), message.getMsg(), Toast.LENGTH_SHORT);
                    toast.show();

                    //Ingredients
                    Class Shoppinglist = de.codingforce.wad.fragment.Shoppinglist.class;
                    MainActivity.main.placeFragment(Shoppinglist, R.id.mainFrame);
                }

                @Override
                public void onFailure(Call<ItemMessage> call, Throwable t) {
                    Log.e(LOG_TAG, t.getMessage());
                }
            });
        }
    }
}
