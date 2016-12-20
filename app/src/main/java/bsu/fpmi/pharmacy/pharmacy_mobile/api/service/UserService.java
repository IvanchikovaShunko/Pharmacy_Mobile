package bsu.fpmi.pharmacy.pharmacy_mobile.api.service;

import bsu.fpmi.pharmacy.pharmacy_mobile.api.entity.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by annashunko
 */

public interface UserService {
    @GET("user/{id}")
    Call<User> user(
            @Path("id") int id
    );

    @POST("user/add")
    Call<User> userAdd(
            @Query("login") String login,
            @Query("password") String password,
            @Query("name") String name,
            @Query("age") int age,
            @Query("gender") String gender,
            @Query("about") String about,
            @Query("home_address") String homeAddress,
            @Query("email") String email,
            @Query("telephone") String telephone
    );

    @POST("user/auth")
    Call<User> userAuth(
            @Query("login") String login,
            @Query("password") String password
    );
}
