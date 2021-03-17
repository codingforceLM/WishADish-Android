package de.codingforce.wad.api;

import java.util.List;

import de.codingforce.wad.activity.MainActivity;
import de.codingforce.wad.item.Item_shoppinglists;
import de.codingforce.wad.item.Item_shoppinglists_ingredients;
import de.codingforce.wad.item.Item_user;
import de.codingforce.wad.item.Item_wish;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface JsonPlaceHolderApi {
    //Shoppinglist
    @GET("list")
    Call<List<Item_shoppinglists>> getShoppinglists(@Header("userID") String userID);

    @GET("list/{listID}")
    Call<Item_shoppinglists> getShoppinglist(@Path("listID") String listID);


    //User
    @GET("user/{username}")
    Call<Item_user> getUser(@Path("username")String username);


    //Wish
    @GET("wish")
    Call<List<Item_wish>> getWish(@Header("userID")String userID, @Header("month") String month, @Header("day") String day);
}
