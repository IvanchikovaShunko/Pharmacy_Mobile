package bsu.fpmi.pharmacy.pharmacy_mobile.activities;

import android.app.ProgressDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import bsu.fpmi.pharmacy.pharmacy_mobile.R;
import bsu.fpmi.pharmacy.pharmacy_mobile.adapters.SubscriptionsAdapter;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.PharmacyRESTService;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.entity.Subscription;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.service.SubscriptionService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubscriptionsActivity extends BaseNavDrawerActivity {
    private ListView listView;
    private SubscriptionsAdapter adapter;
    private ProgressDialog progressDialog;

    List<Subscription> subscriptionList = new ArrayList<>();
    private SwipeRefreshLayout mSwipeLayout;
    private RelativeLayout emptyView;


    @Override
    protected void initActivityGUI() {
        listView = (ListView) findViewById(R.id.list_view_subscriptions);
        emptyView = (RelativeLayout) findViewById(R.id.empty_layout);
        emptyView.setVisibility(View.GONE);

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(getString(R.string.loading));

        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mSwipeLayout.setColorSchemeResources(R.color.colorPrimaryDark);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SubscriptionService subscriptionService = PharmacyRESTService.subscriptionService();
                subscriptionService.userSubscriptions(user.id).enqueue(new Callback<List<Subscription>>() {
                    @Override
                    public void onResponse(Call<List<Subscription>> call, Response<List<Subscription>> response) {
                        subscriptionList = response.body();
                        setAdapter();
                        mSwipeLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<List<Subscription>> call, Throwable t) {
                        mSwipeLayout.setRefreshing(false);
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        SubscriptionService subscriptionService = PharmacyRESTService.subscriptionService();
        subscriptionService.userSubscriptions(user.id).enqueue(new Callback<List<Subscription>>() {
            @Override
            public void onResponse(Call<List<Subscription>> call, Response<List<Subscription>> response) {
                subscriptionList = response.body();
                setAdapter();
            }

            @Override
            public void onFailure(Call<List<Subscription>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void setAdapter() {
        adapter = new SubscriptionsAdapter(this, subscriptionList, user);
        if (subscriptionList.size() == 0)
            emptyView.setVisibility(View.VISIBLE);
        listView.setAdapter(adapter);
    }

    public List<Subscription> getSubscriptionList() {
        return subscriptionList;
    }

    public void setSubscriptionList(List<Subscription> subscriptionList) {
        this.subscriptionList = subscriptionList;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_subscriptions;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
