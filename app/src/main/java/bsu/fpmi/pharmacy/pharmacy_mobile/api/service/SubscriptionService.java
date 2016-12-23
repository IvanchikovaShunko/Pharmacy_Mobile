package bsu.fpmi.pharmacy.pharmacy_mobile.api.service;

import java.util.List;

import bsu.fpmi.pharmacy.pharmacy_mobile.api.entity.Basket;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.entity.Subscription;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by annashunko
 */

public interface SubscriptionService {
    @GET("/user/{userId}/subscriptions")
    Call<List<Subscription>> userSubscriptions(
            @Path("userId") int id
    );

    @POST("/user/{userId}/subscriptions/add")
    Call<Integer> subscribe(
            @Path("userId") int id,
            @Query(value = "medicine_id") int medicineId,
            @Query(value = "period") String period
    );

    @POST("/user/{userId}/subscriptions/remove/{id}")
    Call<Integer> unsubscribe(
            @Path(value = "userId") int userId,
            @Path(value = "id") int id
    );
}
