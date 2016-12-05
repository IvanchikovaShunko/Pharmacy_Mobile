package bsu.fpmi.pharmacy.pharmacy_mobile.activities;

import android.content.Intent;
import android.os.AsyncTask;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {
    private UserLoginTask mAuthTask = null;
    EditText loginEditText;
    EditText passwordEditText;
    Button signInButton;
    Button signUpButton;


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
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
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

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            passwordEditText.setError(getString(R.string.error_invalid_password));
            focusView = passwordEditText;
            cancel = true;
        }

        if (TextUtils.isEmpty(login)) {
            loginEditText.setError(getString(R.string.error_field_required));
            focusView = loginEditText;
            cancel = true;
        } else if (TextUtils.isEmpty(password)) {
            passwordEditText.setError(getString(R.string.error_field_required));
            focusView = passwordEditText;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            //showProgress(true);
            mAuthTask = new UserLoginTask(login, password);
            mAuthTask.execute((Void) null);
        }
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
                        Toast.makeText(getApplicationContext(), "Access denied", Toast.LENGTH_SHORT).show();
                        loginEditText.setError(getString(R.string.error_invalid_email));
                        passwordEditText.setError(getString(R.string.error_incorrect_password));
                        loginEditText.requestFocus();
                    } else {
                        Intent intent = new Intent(getApplicationContext(), MedicineActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    loginEditText.setError(getString(R.string.error_invalid_email));
                    passwordEditText.setError(getString(R.string.error_incorrect_password));
                    passwordEditText.requestFocus();
                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(final Void success) {
            mAuthTask = null;
            //showProgress(false);
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            //showProgress(false);
        }
    }

}
