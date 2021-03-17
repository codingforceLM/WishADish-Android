package de.codingforce.wad.fragment;

import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

import de.codeingforce.wad.R;
import de.codingforce.wad.activity.MainActivity;
import de.codingforce.wad.api.JsonPlaceHolderApi;
import de.codingforce.wad.item.Item_ingredients;
import de.codingforce.wad.item.Item_shoppinglists;
import de.codingforce.wad.item.Item_user;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends NameAwareFragment{
    private static final String LOG_TAG = "Login";
    private EditText username;
    private Button button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        MainActivity.main.change_title("Login");
        Log.e(LOG_TAG, "--onCreatedView--");
        return inflater.inflate(R.layout.fragment_login, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.e(LOG_TAG, "--onViewCreated--");

        username = view.findViewById(R.id.login_username);

        button = view.findViewById(R.id.Login_button);
        button.setOnClickListener(new Login.ButtonListener());
    }

    private class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Log.e(LOG_TAG, "--Button Clicked--");
            MainActivity.username = username.getText().toString();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(MainActivity.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

            Call<Item_user> call = jsonPlaceHolderApi.getUser(MainActivity.username);
            call.enqueue(new Callback<Item_user>() {
                @Override
                public void onResponse(Call<Item_user> call, Response<Item_user> response) {
                    //Check if user exists
                    if(!response.isSuccessful()) {
                        Toast toast = Toast.makeText(v.getContext(), "Code "+ response.code(), Toast.LENGTH_SHORT);
                        toast.show();
                        return;
                    }
                    //get user ID and save it
                    Item_user user = response.body();
                    MainActivity.userID = user.getUserId();
                    //go to Landing Page
                    Class Landing_Page = Landing_Page.class;
                    MainActivity.main. placeFragment(Landing_Page, R.id.mainFrame);
                }

                @Override
                public void onFailure(Call<Item_user> call, Throwable t) {
                    Log.e(LOG_TAG, t.getMessage());
                }
            });
        }
    }
}
