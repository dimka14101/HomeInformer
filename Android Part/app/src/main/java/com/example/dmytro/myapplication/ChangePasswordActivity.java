package com.example.dmytro.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.READ_CONTACTS;


public class ChangePasswordActivity extends AppCompatActivity{

      // UI references.

    private EditText mOldPassword;
    private EditText mNewPassword;
    private EditText mPereatPassword;
    private View mProgressView;
    private View mLoginFormView;
    private Call<ResponseBody> jsonCallChangePassword;
    private User userCredentials=new User();
    private Controller controller=new Controller();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        // Set up the login form.


        mOldPassword = (EditText) findViewById(R.id.old_password);
        mNewPassword = (EditText) findViewById(R.id.new_password);
        mPereatPassword = (EditText) findViewById(R.id.confirm_password);


        Button mEmailSignInButton = (Button) findViewById(R.id.btnChandePassword);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.changePass_form);
        mProgressView = findViewById(R.id.change_progress);

    }











    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mOldPassword.setError(null);
        mNewPassword.setError(null);
        mPereatPassword.setError(null);

        // Store values at the time of the login attempt.
        String oldPassword = mOldPassword.getText().toString();
        String newPassword = mNewPassword.getText().toString();
        String confirmPassword = mPereatPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(oldPassword) && !isPasswordValid(oldPassword)&&!TextUtils.isEmpty(newPassword) && !isPasswordValid(newPassword)&& !TextUtils.isEmpty(confirmPassword)
                && !isPasswordValid(confirmPassword) && !mNewPassword.getText().equals(mPereatPassword.getText())) {
            mNewPassword.setError(getString(R.string.error_invalid_password));
            mPereatPassword.setError(getString(R.string.error_invalid_password));
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
          changePassword(oldPassword,newPassword);
        }
    }


    private void changePassword(String oldPassword, String newPassword) {
        userCredentials=controller.loadUserCredentials(getApplicationContext());
        jsonCallChangePassword = ApiModule.getClient().changePassword(oldPassword,newPassword,newPassword,userCredentials.getTokenType()+" "+userCredentials.getAccessToken());
        jsonCallChangePassword.enqueue(new Callback<ResponseBody>() {


            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.i("RETROFIT", "Login " + response);
                    if (response.code()==200) {
                       controller.showToastMessage(getApplicationContext(),"Пароль успішно змінено!");
                        showProgress(false);
                        Intent main = new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(main);
                    }else{
                        showProgress(false);
                        mPereatPassword.setError(getString(R.string.error_incorrect_password));
                        mPereatPassword.requestFocus();
                        controller.showToastMessage(getApplicationContext(),"Помилка! пароль не змінено! Перевірте повторно дані.");
                    }
                    System.out.println("ChangePass +");

                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println("ChangePass - " + ex);

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showProgress(false);
                mPereatPassword.setError(getString(R.string.error_incorrect_password));
                mPereatPassword.requestFocus();
                controller.showToastMessage(getApplicationContext(),"Помилка! пароль не змінено. Перевіртне з'єднання з інтернетом.");
            }
        });
    }



    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        if(password.length() > 6 && matcher.matches()) return true;
        else return false;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();
    }

}

