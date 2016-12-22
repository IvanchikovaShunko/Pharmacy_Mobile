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
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import bsu.fpmi.pharmacy.pharmacy_mobile.R;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.entity.User;
import bsu.fpmi.pharmacy.pharmacy_mobile.serialize.UserSerializer;

public abstract class BaseNavDrawerActivity extends AppCompatActivity {
    protected Handler handler;

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView headerbackgroudImageView, profileImageView;
    private TextView nameTextView, emailTextView;
    protected boolean isOpenLoginPage = false;
    private Toolbar toolbar;
    private LinearLayout headerView;

    protected User user;

    protected abstract void initActivityGUI();

    protected abstract int getContentView();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        handler = new Handler();
        initUser();
        initDrawer();
        setUpNavigationView();
        initActivityGUI();
        loadNavHeader();


    }

    private void initUser() {
        Bundle args = getIntent().getExtras();
        if (args != null) {
            String userJSON = args.getString("USER");
            if (!TextUtils.isEmpty(userJSON)) {
                user = new UserSerializer().deserializeModel(userJSON);
            }
        }
    }

    protected void loadNavHeader() {
        if (user != null) {
            nameTextView.setText(user.username);
            emailTextView.setText(user.userDetail.email);
        }

    }


    protected void initDrawer() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        navHeader = navigationView.getHeaderView(0);
        nameTextView = (TextView) navHeader.findViewById(R.id.header_name);
        emailTextView = (TextView) navHeader.findViewById(R.id.header_email);
        headerbackgroudImageView = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        profileImageView = (ImageView) navHeader.findViewById(R.id.img_profile);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);

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

        headerView = (LinearLayout) navHeader.findViewById(R.id.header_layout);
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user == null)
                    loadLogInView();
            }
        });
    }

    private void setUpNavigationView() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                onNavItemClick(menuItem.getItemId());
                return false;
            }
        });
        navigationView.bringToFront();
    }


    private void onNavItemClick(int itemId) {
        Intent launchIntent = null;
        switch (itemId) {
            case R.id.nav_profile:
                if (this instanceof ProfileActivity)
                    break;
                if (user == null)
                    launchIntent = new Intent(this, SignInActivity.class);
                else
                    launchIntent = new Intent(this, ProfileActivity.class);
                break;
            case R.id.nav_cart:
                if (this instanceof CartActivity)
                    break;
                if (user == null)
                    launchIntent = new Intent(this, SignInActivity.class);
                else
                    launchIntent = new Intent(this, CartActivity.class);
                break;
            case R.id.nav_subscriptions:
                if (this instanceof SubscriptionsActivity)
                    break;
                if (user == null)
                    launchIntent = new Intent(this, SignInActivity.class);
                else
                    launchIntent = new Intent(this, SubscriptionsActivity.class);
                break;
            case R.id.nav_medicine:
                if (this instanceof MedicineActivity)
                    break;
                launchIntent = new Intent(this, MedicineActivity.class);
                break;
            case R.id.nav_instructions:
                if (this instanceof InstructionsActivity)
                    break;
                launchIntent = new Intent(this, InstructionsActivity.class);
                break;
            case R.id.nav_settings:
                if (this instanceof SettingsActivity)
                    break;
                launchIntent = new Intent(this, SettingsActivity.class);
                break;
            case R.id.nav_about:
                if (this instanceof AboutActivity)
                    break;
                launchIntent = new Intent(this, AboutActivity.class);
                break;
        }

        if (launchIntent != null) {
            final Intent finalLaunchIntent = launchIntent;
            finalLaunchIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            finalLaunchIntent.putExtra("USER", new UserSerializer().serializeModel(user));

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
        isOpenLoginPage = false;
        Intent intent = new Intent(this, SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (user != null) {
            MenuInflater inflater = getMenuInflater();

            inflater.inflate(R.menu.main_menu, menu);
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_logout) {
            user = null;
            Intent intent = new Intent(getApplicationContext(), MedicineActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("USER", new UserSerializer().serializeModel(user));
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }


}
