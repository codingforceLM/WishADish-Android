package de.codingforce.wad.fragment.add;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.codeingforce.wad.R;
import de.codingforce.wad.activity.MainActivity;
import de.codingforce.wad.api.JsonPlaceHolderApi;
import de.codingforce.wad.fragment.Dishes;
import de.codingforce.wad.fragment.Ingredients;
import de.codingforce.wad.fragment.NameAwareFragment;
import de.codingforce.wad.item.ItemDishIngredients;
import de.codingforce.wad.item.ItemGroups;
import de.codingforce.wad.item.ItemIngredient;
import de.codingforce.wad.item.ItemIngredients;
import de.codingforce.wad.item.ItemMessage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateDish extends NameAwareFragment {
    private static final String LOG_TAG = "CreatDishes";

    private EditText dish_name;
    private Button button;
    private FloatingActionButton more_ing;
    private LinearLayout layout_list;

    //lists for spinner
    private ArrayList<String> ingredients;
    private ArrayList<String> units;

    //Ingredientslist
    private ArrayList<ItemIngredients> ingredients_list = new ArrayList<>();

    private List<ItemIngredient>ingr_list;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        Log.e(LOG_TAG, "--onCreatedView--");
        return inflater.inflate(R.layout.fragment_creat_dish, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(LOG_TAG, "--onViewCreated--");

        //add Layout
        layout_list= view.findViewById(R.id.layout_list);

        //dish name
        dish_name = view.findViewById(R.id.dish_name_plainText);

        //get Data
        ingredients = new ArrayList<>();
        ingredients.clear();
        ingredients.add("Zutat auswählen");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<List<ItemIngredient>> call = jsonPlaceHolderApi.getIngredients(MainActivity.token,MainActivity.userID);
        call.enqueue(new Callback<List<ItemIngredient>>() {
            @Override
            public void onResponse(Call<List<ItemIngredient>> call, Response<List<ItemIngredient>> response) {
                if(!response.isSuccessful()) {
                    Toast toast = Toast.makeText(view.getContext(), "Code "+ response.code(), Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                ingr_list = response.body();

                Call<List<ItemIngredient>> call2 = jsonPlaceHolderApi.getIngredients(MainActivity.token,MainActivity.systemID);
                call2.enqueue(new Callback<List<ItemIngredient>>() {
                    @Override
                    public void onResponse(Call<List<ItemIngredient>> call, Response<List<ItemIngredient>> response) {
                        List<ItemIngredient> ingrsys = response.body();
                        for(ItemIngredient ingr : ingrsys){
                            ingr_list.add(ingr);
                        }
                        for(ItemIngredient ingr : ingr_list){
                            ingredients.add(ingr.getName());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ItemIngredient>> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onFailure(Call<List<ItemIngredient>> call, Throwable t) {
                Log.e(LOG_TAG, t.getMessage());
            }
        });

        units = new ArrayList<>();
        units.add("Einheit auswählen");
        units.add("g");
        units.add("kg");
        units.add("ml");
        units.add("l");
        units.add("stk");

        //Action Button
        more_ing = view.findViewById(R.id.more_ingredients);

        more_ing.setOnClickListener(new ActionButtonListener());

        //creat Button
        button = view.findViewById(R.id.button2);
        button.setOnClickListener(new ButtonListener());
    }

    private class ButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Log.e(LOG_TAG, "--Button Clicked--");

            if(checkIfValidAndRead())
            {
                JsonArray ingrArray = new JsonArray();
                for(ItemIngredients i : ingredients_list)
                {
                    JsonObject ingr_Json = new JsonObject();
                    String ingrID=null;
                    for(ItemIngredient ingr :ingr_list){
                        if(ingr.getName().equals(i.getIngredient())){
                            ingrID = ingr.getId();
                            break;
                        }
                    }
                    ingr_Json.addProperty("id",ingrID);
                    ingr_Json.addProperty("amount",i.getAmount());
                    ingr_Json.addProperty("unit",i.getUnit());

                    ingrArray.add(ingr_Json);
                }
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(MainActivity.URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);


                Call<ItemMessage> call = jsonPlaceHolderApi.createDish(MainActivity.token,MainActivity.userID,dish_name.getText().toString(),new Gson().toJson(ingrArray));
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
                        Class Dishes = de.codingforce.wad.fragment.Dishes.class;
                        MainActivity.main.placeFragment(Dishes, R.id.mainFrame);
                    }

                    @Override
                    public void onFailure(Call<ItemMessage> call, Throwable t) {
                        Log.e(LOG_TAG, t.getMessage());
                    }
                });
            }
        }
    }

    private class ActionButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Log.e(LOG_TAG, "--ActionButton Clicked--");
            addView();
        }
    }

    private void addView()
    {
        Log.e(LOG_TAG, "--addView--");
        final View ingredientsView = getLayoutInflater().inflate(R.layout.dynamics_ingredients,null,false);


        EditText amount = ingredientsView.findViewById(R.id.text_amount);
        Spinner spinnerIngr = ingredientsView.findViewById(R.id.spinner_ingr);
        Spinner spinnerUnit = ingredientsView.findViewById(R.id.spinner_unit);
        ArrayAdapter adapter_ingr;
        ArrayAdapter adapter_unit;


        //delete
        FloatingActionButton deleteButton = ingredientsView.findViewById(R.id.delete_button);

        //spinner ingredients
        adapter_ingr = new ArrayAdapter(this.getContext(), android.R.layout.simple_spinner_item, ingredients);
        adapter_ingr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerIngr.setAdapter(adapter_ingr);

        //spinner unit
        adapter_unit = new ArrayAdapter(this.getContext(), android.R.layout.simple_spinner_item, units);
        adapter_unit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerUnit.setAdapter(adapter_unit);

        //amount
        amount = ingredientsView.findViewById(R.id.text_amount);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeView(ingredientsView);
            }
        });

        layout_list.addView(ingredientsView);
    }

    private void removeView(View view)
    {
        Log.e(LOG_TAG, "--RemoveView--");
        layout_list.removeView(view);
    }


    private boolean checkIfValidAndRead() {
        ingredients_list.clear();

        for(int i=0;i<layout_list.getChildCount();i++)
        {
            View ingredientsView = layout_list.getChildAt(i);

            EditText amount = ingredientsView.findViewById(R.id.text_amount);
            Spinner spinnerIngr = ingredientsView.findViewById(R.id.spinner_ingr);
            Spinner spinnerUnit = ingredientsView.findViewById(R.id.spinner_unit);

            //check if spinner ing is set
            if(spinnerIngr.getSelectedItemPosition()==0)
            {
                Toast toast = Toast.makeText(ingredientsView.getContext(), "Bitte eine Zutat auswählen", Toast.LENGTH_SHORT);
                toast.show();
                return false;
            }


            //check if amount is set and is only Numbers
            if(amount.getText().toString().equals(""))
            {
                Toast toast = Toast.makeText(ingredientsView.getContext(), "Bitte Anzahl auswählen", Toast.LENGTH_SHORT);
                toast.show();
                return false;
            }
            else
            {
                String regex = "[0-9]+";
                Pattern p = Pattern.compile(regex);

                Matcher m = p.matcher(amount.getText().toString());

                if(!m.matches())
                {
                    Toast toast = Toast.makeText(ingredientsView.getContext(), "Bitte nur Zahlen eingeben", Toast.LENGTH_SHORT);
                    toast.show();
                    return false;
                }
            }


            //check if spinner unit is set
            if(spinnerUnit.getSelectedItemPosition()==0)
            {
                Toast toast = Toast.makeText(ingredientsView.getContext(), "Bitte eine Einheit auswählen", Toast.LENGTH_SHORT);
                toast.show();
                return false;
            }

            ItemIngredients ingredient = new ItemIngredients();

            //get selected ingredients
            int posi = spinnerIngr.getSelectedItemPosition();
            ingredient.setIngredient(ingredients.get(posi));

            //get given amount
            ingredient.setAmount(amount.getText().toString());

            //get selected unit
            posi = spinnerUnit.getSelectedItemPosition();
            ingredient.setUnit(units.get(posi));

            ingredients_list.add(ingredient);
        }
        return  true;
    }
}
