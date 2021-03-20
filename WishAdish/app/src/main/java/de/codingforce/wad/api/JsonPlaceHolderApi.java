package de.codingforce.wad.api;

import java.util.List;

import de.codingforce.wad.item.ItemDish;
import de.codingforce.wad.item.ItemDishIngredients;
import de.codingforce.wad.item.ItemGroups;
import de.codingforce.wad.item.ItemIngredient;
import de.codingforce.wad.item.ItemMessage;
import de.codingforce.wad.item.ItemShoppinglists;
import de.codingforce.wad.item.ItemUser;
import de.codingforce.wad.item.ItemWish;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface JsonPlaceHolderApi {
    //Shoppinglist
    @GET("list")
    Call<List<ItemShoppinglists>> getShoppinglists(@Header("userID") String userID);

    @GET("list/{listID}")
    Call<ItemShoppinglists> getShoppinglist(@Path("listID") String listID);


    //User
    @GET("user/{username}")
    Call<ItemUser> getUser(@Path("username")String username);


    //Wish
    @GET("wish")
    Call<List<ItemWish>> getWish(@Header("userID")String userID, @Header("month") String month, @Header("day") String day);


    //dishes
    @GET("dish")
    Call<List<ItemDish>> getDishes(@Header("userID")String userID);

    @GET("dish/{dishID}")
    Call<ItemDish> getDish(@Path("dishID")String dishID);

    @POST("dish")
    Call<ItemMessage> createDish(@Header("userId")String userID, @Header("name") String name, @Header("ingredients")String ingredients);


    //Ingredients
    @GET("ingrd")
    Call<List<ItemIngredient>> getIngredients(@Header("userID")String userID);

    @POST("ingrd")
    Call<ItemMessage> createIngredients(@Header("userID")String userID, @Header("name")String name);


    //group
    @GET("group")
    Call<List<ItemGroups>> getGroups(@Header("userID")String userID);

    @POST("group")
    Call<ItemMessage> createGroup(@Header("name") String name,@Header("userId")String userID);


    @GET("group/{groupID}")
    Call<ItemGroups> getGroup(@Path("groupID")String groupID);
}
