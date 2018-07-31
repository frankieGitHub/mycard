package com.mycard.mycard;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.sirvar.robin.RobinActivity;
import com.sirvar.robin.Theme;


public class LoginActivity extends RobinActivity {

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
    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
