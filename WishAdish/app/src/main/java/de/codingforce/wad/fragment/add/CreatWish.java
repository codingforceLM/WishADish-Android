package de.codingforce.wad.fragment.add;

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
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import de.codeingforce.wad.R;
import de.codingforce.wad.activity.MainActivity;
import de.codingforce.wad.api.JsonPlaceHolderApi;
import de.codingforce.wad.fragment.NameAwareFragment;
import de.codingforce.wad.fragment.WishesFromDate;
import de.codingforce.wad.item.ItemDish;
import de.codingforce.wad.item.ItemGroups;
import de.codingforce.wad.item.ItemMessage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreatWish extends NameAwareFragment {
    private static final String LOG_TAG = "Create Wish";

    private Spinner spinner_group;
    private ArrayAdapter adapter_group;
    private Spinner spinner_dish;
    private ArrayAdapter adapter_dish;
    private Button button;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private TextView textView;

    private ArrayList<String> groups;
    private List<ItemGroups>groupsList;
    private ArrayList<String> dishes;
    private List<ItemDish> dishesList;

    private String group;
    private String dish;
    private String time;
    private String wish;

    private String month;
    private String day;
    private String year;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        Log.e(LOG_TAG, "--onCreatedView--");
        return inflater.inflate(R.layout.fragment_creat_wish, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.e(LOG_TAG, "--onViewCreated--");

        MainActivity.main.change_title("Gericht wünschen");

        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(MainActivity.tag);
        if(calendar.get(java.util.Calendar.MONTH) + 1 < 10)
        {
            month ="0"+Integer.toString(calendar.get(java.util.Calendar.MONTH) + 1);
        }else{
            month = Integer.toString(calendar.get(java.util.Calendar.MONTH) + 1);
        }
        day = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
        year = Integer.toString(calendar.get(Calendar.YEAR)- 1900);

        textView = view.findViewById(R.id.textview_day);
        textView.setText("Tag "+ day + "."+month+"."+year);

        //Spinner for Group
        spinner_group = view.findViewById(R.id.spinner_group);

        //get groups
        groups = new ArrayList<>();
        groups.add("Gruppe auswählen");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<List<ItemGroups>> call = jsonPlaceHolderApi.getGroups(MainActivity.token,MainActivity.userID);
        call.enqueue(new Callback<List<ItemGroups>>() {
            @Override
            public void onResponse(Call<List<ItemGroups>> call, Response<List<ItemGroups>> response) {
                if(!response.isSuccessful()) {
                    Toast toast = Toast.makeText(view.getContext(), "Code "+ response.code(), Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                groupsList = response.body();
                for(ItemGroups group : groupsList){
                    groups.add(group.getTitle());
                }

                adapter_group = new ArrayAdapter(view.getContext(), android.R.layout.simple_spinner_item, groups);
                adapter_group.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinner_group.setAdapter(adapter_group);
            }

            @Override
            public void onFailure(Call<List<ItemGroups>> call, Throwable t) {
                Log.e(LOG_TAG, t.getMessage());
            }
        });

        //Spinner for Dishes
        spinner_dish = view.findViewById(R.id.spinner_dish);

        //get dishes
        dishes = new ArrayList<>();
        dishes.add("Gericht auswählen");

        Call<List<ItemDish>> call2 = jsonPlaceHolderApi.getDishes(MainActivity.token,MainActivity.userID);
        call2.enqueue(new Callback<List<ItemDish>>() {
            @Override
            public void onResponse(Call<List<ItemDish>> call, Response<List<ItemDish>> response) {
                if(!response.isSuccessful()) {
                    Toast toast = Toast.makeText(view.getContext(), "Code "+ response.code(), Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                dishesList = response.body();

                for(ItemDish dish : dishesList){
                    dishes.add(dish.getName());
                }

                adapter_dish = new ArrayAdapter(view.getContext(), android.R.layout.simple_spinner_item, dishes);
                adapter_dish.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinner_dish.setAdapter(adapter_dish);
            }

            @Override
            public void onFailure(Call<List<ItemDish>> call, Throwable t) {
                Log.e(LOG_TAG, t.getMessage());
            }
        });

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
            int posi2 = spinner_dish.getSelectedItemPosition();
            if(posi == 0)
            {
                Toast toast = Toast.makeText(v.getContext(), "Bitte eine Gruppe auswählen", Toast.LENGTH_SHORT);
                toast.show();
            }else if (posi2 == 0) {
                Toast toast = Toast.makeText(v.getContext(), "Bitte ein Gericht auswählen", Toast.LENGTH_SHORT);
                toast.show();
            }else {
                group = groups.get((posi));

                dish = dishes.get(posi2);

                //time
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.radio_morning:
                        time = "morning";
                        break;
                    case R.id.radio_day:
                        time = "lunch";
                        break;
                    case R.id.radio_evening:
                        time = "evening";
                        break;

                }
                String groupID="";
                for(ItemGroups gruppe : groupsList){
                    if(gruppe.getTitle().equals(group))
                    {
                        groupID = gruppe.getId();
                        break;
                    }
                }


                String dishID="";
                for(ItemDish dishes: dishesList){
                    if(dishes.getName().equals(dish))
                    {
                        dishID = dishes.getId();
                        break;
                    }
                }


                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(MainActivity.URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

                Log.e(LOG_TAG,"UserID : "+MainActivity.userID + " GroupID : "+ groupID + " dishID : "+ dishID + " Time : " + time + " date : " + year+"-"+month+"-"+day);

                Call<ItemMessage> call = jsonPlaceHolderApi.createWish(MainActivity.token,MainActivity.userID,groupID,dishID,time,year+"-"+month+"-"+day);
                call.enqueue(new Callback<ItemMessage>() {
                    @Override
                    public void onResponse(Call<ItemMessage> call, Response<ItemMessage> response) {
                        if(!response.isSuccessful()) {
                            Toast toast = Toast.makeText(v.getContext(), "Code "+ response.code(), Toast.LENGTH_SHORT);
                            toast.show();
                            return;
                        }
                        ItemMessage message = response.body();
                        Toast toast = Toast.makeText(v.getContext(), message.getMsg(), Toast.LENGTH_SHORT);
                        toast.show();

                        //Wishes
                        Class WishesFromDate = de.codingforce.wad.fragment.WishesFromDate.class;
                        MainActivity.main.placeFragment(WishesFromDate, R.id.mainFrame);
                    }
                    @Override
                    public void onFailure(Call<ItemMessage> call, Throwable t) {
                        Log.e(LOG_TAG, t.getMessage());
                    }
                });
            }
        }
    }
}
