package com.example.spit_hackathon_ecoquest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spit_hackathon_ecoquest.Modules.OnPressUI;
import com.example.spit_hackathon_ecoquest.Modules.SingleTapClick;
import com.example.spit_hackathon_ecoquest.databinding.ActivitySignInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class ActivitySignIn extends AppCompatActivity {
    ActivitySignInBinding binding;
    GestureDetector gestureDetector;
    String emptyFieldError = "Required";
    String dbType = "Test";
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth auth;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Initialization();

        binding.login.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                new OnPressUI().onPressUi(view, motionEvent);
                if (gestureDetector.onTouchEvent(motionEvent)) {
                    if (binding.email.getText().toString().isEmpty())
                        binding.email.setError(emptyFieldError);
                    else if (binding.pass.getText().toString().isEmpty())
                        binding.pass.setError(emptyFieldError);
                    else {
                        binding.loading.setVisibility(View.VISIBLE);
                        auth.signInWithEmailAndPassword(binding.email.getText().toString(),
                                binding.pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                binding.loading.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(ActivitySignIn.this, "Login successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ActivitySignIn.this, MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    Snackbar snackbar = Snackbar.make(binding.layout, "Error: " + task.getException().getMessage(),
                                            Snackbar.LENGTH_LONG);
                                    snackbar.setBackgroundTint(Color.parseColor("#FF0000"));//red
                                    snackbar.setTextColor(Color.parseColor("#FFFFFF"));//white
                                    snackbar.show();
                                }
                            }
                        });
                    }

                }
                return true;
            }
        });

    }

    private void Initialization() {
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseDatabase = FirebaseDatabase.getInstance();

        gestureDetector = new GestureDetector(ActivitySignIn.this, new SingleTapClick());

        auth = FirebaseAuth.getInstance();

        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}