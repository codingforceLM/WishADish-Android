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


    private String[] groups = {"Group A", "Group B", "Group C"};
    private String[] dishes = {"Dish A", "Dish B", "Dish C"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        Log.e(LOG_TAG, "--onCreatedView--");
        return inflater.inflate(R.layout.fragment_creat_wish, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.e(LOG_TAG, "--onViewCreated--");

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
            Log.e(LOG_TAG,groups[posi]);
            //dish
            int posi2 = spinner_dish.getSelectedItemPosition();
            Log.e(LOG_TAG,groups[posi2]);

            //time
            int radioId = radioGroup.getCheckedRadioButtonId();
            radioButton = v.findViewById(radioId);
            Log.e(LOG_TAG, String.valueOf(radioId));
        }
    }
}
