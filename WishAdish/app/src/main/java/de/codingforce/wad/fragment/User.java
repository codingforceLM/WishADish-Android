package de.codingforce.wad.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;

import de.codeingforce.wad.R;
import de.codingforce.wad.activity.MainActivity;
import de.codingforce.wad.api.JsonPlaceHolderApi;
import de.codingforce.wad.fragment.NameAwareFragment;
import de.codingforce.wad.item.ItemGroups;
import de.codingforce.wad.item.ItemUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class User extends NameAwareFragment {
    private static final String LOG_TAG = "User";


    private ImageView image;
    private TextView textName;
    private TextView textBorn;
    private TextView textEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        Log.e(LOG_TAG, "--onCreatedView--");
        return inflater.inflate(R.layout.fragment_user, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(LOG_TAG, "--onViewCreated--");
        MainActivity.main.change_title("Profil von : " + MainActivity.username);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<ItemUser> call = jsonPlaceHolderApi.getUser(MainActivity.token,MainActivity.userID);
        call.enqueue(new Callback<ItemUser>() {
            @Override
            public void onResponse(Call<ItemUser> call, Response<ItemUser> response) {
                if(!response.isSuccessful()) {
                    Toast toast = Toast.makeText(view.getContext(), "Code "+ response.code(), Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                ItemUser user = response.body();

                textName = view.findViewById(R.id.user_name);
                textBorn = view.findViewById(R.id.user_date);
                textEmail = view.findViewById(R.id.user_email);

                if(!user.getFileurl().equals("")) {
                    // show The Image in a ImageView
                    new DownloadImageTask((ImageView) view.findViewById(R.id.user_image))
                            .execute(user.getFileurl());
                }else {
                    new DownloadImageTask((ImageView) view.findViewById(R.id.user_image))
                            .execute("https://supporthubstaffcom.lightningbasecdn.com/wp-content/uploads/2019/08/good-pic.png");
                }

                textName.setText(user.getFirstname() + " " + user.getLastname());
                textBorn.setText(user.getBirthdate());
                textEmail.setText(user.getEmail());
            }

            @Override
            public void onFailure(Call<ItemUser> call, Throwable t) {
                Log.e(LOG_TAG, t.getMessage());
            }
        });
    }

    //Load image
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            InputStream in = null;
            try {
                in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            finally {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
