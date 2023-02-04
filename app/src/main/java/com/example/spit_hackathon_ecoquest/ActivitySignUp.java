package com.example.spit_hackathon_ecoquest;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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

import com.example.spit_hackathon_ecoquest.Models.Users;
import com.example.spit_hackathon_ecoquest.Modules.OnPressUI;
import com.example.spit_hackathon_ecoquest.Modules.SingleTapClick;
import com.example.spit_hackathon_ecoquest.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class ActivitySignUp extends AppCompatActivity {
    ActivitySignUpBinding binding;
    ProgressDialog progressDialog;
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
        binding.goToLogin.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                new OnPressUI().onPressUi(view, motionEvent);
                if (gestureDetector.onTouchEvent(motionEvent)) {
                    Intent intent = new Intent(ActivitySignUp.this, ActivitySignIn.class);
                    startActivity(intent);
                }
                return true;
            }
        });

        binding.login.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                new OnPressUI().onPressUi(view, motionEvent);
                if (gestureDetector.onTouchEvent(motionEvent)) {
                    if (binding.fName.getText().toString().isEmpty())
                        binding.fName.setError(emptyFieldError);
                    else if (binding.lName.getText().toString().isEmpty())
                        binding.lName.setError(emptyFieldError);
                    else if (binding.phoneNumber.getText().toString().isEmpty())
                        binding.phoneNumber.setError(emptyFieldError);
                    else if (binding.email.getText().toString().isEmpty())
                        binding.email.setError(emptyFieldError);
                    else if (binding.pass.getText().toString().isEmpty())
                        binding.pass.setError(emptyFieldError);
                    else {
                        progressDialog.show();
                        auth.createUserWithEmailAndPassword(binding.email.getText().toString(),
                                binding.pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    Users users = new Users();
                                    users.setfName(binding.fName.getText().toString());
                                    users.setlName(binding.lName.getText().toString());
                                    users.setPhoneNumber(binding.phoneNumber.getText().toString());
                                    users.setEmail(binding.email.getText().toString());
                                    users.setPassword(binding.pass.getText().toString());

                                    progressDialog.show();
                                    firebaseDatabase.getReference().child(dbType + "/Users/").child(auth.getUid()).setValue(users).
                                            addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    progressDialog.dismiss();
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(ActivitySignUp.this, "Login successful", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(ActivitySignUp.this, UserPage.class);
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
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(ActivitySignUp.this);
        progressDialog.setMessage("Loading");

        firebaseDatabase = FirebaseDatabase.getInstance();

        gestureDetector = new GestureDetector(ActivitySignUp.this, new SingleTapClick());

        auth = FirebaseAuth.getInstance();

        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}