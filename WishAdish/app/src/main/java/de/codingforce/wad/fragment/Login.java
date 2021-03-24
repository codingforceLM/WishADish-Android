package de.codingforce.wad.fragment;

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
import de.codingforce.wad.fragment.add.Register;
import de.codingforce.wad.item.ItemLogin;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends NameAwareFragment{
    private static final String LOG_TAG = "Login";
    private EditText username;
    private EditText password;
    private Button button;
    private Button signUp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        MainActivity.main.change_title("Login");
        Log.e(LOG_TAG, "--onCreatedView--");
        return inflater.inflate(R.layout.fragment_login, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.e(LOG_TAG, "--onViewCreated--");

        username = view.findViewById(R.id.login_email);
        password = view.findViewById(R.id.login_passwort);

        button = view.findViewById(R.id.Login_button);
        button.setOnClickListener(new Login.ButtonListener());

        signUp = view.findViewById(R.id.signUp_button);
        signUp.setOnClickListener(new Login.ButtonListenerSignUp());
    }

    private class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Log.e(LOG_TAG, "--Button Clicked--");
            //MainActivity.username = username.getText().toString();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(MainActivity.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
            Call<ItemLogin> call = jsonPlaceHolderApi.createLogin(username.getText().toString(),password.getText().toString());
            call.enqueue(new Callback<ItemLogin>() {
                @Override
                public void onResponse(Call<ItemLogin> call, Response<ItemLogin> response) {
                    //Check if user exists
                    if(!response.isSuccessful()) {
                        Toast toast = Toast.makeText(v.getContext(), "Username or password is incorrect", Toast.LENGTH_SHORT);
                        toast.show();
                        return;
                    }
                    ItemLogin login = response.body();
                    MainActivity.username = login.getUsername();
                    MainActivity.userID = login.getUserId();
                    MainActivity.token = "Bearer " +login.getToken();

                    //go to Landing Page
                    Class Landing_Page = LandingPage.class;
                    MainActivity.main. placeFragment(Landing_Page, R.id.mainFrame);
                }

                @Override
                public void onFailure(Call<ItemLogin> call, Throwable t) {
                    Log.e(LOG_TAG, t.getMessage());
                }
            });
        }
    }
    private class ButtonListenerSignUp implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //go to Landing Page
            Class Register = de.codingforce.wad.fragment.add.Register.class;
            MainActivity.main. placeFragment(Register, R.id.mainFrame);
        }
    }
}
