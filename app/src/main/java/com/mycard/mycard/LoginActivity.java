package com.mycard.mycard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.mycard.mycard.comm.CommunicationManager;
import com.mycard.mycard.model.User;
import com.sirvar.robin.RobinActivity;
import com.sirvar.robin.Theme;


public class LoginActivity extends RobinActivity {

    final String TAG = "LoginActivity";
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setLoginTitle("登录");
        setSignupTitle("注册");
        setForgotPasswordTitle("忘记密码");
        setImage(getResources().getDrawable(R.drawable.logo));
        setFont(Typeface.createFromAsset(getAssets(), "Montserrat-Medium.ttf"));
        setTheme(Theme.LIGHT);
        disableSocialLogin();
        showLoginFirst();
    }


    @Override
    protected void onLogin(String email, String password) {
        Toast.makeText(getApplicationContext(), "Login", Toast.LENGTH_SHORT).show();

        new UpdateTask().execute(email, password);

        if(password.equals("123")) {
            Log.e("frankie", "123");
            startActivity(new Intent(this, test2.class));
        }
        else if(password.equals("321")) {
            Log.e("frankie", "321");
            startActivity(new Intent(this, test1.class));
        }

    }

    @Override
    protected void onSignup(String name, String email, String password) {
        Toast.makeText(getApplicationContext(), "Signup", Toast.LENGTH_SHORT).show();
        // Make API call
    }

    @Override
    protected void onForgotPassword(String email) {
        Toast.makeText(getApplicationContext(), "Forgot Password", Toast.LENGTH_SHORT).show();
        // Make API call
        // After sent password email callback
        startLoginFragment();
    }

    @Override
    protected void onGoogleLogin() {
        Toast.makeText(getApplicationContext(), "Google", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onFacebookLogin() {
        Toast.makeText(getApplicationContext(), "Facebook", Toast.LENGTH_SHORT).show();
    }

    private class UpdateTask extends AsyncTask<String, Void, User> {

        private ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(LoginActivity.this);
            mProgressDialog.setMessage("login...");
            mProgressDialog.show();
        }

        @Override
        protected User doInBackground(String... params) {
            User registered = null;
            try {
                registered = CommunicationManager.getInstance(getBaseContext())
                        .loginUser(params[0], params[1]);
            } catch (Exception e) {
                Log.e(TAG, "Error login in: " + e.getMessage());
            }
            return registered;
        }

        @Override
        protected void onPostExecute(User result) {
            mProgressDialog.dismiss();
            if (result != null) {
                Toast.makeText(getBaseContext(),
                        "onPostExecute", Toast.LENGTH_LONG).show();


                finish();
            } else {
                Toast.makeText(getBaseContext(), "onPostExe error", Toast.LENGTH_LONG).show();
            }
        }
    }


    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
