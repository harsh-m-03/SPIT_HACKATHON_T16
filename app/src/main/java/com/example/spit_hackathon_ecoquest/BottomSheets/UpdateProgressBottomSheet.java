package com.example.spit_hackathon_ecoquest.BottomSheets;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.spit_hackathon_ecoquest.Models.Waste;
import com.example.spit_hackathon_ecoquest.Modules.OnPressUI;
import com.example.spit_hackathon_ecoquest.Modules.SingleTapClick;
import com.example.spit_hackathon_ecoquest.UserPage;
import com.example.spit_hackathon_ecoquest.databinding.FragmentUpdateProgressBottomSheetBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;


public class UpdateProgressBottomSheet extends BottomSheetDialogFragment {
    FragmentUpdateProgressBottomSheetBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    GestureDetector gestureDetector;
    String emptyFieldError = "Required";
    ProgressDialog progressDialog;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUpdateProgressBottomSheetBinding.inflate(inflater, container, false);

        Initialization();

        SimpleDateFormat firebaseDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String date = firebaseDateFormat.format(new Date());


        binding.login.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                new OnPressUI().onPressUi(view, motionEvent);
                if (gestureDetector.onTouchEvent(motionEvent)) {
                    if (binding.dryWaste.getText().toString().isEmpty())
                        binding.dryWaste.setError(emptyFieldError);
                    else if (binding.wetWaste.getText().toString().isEmpty())
                        binding.wetWaste.setError(emptyFieldError);
                    else if (binding.others.getText().toString().isEmpty())
                        binding.others.setError(emptyFieldError);
                    else {
                        progressDialog.show();
                        Waste waste = new Waste();
                        waste.setDryWaste(binding.dryWaste.getText().toString());
                        waste.setWetWaste(binding.wetWaste.getText().toString());
                        waste.setOthers(binding.others.getText().toString());

                        database.getReference().child("Test/Waste").child(date).child(auth.getUid()).setValue(waste)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressDialog.dismiss();
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getContext(), "Data updated", Toast.LENGTH_SHORT).show();
                                            Intent intent=new Intent(getContext(), UserPage.class);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(getContext(), task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }
                return true;
            }
        });

        return binding.getRoot();
    }

    void Initialization() {

        auth = FirebaseAuth.getInstance();

        gestureDetector = new GestureDetector(getContext(), new SingleTapClick());

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading");

        database = FirebaseDatabase.getInstance();
    }
}