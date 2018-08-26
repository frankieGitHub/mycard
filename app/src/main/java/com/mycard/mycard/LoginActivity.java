package com.mycard.mycard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
    public static final String PREFS_NAME = "MyPrefsFile"; //本地保存登陆信息
    public static final String PREFS_USERNAME = "username"; //本地保存登陆信息
    public static final String PREFS_ACCESS_TOKEN = "token"; //本地保存登陆信息

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

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String username =  settings.getString(PREFS_USERNAME, "");
        String token = settings.getString(PREFS_ACCESS_TOKEN, "");

        new UpdateTask().execute(email, token);

//        String username = "frankie";
//        String token = "4eb87acfbe976fdd9238c4f87a3e9b3f";
//
//        new UpdateTask().execute(email, token);
//
//        if(password.equals("123")) {
//            Log.e("frankie", "123");
//            startActivity(new Intent(this, test2.class));
//        }
//        else if(password.equals("321")) {
//            Log.e("frankie", "321");
//
//        }

    }

    @Override
    protected void onSignup(String name, String email, String password) {
        Toast.makeText(getApplicationContext(), "Signup", Toast.LENGTH_SHORT).show();
        // Make API call

        User u = new User()
                .setUsername(name)
                .setEmail(email)
                .setFirstName("lin")
                .setLastName("ronghui")
                .setPassWord(password);

        new RegisterTask().execute(u);

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
            mProgressDialog.setMessage("正在登陆...");
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
                        "登陆成功！", Toast.LENGTH_LONG).show();
                startActivity(new Intent(LoginActivity.this, test2.class));


//                Intent resultIntent = new Intent(getBaseContext(), test1.class);
//                resultIntent.putExtra("user", result);
//                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                Toast.makeText(getBaseContext(), "登陆失败", Toast.LENGTH_LONG).show();
            }
        }
    }


    private class RegisterTask extends AsyncTask<User, Void, User> {

        private ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(LoginActivity.this);
            mProgressDialog.setMessage("正在注册...");
            mProgressDialog.show();
        }

        @Override
        protected User doInBackground(User... params) {
            User registered = null;
            try {
                registered = CommunicationManager
                        .getInstance(getBaseContext()).registerUser(params[0]);
            } catch (Exception e) {
                Log.e(TAG, "Error creating a user: " + e.getMessage());
            }
            return registered;
        }

        @Override
        protected void onPostExecute(User result) {
            mProgressDialog.dismiss();
            if (result != null) {

                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString(PREFS_USERNAME, result.getUsername());
                editor.putString(PREFS_ACCESS_TOKEN, result.getAccessToken());
                editor.commit();

                Toast.makeText(getBaseContext(), "成功注册", Toast.LENGTH_LONG).show();

//                startActivity(new Intent(this, test1.class));
                finish();
//                Intent resultIntent = new Intent(getBaseContext(), DetailActivity.class);
//                resultIntent.putExtra("user", result);
//                setResult(RESULT_OK, resultIntent);
//                finish();
            } else {
                Toast.makeText(getBaseContext(), "该用户已经存在", Toast.LENGTH_LONG).show();
            }

        }
    }



    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
