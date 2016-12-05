package bsu.fpmi.pharmacy.pharmacy_mobile.api;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import bsu.fpmi.pharmacy.pharmacy_mobile.api.service.MedicineService;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.service.UserService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by annashunko
 */

public class PharmacyRESTService {
    public static final String API_URL = "https://pharmacyrestservice.herokuapp.com/";
    private static Retrofit retrofit;

    protected static Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create(getGsonBuilder().create()))
                    .build();
        }
        return retrofit;
    }



    public static GsonBuilder getGsonBuilder() {
        String datePattern = "yyyy-MM-dd";
        final SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);

        GsonBuilder builder = new GsonBuilder();


        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            @Override
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                try {
                    return dateFormat.parse(json.getAsString());
                } catch (ParseException e) {
                    return null;
                }
            }
        });

        return builder;
    }

    public static MedicineService medicineService() {
        return getRetrofit().create(MedicineService.class);
    }

    public static UserService userService() {
        return getRetrofit().create(UserService.class);
    }

}
