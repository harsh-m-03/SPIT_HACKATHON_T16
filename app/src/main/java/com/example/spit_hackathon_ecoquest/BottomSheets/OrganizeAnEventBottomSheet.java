package com.example.spit_hackathon_ecoquest.BottomSheets;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.spit_hackathon_ecoquest.Models.Events;
import com.example.spit_hackathon_ecoquest.Modules.OnPressUI;
import com.example.spit_hackathon_ecoquest.Modules.SingleTapClick;
import com.example.spit_hackathon_ecoquest.databinding.FragmentOraganizeAnEventBottomSheetBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class OrganizeAnEventBottomSheet extends BottomSheetDialogFragment {
    FragmentOraganizeAnEventBottomSheetBinding binding;
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
        binding = FragmentOraganizeAnEventBottomSheetBinding.inflate(inflater, container, false);

        Initialization();

        binding.add.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                new OnPressUI().onPressUi(view, motionEvent);
                if (gestureDetector.onTouchEvent(motionEvent)) {
                    if (binding.title.getText().toString().isEmpty())
                        binding.title.setError(emptyFieldError);
                    else if (binding.location.getText().toString().isEmpty())
                        binding.location.setError(emptyFieldError);
                    else if (binding.time.getText().toString().isEmpty())
                        binding.time.setError(emptyFieldError);
                    else if (binding.date.getText().toString().isEmpty())
                        binding.date.setError(emptyFieldError);
                    else if (binding.greenPoints.getText().toString().isEmpty())
                        binding.greenPoints.setError(emptyFieldError);
                    else {
                        Events events = new Events();
                        events.setTitle(binding.title.getText().toString());
                        events.setLocation(binding.location.getText().toString());
                        events.setTime(binding.time.getText().toString());
                        events.setGreen_points(binding.greenPoints.getText().toString());
                        events.setDate(binding.date.getText().toString());

                        progressDialog.show();
                        Calendar calendar = Calendar.getInstance();
                        String time = String.valueOf(calendar.getTimeInMillis());
                        database.getReference().child("Test/Events").child(time).setValue(events)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressDialog.dismiss();
                                        if (task.isSuccessful()) {
                                            OrganizeAnEventBottomSheet.this.dismiss();
                                            Toast.makeText(getContext(), "Event added", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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