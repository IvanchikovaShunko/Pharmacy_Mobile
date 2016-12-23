package bsu.fpmi.pharmacy.pharmacy_mobile.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import bsu.fpmi.pharmacy.pharmacy_mobile.PharmacyApp;
import bsu.fpmi.pharmacy.pharmacy_mobile.R;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.PharmacyRESTService;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.entity.Medicine;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.entity.User;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.service.MedicineService;
import bsu.fpmi.pharmacy.pharmacy_mobile.serialize.MedicineSerializer;
import bsu.fpmi.pharmacy.pharmacy_mobile.serialize.UserSerializer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MedicineInfoActivity extends AppCompatActivity {
    public static final String DATE_FORMAT = "MM/dd/yyyy";

    private Toolbar toolbar;
    private TextView nameTextView, consistTextView, costTextView, contradictionsTextView,
            stateTextView, gramTextView, dosingTextView, aboutTextView;
    private ImageView imageView;
    private Medicine medicine;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_info);
        initFields();
        initGUI();
    }

    private void initGUI() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        nameTextView = (TextView) findViewById(R.id.nameTextView);
        consistTextView = (TextView) findViewById(R.id.consistTextView);
        costTextView = (TextView) findViewById(R.id.costTextView);
        contradictionsTextView = (TextView) findViewById(R.id.contradictionsTextView);
        aboutTextView = (TextView) findViewById(R.id.aboutTextView);
        dosingTextView = (TextView) findViewById(R.id.dosingTextView);
        stateTextView = (TextView) findViewById(R.id.stateTextView);
        gramTextView = (TextView) findViewById(R.id.gramTextView);
        imageView = (ImageView) findViewById(R.id.image_medicine);

        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

        if (medicine != null) {

            String date = "";
            if (medicine.expiration_date != null)
                date = dateFormat.format(medicine.expiration_date);

            setTitle(medicine.nameMedicine);
            nameTextView.setText(medicine.nameMedicine);
            consistTextView.setText(medicine.consist);
            costTextView.setText(String.valueOf(medicine.cost) + "$");
            contradictionsTextView.setText(medicine.contradictions);
            aboutTextView.setText(medicine.aboutMedicine);
            dosingTextView.setText(medicine.dosing);
            stateTextView.setText(medicine.state);
            gramTextView.setText(String.valueOf(medicine.gramInOne) + " мл");
            if (!TextUtils.isEmpty(medicine.imagePath)){
                PharmacyApp.PICASSO.load(medicine.imagePath).into(imageView);
            }
        }
    }

    private void initFields() {
        Bundle args = getIntent().getExtras();
        if (args != null) {
            String userJSON = args.getString("USER");
            if (!TextUtils.isEmpty(userJSON)) {
                user = new UserSerializer().deserializeModel(userJSON);
            }
            String medicineJSON = args.getString("MEDICINE");
            if (!TextUtils.isEmpty(medicineJSON)) {
                medicine = new MedicineSerializer().deserializeModel(medicineJSON);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (user != null && user.role.equalsIgnoreCase("admin")) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.medicine_menu_admin, menu);
            return true;
        } else {
            super.onCreateOptionsMenu(menu);
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_delete_medicine) {
            new AlertDialog.Builder(MedicineInfoActivity.this)
                    .setTitle(R.string.deleting_medicine)
                    .setMessage(R.string.confirm_delete_medicine)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final MedicineService medicineService = PharmacyRESTService.medicineService();
                            medicineService.medicineDelete(medicine.idMedicine).enqueue(new Callback<Integer>() {
                                @Override
                                public void onResponse(Call<Integer> call, Response<Integer> response) {
                                    if (response.body() != null)
                                        if (response.body().equals(200)) {
                                        Toast.makeText(getApplicationContext(), R.string.med_deleted, Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Integer> call, Throwable t) {
                                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    })
                    .setNegativeButton(R.string.no, null)
                    .show();
        }


        return super.onOptionsItemSelected(item);
    }
}
