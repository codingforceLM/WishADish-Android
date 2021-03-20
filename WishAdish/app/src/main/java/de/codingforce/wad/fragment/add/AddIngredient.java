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
import de.codingforce.wad.fragment.Ingredients;
import de.codingforce.wad.fragment.NameAwareFragment;
import de.codingforce.wad.item.ItemMessage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddIngredient extends NameAwareFragment {

    private static final String LOG_TAG = "Add Ingredient";
    private Button button;
    private EditText editText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        Log.e(LOG_TAG, "--onCreatedView--");
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_add_ingr, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(LOG_TAG, "--onViewCreated--");
        MainActivity.main.change_title("Zutat erstellen");

        button = view.findViewById(R.id.add_ingr_button);
        button.setOnClickListener(new AddIngredient.ButtonListener());

        editText = view.findViewById(R.id.ingr_Name);

    }

    private class ButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (editText.getText().toString() == "") {
                Toast toast = Toast.makeText(v.getContext(), "Bitte Name eingeben", Toast.LENGTH_SHORT);
                toast.show();
                return;
                }
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(MainActivity.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

            Call<ItemMessage> call = jsonPlaceHolderApi.createIngredients(MainActivity.userID, editText.getText().toString());
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
                    Toast toast = Toast.makeText(v.getContext(), "Zutat : " + editText.getText() + " hinzugefügt", Toast.LENGTH_SHORT);
                    toast.show();

                    //Ingredients
                    Class Ingredients = Ingredients.class;
                    MainActivity.main.placeFragment(Ingredients, R.id.mainFrame);
                }

                @Override
                public void onFailure(Call<ItemMessage> call, Throwable t) {
                    Log.e(LOG_TAG, t.getMessage());
                }
            });
        }
    }
}
