package de.codingforce.wad.fragment;

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

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.codeingforce.wad.R;
import de.codingforce.wad.item.Item_ingredients;

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
    private ArrayList<Item_ingredients> ingredients_list = new ArrayList<>();


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
        ingredients.add("Zutat auswählen");
        ingredients.add("ing A");
        ingredients.add("ing B");
        ingredients.add("ing C");
        ingredients.add("ing D");

        units = new ArrayList<>();
        units.add("Einheit auswählen");
        units.add("kg");
        units.add("l");
        units.add("stück");

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
                //get Name
                Log.e(LOG_TAG, dish_name.getText().toString());

                for(Item_ingredients i : ingredients_list)
                {
                    Log.e(LOG_TAG, "Ingredients :" + i.getIngredient() + "Amount :"+ i.getAmount() +"Unit :" + i.getUnit());
                }
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

            Item_ingredients ingredient = new Item_ingredients();

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
