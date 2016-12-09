package bsu.fpmi.pharmacy.pharmacy_mobile.api.service;

import java.util.List;

import bsu.fpmi.pharmacy.pharmacy_mobile.api.entity.Medicine;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.entity.MedicineResult;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.entity.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by annashunko
 */

public interface MedicineService {
    @GET("medicine/{id}")
    Call<Medicine> medicineById(
            @Path("id") int id
    );

    @GET("medicine/list")
    Call<List<Medicine>> medicineList();


    @POST("medicine/add")
    Call<Medicine> medicineAdd(
            @Query(value = "name") String name,
            @Query(value = "about") String about,
            @Query(value = "gram_in_one") double gramInOne,
            @Query(value = "cost") double cost,
            @Query(value = "quantity") int quantity,
            @Query(value = "consist") String consist,
            @Query(value = "state") String state,
            @Query(value = "dosing") String dosing,
            @Query(value = "contradictions") String contradictions,
            @Query(value = "expiration_date") String expireDate
    );

    @POST("medicine/delete/{id}")
    Call<Medicine> medicineDelete(
            @Path("id") int id
    );

}
