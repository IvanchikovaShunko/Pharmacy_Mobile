package bsu.fpmi.pharmacy.pharmacy_mobile.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
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

import static bsu.fpmi.pharmacy.pharmacy_mobile.R.drawable.i;

public class CartActivity extends BaseNavDrawerActivity {
    private ListView listView;
    private CartAdapter cartAdapter;
    private ProgressDialog progressDialog;
    private FloatingActionButton floatingActionButton;
    private RelativeLayout emptyView;


    Basket basket;
    List<Medicine> cartMedicines = new ArrayList<>();
    private SwipeRefreshLayout mSwipeLayout;


    @Override
    protected void initActivityGUI() {
        listView = (ListView) findViewById(R.id.list_view_cart);
        emptyView = (RelativeLayout) findViewById(R.id.empty_layout);
        emptyView.setVisibility(View.GONE);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeOrder();
            }
        });
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();

        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mSwipeLayout.setColorSchemeResources(R.color.colorPrimaryDark);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                BasketService basketService = PharmacyRESTService.basketService();
                basketService.userBasket(user.id).enqueue(new Callback<Basket>() {
                    @Override
                    public void onResponse(Call<Basket> call, Response<Basket> response) {
                        mSwipeLayout.setRefreshing(false);
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
                        mSwipeLayout.setRefreshing(false);
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

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

    private void makeOrder() {
        StringBuilder sb = new StringBuilder();
        double total = 0;
        for (Medicine m: cartMedicines){
            sb.append(m.nameMedicine + "  -  " + m.cost + "$\n");
            total += m.cost;
        }
        new AlertDialog.Builder(CartActivity.this)
                .setTitle(R.string.order)
                .setMessage(getString(R.string.your_order) + "\n\n" + sb.toString() + "\n"
                        + getString(R.string.total) + " " + total + "$\n"
                        + getString(R.string.confirm_order))
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), R.string.confirmed, Toast.LENGTH_SHORT).show();
                        clearBasket();
                    }

                })
                .setNegativeButton(R.string.no, null)
                .show();
    }

    private void clearBasket() {
        BasketService basketService = PharmacyRESTService.basketService();
        basketService.userClearBasket(user.id).enqueue(new Callback<Basket>() {
            @Override
            public void onResponse(Call<Basket> call, Response<Basket> response) {
                basket = response.body();
                if (basket != null) {
                    cartMedicines = basket.medicines;
                    setAdapter();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.enable_clear_basket, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Basket> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void setAdapter() {
        cartAdapter = new CartAdapter(this, cartMedicines, user);
        if (cartMedicines.size() == 0)
            emptyView.setVisibility(View.VISIBLE);
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
