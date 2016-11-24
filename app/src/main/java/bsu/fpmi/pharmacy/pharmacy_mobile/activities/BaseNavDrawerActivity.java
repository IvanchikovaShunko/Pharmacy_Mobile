package bsu.fpmi.pharmacy.pharmacy_mobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import bsu.fpmi.pharmacy.pharmacy_mobile.R;

public abstract class BaseNavDrawerActivity extends AppCompatActivity {
    protected Handler handler;

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView headerbackgroudImageView, profileImageView;
    private TextView nameTextView, emailTextView;
    private boolean isOpenLoginPage = false;

    protected abstract void initActivityGUI();

    protected abstract int getContentView();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        handler = new Handler();
        initDrawer();
        loadNavHeader();
        setUpNavigationView();
        if (isOpenLoginPage)
            loadLogInView();
    }

    protected void loadNavHeader() {
//        if (user != null) {
//            User user = new UserDAO().getUser();
//            nameTextView.setText(user.getNickname());
//            emailTextView.setText(user.getEmail());
//        }

    }



    protected void initDrawer() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        navHeader = navigationView.getHeaderView(0);
        nameTextView = (TextView) navHeader.findViewById(R.id.header_name);
        emailTextView = (TextView) navHeader.findViewById(R.id.header_email);
        headerbackgroudImageView = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        profileImageView = (ImageView) navHeader.findViewById(R.id.img_profile);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer,
                (Toolbar) findViewById(R.id.toolbar),
                R.string.open_drawer, R.string.close_drawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
    }

    private void setUpNavigationView() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                onNavItemClick(menuItem.getItemId());
                return false;
            }
        });
    }

    private void onNavItemClick(int itemId) {
        Intent launchIntent = null;
        switch (itemId) {
            case R.id.nav_profile:
                if (this instanceof ProfileActivity)
                    break;
                launchIntent = new Intent(this, ProfileActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                break;
            case R.id.nav_cart:
                if (this instanceof CartActivity)
                    break;
                launchIntent = new Intent(this, CartActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                break;
            case R.id.nav_subscriptions:
                if (this instanceof SubscriptionsActivity)
                    break;
                launchIntent = new Intent(this, SubscriptionsActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                break;
            case R.id.nav_medicine:
                if (this instanceof MedicineActivity)
                    break;
                launchIntent = new Intent(this, MedicineActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                break;
            case R.id.nav_instructions:
                if (this instanceof InstructionsActivity)
                    break;
                launchIntent = new Intent(this, InstructionsActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                break;
            case R.id.nav_settings:
                if (this instanceof SettingsActivity)
                    break;
                launchIntent = new Intent(this, SettingsActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                break;
            case R.id.nav_about:
                if (this instanceof AboutActivity)
                    break;
                launchIntent = new Intent(this, AboutActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                break;
        }

        if (launchIntent != null) {
            final Intent finalLaunchIntent = launchIntent;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(finalLaunchIntent);
                }
            }, 250);
        }
        drawer.closeDrawer(GravityCompat.START);
    }

    protected void loadLogInView() {
//        isOpenLoginPage = false;
//        Intent intent = new Intent(this, LoginActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
        }
    }
}
