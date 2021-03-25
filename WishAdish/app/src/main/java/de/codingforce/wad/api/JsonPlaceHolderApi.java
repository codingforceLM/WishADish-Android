package de.codingforce.wad.api;

import java.util.List;

import de.codingforce.wad.item.ItemDish;
import de.codingforce.wad.item.ItemDishIngredients;
import de.codingforce.wad.item.ItemGroups;
import de.codingforce.wad.item.ItemIngredient;
import de.codingforce.wad.item.ItemInvite;
import de.codingforce.wad.item.ItemLogin;
import de.codingforce.wad.item.ItemMessage;
import de.codingforce.wad.item.ItemShoppinglists;
import de.codingforce.wad.item.ItemUser;
import de.codingforce.wad.item.ItemWish;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface JsonPlaceHolderApi {
    //Shoppinglist
    @GET("list")
    Call<List<ItemShoppinglists>> getShoppinglists(@Header("Authorization")String token,@Header("userID") String userID);

    @GET("list/{listID}")
    Call<ItemShoppinglists> getShoppinglist(@Header("Authorization")String token,@Path("listID") String listID);

    @POST("list")
    Call<ItemMessage> createShoppinglist(@Header("Authorization")String token,@Header("name")String name, @Header("groupId") String groupID, @Header("userId") String userID);

    @PUT("list")
    Call<ItemMessage> changeShoppinglist(@Header("Authorization")String token,@Header("shoppinglist")String shoppinglist);

    //User
    @GET("user/{id}")
    Call<ItemUser> getUser(@Header("Authorization")String token,@Path("id")String id);

    @POST("user")
    Call<ItemMessage> createUser(@Header("firstname")String firstname,@Header("lastname")String lastname,@Header("username")String username,@Header("email")String email
    ,@Header("password")String password,@Header("birthday")String birthday);



    //Wish
    @GET("wish")
    Call<List<ItemWish>> getWish(@Header("Authorization")String token,@Header("userID")String userID, @Header("month") String month, @Header("day") String day,@Header("year")String year);

    @POST("wish")
    Call<ItemMessage> createWish(@Header("Authorization")String token,@Header("userId")String userID,@Header("groupId")String groupID,@Header("dishId")String dishID,@Header("daytime")String daytime,@Header("date")String date);


    //dishes
    @GET("dish")
    Call<List<ItemDish>> getDishes(@Header("Authorization")String token,@Header("userID")String userID);

    @GET("dish/{dishID}")
    Call<ItemDish> getDish(@Header("Authorization")String token,@Path("dishID")String dishID);

    @POST("dish")
    Call<ItemMessage> createDish(@Header("Authorization")String token,@Header("userId")String userID, @Header("name") String name, @Header("ingredients")String ingredients);


    //Ingredients
    @GET("ingrd")
    Call<List<ItemIngredient>> getIngredients(@Header("Authorization")String token,@Header("userID")String userID);

    @POST("ingrd")
    Call<ItemMessage> createIngredients(@Header("Authorization")String token,@Header("userID")String userID, @Header("name")String name);


    //group
    @GET("group")
    Call<List<ItemGroups>> getGroups(@Header("Authorization")String token,@Header("userID")String userID);

    @POST("group")
    Call<ItemMessage> createGroup(@Header("Authorization")String token,@Header("name") String name,@Header("userId")String userID);


    @GET("group/{groupID}")
    Call<ItemGroups> getGroup(@Header("Authorization")String token,@Path("groupID")String groupID);


    //login
    @POST("login")
    Call<ItemLogin> createLogin(@Header("email")String email, @Header("password")String password);



    //invite
    @GET("invite")
    Call<ItemInvite> getInvite(@Header("Authorization")String token,@Header("groupID") String groupID);
}
