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

import java.util.ArrayList;
import java.util.List;

import de.codeingforce.wad.R;
import de.codingforce.wad.activity.MainActivity;
import de.codingforce.wad.api.JsonPlaceHolderApi;
import de.codingforce.wad.fragment.Ingredients;
import de.codingforce.wad.fragment.NameAwareFragment;
import de.codingforce.wad.fragment.Shoppinglists;
import de.codingforce.wad.item.ItemGroup;
import de.codingforce.wad.item.ItemGroups;
import de.codingforce.wad.item.ItemMessage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddShoppinglist extends NameAwareFragment {
    private static final String LOG_TAG = "Add Shoppinglist";
    private Button button;
    private EditText editText;
    private Spinner spinner;
    private ArrayAdapter adapter;

    //lists for spinner
    private ArrayList<String> group_name = new ArrayList<>();

    private List<ItemGroups> groups;
    private String groupID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        Log.e(LOG_TAG, "--onCreatedView--");
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_add_shoppinglist, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(LOG_TAG, "--onViewCreated--");
        MainActivity.main.change_title("Einkaufsliste erstellen");
        group_name.clear();

        button = view.findViewById(R.id.add_shoppinglist_button);
        button.setOnClickListener(new AddShoppinglist.ButtonListener());

        editText = view.findViewById(R.id.shoppinglist_Name);

        spinner = view.findViewById(R.id.spinner_shoppinglist_group);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<List<ItemGroups>> call = jsonPlaceHolderApi.getGroups(MainActivity.userID);
        call.enqueue(new Callback<List<ItemGroups>>() {
            @Override
            public void onResponse(Call<List<ItemGroups>> call, Response<List<ItemGroups>> response) {
                if(!response.isSuccessful()) {
                    Toast toast = Toast.makeText(view.getContext(), "Code "+ response.code(), Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                groups = response.body();
                for(ItemGroups group : groups){
                    group_name.add(group.getTitle());
                }

                adapter = new ArrayAdapter(view.getContext(), android.R.layout.simple_spinner_item, group_name);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinner.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<ItemGroups>> call, Throwable t) {
                Log.e(LOG_TAG, t.getMessage());
            }
        });

    }

    private class ButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Log.e(LOG_TAG, "Button clicked");

            int posi = spinner.getSelectedItemPosition();
            String gruppe = group_name.get(posi);
            for(ItemGroups group : groups){
                if(group.getTitle().equals(gruppe))
                {
                    groupID = group.getId();
                    break;
                }
            }
            String name = editText.getText().toString();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(MainActivity.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

            Call<ItemMessage> call = jsonPlaceHolderApi.createShoppinglist(name, groupID ,MainActivity.userID);
            call.enqueue(new Callback<ItemMessage>() {
                @Override
                public void onResponse(Call<ItemMessage> call, Response<ItemMessage> response) {
                    if (!response.isSuccessful()) {
                        Toast toast = Toast.makeText(v.getContext(), "Code " + response.code(), Toast.LENGTH_SHORT);
                        toast.show();
                        return;
                    }
                    if (response.code() != 200) {
                        Toast toast = Toast.makeText(v.getContext(), "Fehler " + response.code() + "daten konnten nicht gespeichert werden", Toast.LENGTH_SHORT);
                        toast.show();
                        return;
                    }

                    //Ingredients
                    Class Shoppinglists = de.codingforce.wad.fragment.Shoppinglists.class;
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
