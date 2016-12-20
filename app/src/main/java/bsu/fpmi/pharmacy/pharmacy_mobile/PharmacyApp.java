package bsu.fpmi.pharmacy.pharmacy_mobile;

import android.app.Application;

import com.squareup.picasso.Picasso;

public class PharmacyApp extends Application {
    public static Picasso PICASSO;

    @Override
    public void onCreate() {
        super.onCreate();
        PICASSO = PicassoCache.INSTANCE.getPicassoCache(this);
    }

}