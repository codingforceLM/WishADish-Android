package de.codingforce.wad.api;

import java.util.List;

import de.codingforce.wad.activity.MainActivity;
import de.codingforce.wad.item.Item_shoppinglists;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;

public interface JsonPlaceHolderApi {
    @GET("list")
    Call<List<Item_shoppinglists>> getShoppinglists(@Header("groupID") String groupID);
}
