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
import de.codingforce.wad.fragment.Login;
import de.codingforce.wad.fragment.NameAwareFragment;
import de.codingforce.wad.item.ItemMessage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Register extends NameAwareFragment {

    private EditText firstnameText;
    private EditText nameText;
    private EditText usernameText;
    private EditText emailText;
    private EditText passwortText;
    private EditText birthdayText;
    private Button registerButton;
    private static final String LOG_TAG = "Register";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        Log.e(LOG_TAG, "--onCreatedView--");
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_register, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(LOG_TAG, "--onViewCreated--");
        MainActivity.main.change_title("WishADish");

        firstnameText = view.findViewById(R.id.register_firstname);
        nameText = view.findViewById(R.id.register_secondname);
        usernameText = view.findViewById(R.id.register_username);
        emailText = view.findViewById(R.id.register_email);
        passwortText = view.findViewById(R.id.register_passwort);
        birthdayText = view.findViewById(R.id.register_birthday);
        registerButton = view.findViewById(R.id.register_registerButton);

        registerButton.setOnClickListener(new Register.ButtonListener());

    }

    private class ButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(MainActivity.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

            Call<ItemMessage> call = jsonPlaceHolderApi.createUser(firstnameText.getText().toString(),nameText.getText().toString(),usernameText.getText().toString(),
                    emailText.getText().toString(),passwortText.getText().toString(),birthdayText.getText().toString());
            call.enqueue(new Callback<ItemMessage>() {
                @Override
                public void onResponse(Call<ItemMessage> call, Response<ItemMessage> response) {
                    if (!response.isSuccessful()) {
                        Toast toast = Toast.makeText(v.getContext(), "Code " + response.code(), Toast.LENGTH_SHORT);
                        toast.show();
                        return;
                    }
                    ItemMessage msg = response.body();
                    Toast toast = Toast.makeText(v.getContext(), msg.getMsg(), Toast.LENGTH_SHORT);
                    toast.show();

                    //Login
                    Class Login = de.codingforce.wad.fragment.Login.class;
                    MainActivity.main.placeFragment(Login, R.id.mainFrame);
                }
                @Override
                public void onFailure(Call<ItemMessage> call, Throwable t) {
                    Log.e(LOG_TAG, t.getMessage());
                }
            });
        }
    }
}
