package bsu.fpmi.pharmacy.pharmacy_mobile.api.service;

import bsu.fpmi.pharmacy.pharmacy_mobile.api.entity.Basket;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by annashunko
 */

public interface BasketService {
    @GET("/user/{userId}/basket")
    Call<Basket> userBasket(
            @Path("id") int id
    );

    @POST("/user/{userId}/basket/add")
    Call<Basket> userAddToBasket(
            @Path("id") int id,
            @Query("medicine_id") int medicineId
    );

    @POST("/user/{userId}/basket/remove")
    Call<Basket> userRemoveFromBasket(
            @Path("id") int id,
            @Query("medicine_id") int medicineId
    );
}
