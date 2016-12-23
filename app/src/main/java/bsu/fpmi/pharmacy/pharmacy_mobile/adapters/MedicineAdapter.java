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
import bsu.fpmi.pharmacy.pharmacy_mobile.api.PharmacyRESTService;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.entity.Basket;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.entity.Medicine;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.entity.User;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.service.BasketService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MedicineAdapter extends BaseAdapter {
    Context context;
    LayoutInflater lInflater;
    User user;

    List<Medicine> medicines;

    public MedicineAdapter(Context context, List<Medicine> medicines, User user) {
        this.context = context;
        this.medicines = medicines;
        this.user = user;
        lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return medicines.size();
    }

    @Override
    public Object getItem(int i) {
        return medicines.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View cView = view;
        if (cView == null) {
            cView = lInflater.inflate(R.layout.item_medicine, viewGroup, false);
        }
        final Medicine medicine = (Medicine) getItem(i);

        ((TextView) cView.findViewById(R.id.medicine_name)).setText(medicine.nameMedicine);
        ((TextView) cView.findViewById(R.id.medicine_state)).setText(medicine.state + ", " + medicine.cost + " $");

        ImageView imageView = (ImageView) cView.findViewById(R.id.photo_view);
        if (!TextUtils.isEmpty(medicine.imagePath))
            PharmacyApp.PICASSO.load(medicine.imagePath).into(imageView);

        ImageView addToCartImageView = (ImageView) cView.findViewById(R.id.add_to_cart_imageView);
        ImageView subscribeImageView = (ImageView) cView.findViewById(R.id.subscribe_imageView);
        if (user == null) {
            addToCartImageView.setVisibility(View.INVISIBLE);
            subscribeImageView.setVisibility(View.INVISIBLE);
        }
        addToCartImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    addMedicineToCart(medicine.idMedicine);
            }
        });

        return cView;
    }

    private void addMedicineToCart(int medicineId) {
        BasketService basketService = PharmacyRESTService.basketService();
        basketService.userAddToBasket(user.id, medicineId).enqueue(new Callback<Basket>() {
            @Override
            public void onResponse(Call<Basket> call, Response<Basket> response) {
                Toast.makeText(context, R.string.medicine_added_cart, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Basket> call, Throwable t) {
                Toast.makeText(context, R.string.medicine_in_cart, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
