package bsu.fpmi.pharmacy.pharmacy_mobile.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import bsu.fpmi.pharmacy.pharmacy_mobile.R;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.PharmacyRESTService;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.entity.User;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.entity.UserDetail;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.service.UserService;
import bsu.fpmi.pharmacy.pharmacy_mobile.serialize.UserSerializer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    private EditText fullNameEditText, loginEditText, emailEditText, passwordEditText,
            confPasswordEditText, ageEditText, addressEditText, phoneEditText, aboutEditText, genderEditText;
    //    private Spinner spinner;
    private FloatingActionButton floatingActionButton;
    private ProgressDialog progressDialog;

    private TextInputLayout usernameWrapper;
    private TextInputLayout emailWrapper;
    private TextInputLayout fullNameWrapper;
    private TextInputLayout confirmPasswordWrapper;
    private TextInputLayout passwordWrapper;
    private TextInputLayout ageWrapper;
    private TextInputLayout genderWrapper;
    private TextInputLayout addressWrapper;
    private TextInputLayout phoneWrapper;
    private TextInputLayout aboutWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initGUI();
    }

    private void initGUI() {
        loginEditText = (EditText) findViewById(R.id.editTextLogin);
        emailEditText = (EditText) findViewById(R.id.editTextEmail);
        passwordEditText = (EditText) findViewById(R.id.editTextPassword);
        confPasswordEditText = (EditText) findViewById(R.id.editTextConfirmPassword);
        ageEditText = (EditText) findViewById(R.id.editTextAge);
        fullNameEditText = (EditText) findViewById(R.id.editTextFullName);
        addressEditText = (EditText) findViewById(R.id.editTextAddress);
        phoneEditText = (EditText) findViewById(R.id.editTextPhone);
        aboutEditText = (EditText) findViewById(R.id.editTextAbout);
        genderEditText = (EditText) findViewById(R.id.editTextGender);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(getString(R.string.signing_up));

        usernameWrapper = (TextInputLayout) findViewById(R.id.usernameWrapper);
        usernameWrapper.setHint(getString(R.string.username));
        emailWrapper = (TextInputLayout) findViewById(R.id.emailWrapper);
        emailWrapper.setHint(getString(R.string.email));
        passwordWrapper = (TextInputLayout) findViewById(R.id.passwordWrapper);
        passwordWrapper.setHint(getString(R.string.password));
        confirmPasswordWrapper = (TextInputLayout) findViewById(R.id.confirmPasswordWrapper);
        confirmPasswordWrapper.setHint(getString(R.string.confirm_password));
        fullNameWrapper = (TextInputLayout) findViewById(R.id.fullNameWrapper);
        fullNameWrapper.setHint(getString(R.string.full_name));
        ageWrapper = (TextInputLayout) findViewById(R.id.ageWrapper);
        ageWrapper.setHint(getString(R.string.age));
        genderWrapper = (TextInputLayout) findViewById(R.id.genderWrapper);
        genderWrapper.setHint(getString(R.string.gender));
        addressWrapper = (TextInputLayout) findViewById(R.id.addressWrapper);
        addressWrapper.setHint(getString(R.string.address));
        phoneWrapper = (TextInputLayout) findViewById(R.id.phoneWrapper);
        phoneWrapper.setHint(getString(R.string.phone));
        aboutWrapper = (TextInputLayout) findViewById(R.id.aboutWrapper);
        aboutWrapper.setHint(getString(R.string.about_you));

    }

    private void register() {
        String password = passwordEditText.getText().toString();
        String confPassword = confPasswordEditText.getText().toString();

        if (checkAreFieldsEmpty() && passwordsMatching(password, confPassword)) {
            User user = new User();
            user.userDetail = new UserDetail();
            user.userDetail.gender = genderEditText.getText().toString();
            user.userDetail.about = aboutEditText.getText().toString();
            if (!TextUtils.isEmpty(ageEditText.getText().toString()))
                user.userDetail.age = Integer.parseInt(ageEditText.getText().toString());
            user.userDetail.email = emailEditText.getText().toString();
            user.userDetail.homeAddress = addressEditText.getText().toString();
            user.userDetail.name = fullNameEditText.getText().toString();
            user.userDetail.telephone = phoneEditText.getText().toString();

            user.password = passwordEditText.getText().toString();
            user.role = "USER";
            user.username = loginEditText.getText().toString();
            new SignUpAsyncTask(user).execute();
            progressDialog.show();


        }
    }

    private boolean passwordsMatching(String pass, String confPass) {
        boolean res = pass.equals(confPass);
        if (!res) {
            Toast.makeText(getApplicationContext(), R.string.password_not_match, Toast.LENGTH_SHORT).show();
            confPasswordEditText.setError(getString(R.string.not_match));
            passwordEditText.setError(getString(R.string.not_match));
            confPasswordEditText.requestFocus();
        }

        return res;
    }

    private boolean checkAreFieldsEmpty() {
        setErrorNull();
        View focusView = null;

        if (TextUtils.isEmpty(loginEditText.getText())) {
            fieldIsEmpty(loginEditText);
            return false;
        }
        if (TextUtils.isEmpty(emailEditText.getText())) {
            fieldIsEmpty(emailEditText);
            return false;
        }
        if (TextUtils.isEmpty(passwordEditText.getText())) {
            fieldIsEmpty(passwordEditText);
            return false;
        }
        if (TextUtils.isEmpty(confPasswordEditText.getText())) {
            fieldIsEmpty(confPasswordEditText);
            return false;
        }
//        if (TextUtils.isEmpty(ageEditText.getText())) {
//            fieldIsEmpty(ageEditText);
//            return false;
//        }
//
        if (TextUtils.isEmpty(fullNameEditText.getText())) {
            fieldIsEmpty(fullNameEditText);
            return false;
        }
//        if (TextUtils.isEmpty(addressEditText.getText())) {
//            fieldIsEmpty(addressEditText);
//            return false;
//        }
//        if (TextUtils.isEmpty(phoneEditText.getText())) {
//            fieldIsEmpty(phoneEditText);
//            return false;
//        }

//        if (TextUtils.isEmpty(aboutEditText.getText())) {
//            fieldIsEmpty(aboutEditText);
//            return false;
//        }

        return true;
    }

    private void setErrorNull() {
        loginEditText.setError(null);
        emailEditText.setError(null);
        passwordEditText.setError(null);
        confPasswordEditText.setError(null);
        ageEditText.setError(null);
        fullNameEditText.setError(null);
        addressEditText.setError(null);
        phoneEditText.setError(null);
        aboutEditText.setError(null);

    }

    private void fieldIsEmpty(EditText editText) {
        editText.setError(getString(R.string.field_required));
        editText.requestFocus();
    }


    public class SignUpAsyncTask extends AsyncTask<Void, Void, Void> {
        private User user;

        SignUpAsyncTask(User user) {
            this.user = user;
        }

        @Override
        protected Void doInBackground(Void... params) {
            UserService userService = PharmacyRESTService.userService();
            userService.userAdd(user.username, user.password, user.userDetail.name, user.userDetail.age,
                    user.userDetail.gender, user.userDetail.about, user.userDetail.homeAddress,
                    user.userDetail.email, user.userDetail.telephone).enqueue(new Callback<User>() {
                  @Override
                  public void onResponse(Call<User> call, Response<User> response) {
                      User user = response.body();
                      if (user == null) {
                          Toast.makeText(getApplicationContext(), R.string.cant_sign_up, Toast.LENGTH_SHORT).show();
                      } else {
                          Intent intent = new Intent(getApplicationContext(), MedicineActivity.class);
                          intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                          intent.putExtra("USER", new UserSerializer().serializeModel(user));
                          startActivity(intent);
                      }
                      progressDialog.hide();
                  }

                  @Override
                  public void onFailure(Call<User> call, Throwable t) {
                      progressDialog.hide();
                      Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                  }
              }

            );

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
}
