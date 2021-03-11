package de.codingforce.wad.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.util.ArrayList;

import de.codeingforce.wad.R;

public class CreatWish extends NameAwareFragment{
    private static final String LOG_TAG = "Create Wish";

    private Spinner spinner_group;
    private ArrayAdapter adapter_group;
    private Spinner spinner_dish;
    private ArrayAdapter adapter_dish;
    private Button button;
    private RadioGroup radioGroup;
    private RadioButton radioButton;

    private ArrayList<String> groups;
    private ArrayList<String> dishes;

    private String group;
    private String dish;
    private String time;
    private String wish;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        Log.e(LOG_TAG, "--onCreatedView--");
        return inflater.inflate(R.layout.fragment_creat_wish, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.e(LOG_TAG, "--onViewCreated--");

        groups = new ArrayList<>();
        groups.add("Group A");
        groups.add("Group B");
        groups.add("Group C");
        groups.add("Group D");

        dishes = new ArrayList<>();
        dishes.add("Dish A");
        dishes.add("Dish B");
        dishes.add("Dish C");


        //Spinner for Group
        spinner_group = view.findViewById(R.id.spinner_group);

        adapter_group = new ArrayAdapter(view.getContext(), android.R.layout.simple_spinner_item, groups);
        adapter_group.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_group.setAdapter(adapter_group);

        //Spinner for Dishes
        spinner_dish = view.findViewById(R.id.spinner_dish);

        adapter_dish = new ArrayAdapter(view.getContext(), android.R.layout.simple_spinner_item, dishes);
        adapter_dish.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_dish.setAdapter(adapter_dish);

        //create Radio Buttons
        radioGroup = view.findViewById(R.id.radioGroup);


        //Create Button
        button = view.findViewById(R.id.button);
        button.setOnClickListener(new CreatWish.ButtonListener());

    }

    private class ButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Log.e(LOG_TAG, "--Button Clicked--");
            //groups
            int posi = spinner_group.getSelectedItemPosition();
            group = groups.get((posi));
            //dish
            int posi2 = spinner_dish.getSelectedItemPosition();
            dish = dishes.get(posi2);

            //time
            switch (radioGroup.getCheckedRadioButtonId()) {
                case R.id.radio_morning :
                    time = "morgens";
                    break;
                case R.id.radio_day :
                    time = "mittags";
                    break;
                case R.id.radio_evening:
                    time = "abends";
                    break;

            }
            wish = "Gruppe :"+ group + "\tGericht :"+ dish + "\tZeit:" +time;
            Log.e(LOG_TAG, wish);
        }
    }
}
