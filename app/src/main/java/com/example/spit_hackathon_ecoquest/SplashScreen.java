package com.example.spit_hackathon_ecoquest;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class SplashScreen extends AppCompatActivity {
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Thread thread = new Thread() {
            public void run() {
                try {
                    sleep(2350);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    auth = FirebaseAuth.getInstance();
                    if (auth.getCurrentUser() == null) {
                        Intent intent = new Intent(SplashScreen.this, ActivitySignUp.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(SplashScreen.this, UserPage.class);
                        startActivity(intent);
                    }
                }
            }
        };
        thread.start();
    }
}