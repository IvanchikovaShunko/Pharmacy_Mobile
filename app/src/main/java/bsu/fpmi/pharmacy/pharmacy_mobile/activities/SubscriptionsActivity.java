package bsu.fpmi.pharmacy.pharmacy_mobile.activities;

import android.app.ProgressDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import bsu.fpmi.pharmacy.pharmacy_mobile.R;
import bsu.fpmi.pharmacy.pharmacy_mobile.adapters.SubscriptionsAdapter;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.entity.Subscription;

public class SubscriptionsActivity extends BaseNavDrawerActivity {
    private ListView listView;
    private SubscriptionsAdapter adapter;
    private ProgressDialog progressDialog;

    List<Subscription> subscriptionList = new ArrayList<>();
    private SwipeRefreshLayout mSwipeLayout;


    @Override
    protected void initActivityGUI() {
        listView = (ListView) findViewById(R.id.list_view_subscriptions);
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(getString(R.string.loading));

        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mSwipeLayout.setColorSchemeResources(R.color.colorPrimaryDark);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeLayout.setRefreshing(false);

            }
        });

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
