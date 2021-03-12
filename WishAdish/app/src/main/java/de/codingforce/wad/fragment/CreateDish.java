package de.codingforce.wad.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import de.codeingforce.wad.R;

public class CreateDish extends NameAwareFragment {
    private static final String LOG_TAG = "CreatDishes";

    private EditText dish_name;
    private Button button;
    private Spinner spinner_ingredients;
    private ArrayAdapter adapter_ingredients;
    private Spinner spinner_unit;
    private ArrayAdapter adapter_unit;
    private EditText amount;

    private ArrayList<String> ingredients;
    private ArrayList<String> units;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        Log.e(LOG_TAG, "--onCreatedView--");
        return inflater.inflate(R.layout.fragment_creat_dish, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(LOG_TAG, "--onViewCreated--");

        //dish name
        dish_name = view.findViewById(R.id.dish_name_plainText);

        //get Data
        ingredients = new ArrayList<>();
        ingredients.add("ing A");
        ingredients.add("ing B");
        ingredients.add("ing C");
        ingredients.add("ing D");

        units = new ArrayList<>();
        units.add("kg");
        units.add("l");
        units.add("stück");

        //spinner ingredients
        spinner_ingredients = view.findViewById(R.id.ingredients_spinner);
        adapter_ingredients = new ArrayAdapter(view.getContext(), android.R.layout.simple_spinner_item, ingredients);
        adapter_ingredients.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_ingredients.setAdapter(adapter_ingredients);

        //spinner unit
        spinner_unit = view.findViewById(R.id.unit_spinner);
        adapter_unit = new ArrayAdapter(view.getContext(), android.R.layout.simple_spinner_item, units);
        adapter_unit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_unit.setAdapter(adapter_unit);

        //amount
        amount = view.findViewById(R.id.amount_plainText);



        //creat Button
        button = view.findViewById(R.id.button2);
        button.setOnClickListener(new ButtonListener());
    }

    private class ButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Log.e(LOG_TAG, "--Button Clicked--");
            //get Name
            Log.e(LOG_TAG, dish_name.getText().toString());
            //get spinner values

            //ingredients
            int posi = spinner_ingredients.getSelectedItemPosition();
            Log.e(LOG_TAG, ingredients.get(posi));

            //get amount
            Log.e(LOG_TAG, amount.getText().toString());

            //unit
            int posi2 = spinner_unit.getSelectedItemPosition();
            Log.e(LOG_TAG, units.get(posi2));


        }
    }
}
