package bsu.fpmi.pharmacy.pharmacy_mobile.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import bsu.fpmi.pharmacy.pharmacy_mobile.R;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.PharmacyRESTService;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.entity.User;
import bsu.fpmi.pharmacy.pharmacy_mobile.api.service.UserService;
import bsu.fpmi.pharmacy.pharmacy_mobile.serialize.UserSerializer;
import bsu.fpmi.pharmacy.pharmacy_mobile.util.Dialogs;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {
    private UserLoginTask mAuthTask = null;
    EditText loginEditText;
    EditText passwordEditText;
    Button signInButton;
    Button signUpButton;

    ProgressDialog progressDialog;
    private TextInputLayout loginWrapper;
    private TextInputLayout passwordWrapper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initGUI();
    }

    private void initGUI() {
        loginEditText = (EditText) findViewById(R.id.loginEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        signInButton = (Button) findViewById(R.id.signInButton);
        signUpButton = (Button) findViewById(R.id.signUpButton);
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Signing in...");

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        loginEditText.requestFocus();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });
        loginWrapper = (TextInputLayout) findViewById(R.id.loginWrapper);
        loginWrapper.setHint("Login");
        passwordWrapper = (TextInputLayout) findViewById(R.id.passwordWrapper);
        passwordWrapper.setHint("Password");



    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        loginEditText.setError(null);
        passwordEditText.setError(null);

        String login = loginEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(login)) {
            loginEditText.setError(getString(R.string.error_field_required));
            focusView = loginEditText;
            cancel = true;
        } else if (TextUtils.isEmpty(password)) {
            passwordEditText.setError(getString(R.string.error_field_required));
            focusView = passwordEditText;
            cancel = true;
        }

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            passwordEditText.setError(getString(R.string.error_invalid_password));
            focusView = passwordEditText;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            progressDialog.show();
            mAuthTask = new UserLoginTask(login, password);
            mAuthTask.execute((Void) null);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), MedicineActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Void> {
        private final String mLogin;
        private final String mPassword;

        UserLoginTask(String login, String password) {
            mLogin = login;
            mPassword = password;
        }

        @Override
        protected Void doInBackground(Void... params) {
            UserService userService = PharmacyRESTService.userService();
            userService.userAuth(mLogin, mPassword).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    User user = response.body();
                    if (user == null) {
                        Toast.makeText(getApplicationContext(), "Incorrect username or password", Toast.LENGTH_SHORT).show();
                        loginEditText.requestFocus();
                    } else {
                        Intent intent = new Intent(getApplicationContext(), MedicineActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.putExtra("USER", new UserSerializer().serializeModel(user));
                        startActivity(intent);
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(final Void success) {
            mAuthTask = null;
            progressDialog.hide();
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            progressDialog.hide();
        }
    }

}
