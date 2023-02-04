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

import com.example.spit_hackathon_ecoquest.Models.Complaint;
import com.example.spit_hackathon_ecoquest.Modules.OnPressUI;
import com.example.spit_hackathon_ecoquest.Modules.SingleTapClick;
import com.example.spit_hackathon_ecoquest.databinding.FragmentFileAComplaintBottomSheetBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;


public class FileAComplaintBottomSheet extends BottomSheetDialogFragment {
    FragmentFileAComplaintBottomSheetBinding binding;
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
        binding = FragmentFileAComplaintBottomSheetBinding.inflate(inflater, container, false);

        Initialization();

        binding.add.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                new OnPressUI().onPressUi(view, motionEvent);
                if (gestureDetector.onTouchEvent(motionEvent)) {
                    if (binding.location.getText().toString().isEmpty())
                        binding.location.setError(emptyFieldError);
                    else if (binding.date.getText().toString().isEmpty())
                        binding.date.setError(emptyFieldError);
                    else if (binding.desc.getText().toString().isEmpty())
                        binding.desc.setError(emptyFieldError);
                    else {
                        progressDialog.show();
                        Calendar calendar = Calendar.getInstance();
                        String time = String.valueOf(calendar.getTimeInMillis());
                        Complaint complaint = new Complaint();
                        complaint.setDate(binding.date.getText().toString());
                        complaint.setLocation(binding.location.getText().toString());
                        complaint.setDescription(binding.desc.getText().toString());
                        complaint.setUid(time);

                        database.getReference().child("Test/Complaint").child(time).setValue(complaint)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressDialog.dismiss();
                                        if (task.isSuccessful()) {
                                            FileAComplaintBottomSheet.this.dismiss();
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