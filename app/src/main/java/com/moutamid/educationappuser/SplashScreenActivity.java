package com.moutamid.educationappuser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.moutamid.educationappuser.ui.LoginActivity;
import com.moutamid.educationappuser.utilis.Constants;

public class SplashScreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(() -> {
            if (Constants.auth().getCurrentUser() != null){
                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                finish();
            } else {
                startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                finish();
            }
        }, 2000);

    }
}