package bsu.fpmi.pharmacy.pharmacy_mobile.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import bsu.fpmi.pharmacy.pharmacy_mobile.PharmacyApp;
import bsu.fpmi.pharmacy.pharmacy_mobile.R;
import bsu.fpmi.pharmacy.pharmacy_mobile.activities.CartActivity;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.PharmacyRESTService;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.entity.Basket;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.entity.Medicine;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.entity.User;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.service.BasketService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by annashunko on 25-Nov-16.
 */

public class CartAdapter extends BaseAdapter {
    CartActivity cartActivity;
    LayoutInflater lInflater;
    User user;

    List<Medicine> cartMedicines;

    public CartAdapter(CartActivity cartActivity, List<Medicine> cartMedicines, User user) {
        this.cartActivity = cartActivity;
        this.cartMedicines = cartMedicines;
        this.user = user;
        lInflater = (LayoutInflater) cartActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return cartMedicines.size();
    }

    @Override
    public Object getItem(int i) {
        return cartMedicines.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View cView = view;
        if (cView == null) {
            cView = lInflater.inflate(R.layout.item_cart, viewGroup, false);
        }
        final Medicine medicine = (Medicine) getItem(i);

        ((TextView) cView.findViewById(R.id.medicine_name)).setText(medicine.nameMedicine);
        ((TextView) cView.findViewById(R.id.price_textView)).setText(Double.toString(medicine.cost) + " $");

        ImageView imageView = (ImageView) cView.findViewById(R.id.photo_view);
        if (!TextUtils.isEmpty(medicine.imagePath))
            PharmacyApp.PICASSO.load(medicine.imagePath).into(imageView);

        ImageView removeFromCartImageView = (ImageView) cView.findViewById(R.id.remove_from_cart_imageView);
        removeFromCartImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeFromCart(medicine.idMedicine);


            }
        });
        return cView;
    }

    private void removeFromCart(int medicineId) {
        BasketService basketService = PharmacyRESTService.basketService();
        basketService.userRemoveFromBasket(user.id, medicineId).enqueue(new Callback<Basket>() {
            @Override
            public void onResponse(Call<Basket> call, Response<Basket> response) {
                if (response.body() != null) {
                    cartActivity.setCartMedicines(response.body().medicines);
                    cartActivity.setAdapter();
                    Toast.makeText(cartActivity, R.string.medicine_removed_cart, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Basket> call, Throwable t) {
                Toast.makeText(cartActivity, R.string.error_removing, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
