package de.codingforce.wad.fragment.add;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import de.codeingforce.wad.R;
import de.codingforce.wad.activity.MainActivity;
import de.codingforce.wad.api.JsonPlaceHolderApi;
import de.codingforce.wad.fragment.Groups;
import de.codingforce.wad.fragment.Ingredients;
import de.codingforce.wad.fragment.NameAwareFragment;
import de.codingforce.wad.item.ItemMessage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddGroup extends NameAwareFragment {
    private static final String LOG_TAG = "Add Group";
    private Button button;
    private EditText editText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        Log.e(LOG_TAG, "--onCreatedView--");
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_add_group, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(LOG_TAG, "--onViewCreated--");
        MainActivity.main.change_title("Gruppe erstellen");

        button = view.findViewById(R.id.add_group_button);
        button.setOnClickListener(new AddGroup.ButtonListener());

        editText = view.findViewById(R.id.group_Name);

    }

    private class ButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (editText.getText().toString().equals("") || editText.getText().toString().equals("Name")) {
                Toast toast = Toast.makeText(v.getContext(), "Bitte Name eingeben", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(MainActivity.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

            Call<ItemMessage> call = jsonPlaceHolderApi.createGroup(MainActivity.token,editText.getText().toString(),MainActivity.userID);
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
                    Toast toast = Toast.makeText(v.getContext(), "Gruppe : " + editText.getText() + " erstellt", Toast.LENGTH_SHORT);
                    toast.show();

                    //Group
                    Class Groups = de.codingforce.wad.fragment.Groups.class;
                    MainActivity.main.placeFragment(Groups, R.id.mainFrame);
                }

                @Override
                public void onFailure(Call<ItemMessage> call, Throwable t) {
                    Log.e(LOG_TAG, t.getMessage());
                }
            });
        }
    }
}
