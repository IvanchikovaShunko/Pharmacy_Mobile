package bsu.fpmi.pharmacy.pharmacy_mobile.activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import bsu.fpmi.pharmacy.pharmacy_mobile.R;
import bsu.fpmi.pharmacy.pharmacy_mobile.adapters.CartAdapter;
import bsu.fpmi.pharmacy.pharmacy_mobile.adapters.InstructionsAdapter;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.PharmacyRESTService;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.entity.Basket;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.entity.Medicine;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.service.BasketService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends BaseNavDrawerActivity {
    private ListView listView;
    private CartAdapter cartAdapter;
    private ProgressDialog progressDialog;

    Basket basket;
    List<Medicine> cartMedicines = new ArrayList<>();


    @Override
    protected void initActivityGUI() {
        listView = (ListView) findViewById(R.id.list_view_cart);

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        BasketService basketService = PharmacyRESTService.basketService();
        basketService.userBasket(user.id).enqueue(new Callback<Basket>() {
            @Override
            public void onResponse(Call<Basket> call, Response<Basket> response) {
                progressDialog.hide();
                basket = response.body();
                if (basket != null) {
                    cartMedicines = basket.medicines;
                    setAdapter();
                } else {
                    Toast.makeText(getApplicationContext(), "Enable to load cart", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Basket> call, Throwable t) {
                progressDialog.hide();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setAdapter() {
        cartAdapter = new CartAdapter(this, cartMedicines, user);
        listView.setAdapter(cartAdapter);
    }
    @Override
    protected int getContentView() {
        return R.layout.activity_cart;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
