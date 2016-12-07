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

}
