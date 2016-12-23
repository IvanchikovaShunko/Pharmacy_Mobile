package bsu.fpmi.pharmacy.pharmacy_mobile.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import bsu.fpmi.pharmacy.pharmacy_mobile.R;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.PharmacyRESTService;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.entity.Medicine;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.entity.User;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.service.MedicineService;
import bsu.fpmi.pharmacy.pharmacy_mobile.serialize.UserSerializer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMedicineActivity extends AppCompatActivity {
    public static final String DATE_FORMAT = "MM/dd/yyyy";

    private Toolbar toolbar;
    private ProgressDialog progressDialog;

    private FloatingActionButton floatingActionButton;
    private TextInputLayout nameWrapper;
    private TextInputLayout aboutWrapper;
    private TextInputLayout consistWrapper;
    private TextInputLayout contradictionsWrapper;
    private TextInputLayout dosingWrapper;
    private TextInputLayout quantityWrapper;
    private TextInputLayout gramInOneWrapper;
    private TextInputLayout dateWrapper;
    private TextInputLayout stateWrapper;
    private TextInputLayout costWrapper;
    private EditText editTextName, editTextAbout, editTextConsist, editTextContradictions,
            editTextDosing, editTextGramInOne, editTextQuantity, editTextCost, editTextState, editTextDate;

    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);
        initUser();
        initGUI();
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

        nameWrapper = (TextInputLayout) findViewById(R.id.nameWrapper);
        aboutWrapper = (TextInputLayout) findViewById(R.id.aboutWrapper);
        consistWrapper = (TextInputLayout) findViewById(R.id.consistWrapper);
        contradictionsWrapper = (TextInputLayout) findViewById(R.id.contradictionsWrapper);
        dosingWrapper = (TextInputLayout) findViewById(R.id.dosingWrapper);
        quantityWrapper = (TextInputLayout) findViewById(R.id.quantityWrapper);
        gramInOneWrapper = (TextInputLayout) findViewById(R.id.gramInOneWrapper);
        costWrapper = (TextInputLayout) findViewById(R.id.costWrapper);
        stateWrapper = (TextInputLayout) findViewById(R.id.stateWrapper);
        dateWrapper = (TextInputLayout) findViewById(R.id.dateWrapper);

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextAbout = (EditText) findViewById(R.id.editTextAbout);
        editTextConsist = (EditText) findViewById(R.id.editTextConsist);
        editTextContradictions = (EditText) findViewById(R.id.editTextContradictions);
        editTextDosing = (EditText) findViewById(R.id.editTextDosing);
        editTextQuantity = (EditText) findViewById(R.id.editTextQuantity);
        editTextGramInOne = (EditText) findViewById(R.id.editTextGramInOne);
        editTextCost = (EditText) findViewById(R.id.editTextCost);
        editTextState = (EditText) findViewById(R.id.editTextState);
        editTextDate = (EditText) findViewById(R.id.editTextDate);

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(getString(R.string.processing));

        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAreFieldsEmpty()) {
                    Medicine medicine = new Medicine();
                    medicine.nameMedicine = editTextName.getText().toString();
                    medicine.aboutMedicine = editTextAbout.getText().toString();
                    medicine.state = editTextState.getText().toString();
                    medicine.gramInOne = Double.parseDouble(editTextGramInOne.getText().toString());
                    medicine.quantity = Integer.parseInt(editTextQuantity.getText().toString());
                    medicine.cost = Double.parseDouble(editTextCost.getText().toString());
                    medicine.consist = editTextConsist.getText().toString();
                    medicine.contradictions = editTextContradictions.getText().toString();
                    medicine.dosing = editTextDosing.getText().toString();
                    DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
                    Date date = new Date();
                    try {
                        date = dateFormat.parse(editTextDate.getText().toString());
                    } catch (ParseException e) {
                       editTextDate.setError(getString(R.string.invalid_date));
                        return;
                    }
                    medicine.expiration_date = date;
                    new AddMedicineAsyncTask(medicine).execute();
                }
            }

        });


    }

    private boolean checkAreFieldsEmpty() {
        setErrorNull();
        View focusView = null;

        if (TextUtils.isEmpty(editTextName.getText())) {
            fieldIsEmpty(editTextName);
            return false;
        }
        if (TextUtils.isEmpty(editTextState.getText())) {
            fieldIsEmpty(editTextState);
            return false;
        }

        if (TextUtils.isEmpty(editTextAbout.getText())) {
            fieldIsEmpty(editTextAbout);
            return false;
        }

        if (TextUtils.isEmpty(editTextConsist.getText())) {
            fieldIsEmpty(editTextConsist);
            return false;
        }

        if (TextUtils.isEmpty(editTextDosing.getText())) {
            fieldIsEmpty(editTextDosing);
            return false;
        }

        if (TextUtils.isEmpty(editTextContradictions.getText())) {
            fieldIsEmpty(editTextContradictions);
            return false;
        }

        if (TextUtils.isEmpty(editTextDate.getText())) {
            fieldIsEmpty(editTextDate);
            return false;
        }

        if (TextUtils.isEmpty(editTextGramInOne.getText())) {
            fieldIsEmpty(editTextGramInOne);
            return false;
        }

        if (TextUtils.isEmpty(editTextCost.getText())) {
            fieldIsEmpty(editTextCost);
            return false;
        }

        if (TextUtils.isEmpty(editTextQuantity.getText())) {
            fieldIsEmpty(editTextQuantity);
            return false;
        }

        return true;
    }


    private void setErrorNull() {
        editTextName.setError(null);
        editTextAbout.setError(null);
        editTextConsist.setError(null);
        editTextContradictions.setError(null);
        editTextDosing.setError(null);
        editTextQuantity.setError(null);
        editTextGramInOne.setError(null);
        editTextCost.setError(null);
        editTextState.setError(null);
        editTextDate.setError(null);
    }

    private void fieldIsEmpty(EditText editText) {
        editText.setError(getString(R.string.field_required));
        editText.requestFocus();
    }

    public class AddMedicineAsyncTask extends AsyncTask<Void, Void, Void> {

        private Medicine medicine;

        AddMedicineAsyncTask(Medicine medicine) {
            this.medicine = medicine;
        }

        @Override
        protected Void doInBackground(Void... params) {
            MedicineService medicineService = PharmacyRESTService.medicineService();
            DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            String date = dateFormat.format(medicine.expiration_date);
            medicineService.medicineAdd(medicine.nameMedicine, medicine.aboutMedicine, medicine.gramInOne,
                    medicine.cost, medicine.quantity, medicine.consist, medicine.state, medicine.dosing,
                    medicine.contradictions, date).enqueue(new Callback<Medicine>() {
                @Override
                public void onResponse(Call<Medicine> call, Response<Medicine> response) {
                    Medicine medicineNew = response.body();
                    if (medicineNew == null) {
                        Toast.makeText(getApplicationContext(), R.string.unable_add_medicine, Toast.LENGTH_SHORT).show();
                    } else {
                        launchMedicineIntent();
                    }
                }

                @Override
                public void onFailure(Call<Medicine> call, Throwable t) {
                      Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(final Void success) {
            progressDialog.hide();
        }

        @Override
        protected void onCancelled() {
            progressDialog.hide();
        }
    }

    private void launchMedicineIntent() {
        Intent intent = new Intent(this, MedicineActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra("USER", new UserSerializer().serializeModel(user));
        startActivity(intent);
    }
}
