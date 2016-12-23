package bsu.fpmi.pharmacy.pharmacy_mobile.activities;

import android.app.ProgressDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import bsu.fpmi.pharmacy.pharmacy_mobile.R;
import bsu.fpmi.pharmacy.pharmacy_mobile.adapters.CartAdapter;
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
    private SwipeRefreshLayout mSwipeLayout;


    @Override
    protected void initActivityGUI() {
        listView = (ListView) findViewById(R.id.list_view_cart);

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();

        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mSwipeLayout.setColorSchemeResources(R.color.colorPrimaryDark);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeLayout.setRefreshing(false);

            }
        });

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
                    Toast.makeText(getApplicationContext(), R.string.enable_load_cart, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Basket> call, Throwable t) {
                progressDialog.hide();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void setAdapter() {
        cartAdapter = new CartAdapter(this, cartMedicines, user);
        listView.setAdapter(cartAdapter);
    }

    public List<Medicine> getCartMedicines() {
        return cartMedicines;
    }

    public void setCartMedicines(List<Medicine> cartMedicines) {
        this.cartMedicines = cartMedicines;
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
