package bsu.fpmi.pharmacy.pharmacy_mobile.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import bsu.fpmi.pharmacy.pharmacy_mobile.R;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.entity.Medicine;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.entity.User;
import bsu.fpmi.pharmacy.pharmacy_mobile.serialize.MedicineSerializer;
import bsu.fpmi.pharmacy.pharmacy_mobile.serialize.UserSerializer;

public class MedicineInfoActivity extends AppCompatActivity {
    public static final String DATE_FORMAT = "MM/dd/yyyy";

    private Toolbar toolbar;
    private TextView nameTextView, consistTextView, costTextView, dateTextView, contradictionsTextView,
            quantityTextView, stateTextView, gramTextView, dosingTextView, aboutTextView;

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
        dateTextView = (TextView) findViewById(R.id.dateTextView);
        contradictionsTextView = (TextView) findViewById(R.id.contradictionsTextView);
        aboutTextView = (TextView) findViewById(R.id.aboutTextView);
        dosingTextView = (TextView) findViewById(R.id.dosingTextView);
        stateTextView = (TextView) findViewById(R.id.stateTextView);
        gramTextView = (TextView) findViewById(R.id.gramTextView);
        quantityTextView = (TextView) findViewById(R.id.quantityTextView);

        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

        if (medicine != null) {

            String date = "";
            if (medicine.expiration_date != null)
                date = dateFormat.format(medicine.expiration_date);

            setTitle(medicine.nameMedicine);
            nameTextView.setText(medicine.nameMedicine);
            consistTextView.setText(medicine.consist);
            costTextView.setText(String.valueOf(medicine.cost));
            dateTextView.setText(date);
            contradictionsTextView.setText(medicine.contradictions);
            aboutTextView.setText(medicine.aboutMedicine);
            dosingTextView.setText(medicine.dosing);
            stateTextView.setText(medicine.state);
            gramTextView.setText(String.valueOf(medicine.gramInOne));
            quantityTextView.setText(String.valueOf(medicine.quantity));
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
}
