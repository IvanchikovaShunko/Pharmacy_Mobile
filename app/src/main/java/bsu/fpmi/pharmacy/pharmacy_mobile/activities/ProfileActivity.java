package bsu.fpmi.pharmacy.pharmacy_mobile.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import bsu.fpmi.pharmacy.pharmacy_mobile.R;
import bsu.fpmi.pharmacy.pharmacy_mobile.serialize.UserSerializer;

public class ProfileActivity extends BaseNavDrawerActivity {
    TextView usernameTextView, nameTextView, aboutTextView, phoneTextView, emailTextView, addressTextView;
    TextView myCartButton, mySubscriptionsButton, myOrdersButton, logOutButton;
    FloatingActionButton floatingActionButton;

    @Override
    protected void initActivityGUI() {
        usernameTextView = (TextView) findViewById(R.id.username_TextView);
        nameTextView = (TextView) findViewById(R.id.full_name_textView);
        aboutTextView = (TextView) findViewById(R.id.about_TextView);
        phoneTextView = (TextView) findViewById(R.id.phone_TextView);
        emailTextView = (TextView) findViewById(R.id.email_TextView);
        addressTextView = (TextView) findViewById(R.id.address_TextView);

        myCartButton = (TextView) findViewById(R.id.my_cart_button);
        mySubscriptionsButton = (TextView) findViewById(R.id.my_subscriptions_button);
        myOrdersButton = (TextView) findViewById(R.id.my_orders_button);
        logOutButton = (TextView) findViewById(R.id.logout_button);



        myCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewActivity(CartActivity.class);
            }
        });

        mySubscriptionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewActivity(SubscriptionsActivity.class);
            }
        });

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user = null;
                startNewActivity(MedicineActivity.class);
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_profile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (user != null) {
            String notAvailable = "N/A";
            String login = user.username == null ? notAvailable : user.username;
            String name = user.userDetail.name == null ? notAvailable : user.userDetail.name;
            String about = user.userDetail.about == null ? notAvailable : user.userDetail.about;
            String telephone = user.userDetail.telephone == null ? notAvailable : user.userDetail.telephone;
            String email = user.userDetail.email == null ? notAvailable : user.userDetail.email;
            String homeAddress = user.userDetail.homeAddress == null ? notAvailable : user.userDetail.homeAddress;

            usernameTextView.setText(login);
            nameTextView.setText(name);
            aboutTextView.setText(about);
            phoneTextView.setText(telephone);
            emailTextView.setText(email);
            addressTextView.setText(homeAddress);
        }
    }

    private void startNewActivity(Class aClass) {
        Intent intent = new Intent(getApplicationContext(), aClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra("USER", new UserSerializer().serializeModel(user));
        startActivity(intent);
    }


}
