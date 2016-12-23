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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import bsu.fpmi.pharmacy.pharmacy_mobile.R;
import bsu.fpmi.pharmacy.pharmacy_mobile.activities.SubscriptionsActivity;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.PharmacyRESTService;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.entity.Basket;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.entity.Subscription;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.entity.User;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.service.SubscriptionService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by annashunko on 25-Nov-16.
 */

public class SubscriptionsAdapter extends BaseAdapter {
    SubscriptionsActivity activity;
    LayoutInflater lInflater;
    User user;

    List<Subscription> subscriptionList;

    public SubscriptionsAdapter(SubscriptionsActivity activity, List<Subscription> subscriptions, User user) {
        this.activity = activity;
        this.subscriptionList = subscriptions;
        this.user = user;
        lInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return subscriptionList.size();
    }

    @Override
    public Object getItem(int i) {
        return subscriptionList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View cView = view;
        if (cView == null) {
            cView = lInflater.inflate(R.layout.item_subscruption, viewGroup, false);
        }
        final Subscription subscription = (Subscription) getItem(i);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, Integer.parseInt(subscription.subscriptionPeriod));  // number of days to add
        String date = new SimpleDateFormat("dd.MM.yyyy").format(c.getTime());


        ((TextView) cView.findViewById(R.id.sub_name)).setText(subscription.medicine.nameMedicine);
        ((TextView) cView.findViewById(R.id.expire_date_textView)).setText("Подписка действительна до: " + date);
        ((TextView) cView.findViewById(R.id.days_left_textView)).setText("Осталось " + subscription.subscriptionPeriod + " дней(я)");


        ImageView cancelImageView = (ImageView) cView.findViewById(R.id.cancel_imageView);
        cancelImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unsubscribe(subscription.idSubscription);
            }
        });
        return cView;
    }

    private void unsubscribe(int subId) {
        final SubscriptionService subscriptionService = PharmacyRESTService.subscriptionService();
        subscriptionService.unsubscribe(user.id, subId).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response != null)
                    if (response.body() == 200) {
                        subscriptionService.userSubscriptions(user.id).enqueue(new Callback<List<Subscription>>() {
                            @Override
                            public void onResponse(Call<List<Subscription>> call, Response<List<Subscription>> response) {
                                activity.setSubscriptionList(response.body());
                                activity.setAdapter();
                                Toast.makeText(activity, R.string.unsubscribed, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<List<Subscription>> call, Throwable t) {
                                Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
