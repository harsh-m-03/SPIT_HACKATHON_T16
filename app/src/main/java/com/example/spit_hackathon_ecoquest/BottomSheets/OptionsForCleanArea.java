package com.example.spit_hackathon_ecoquest.BottomSheets;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.spit_hackathon_ecoquest.Modules.OnPressUI;
import com.example.spit_hackathon_ecoquest.Modules.SingleTapClick;
import com.example.spit_hackathon_ecoquest.databinding.FragmentOptionsForCleanAreaBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class OptionsForCleanArea extends BottomSheetDialogFragment {
    FragmentOptionsForCleanAreaBinding binding;
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
        binding = FragmentOptionsForCleanAreaBinding.inflate(inflater, container, false);

        Initialization();

        binding.organize.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                new OnPressUI().onPressUi(view, motionEvent);
                if (gestureDetector.onTouchEvent(motionEvent)) {
                    OrganizeAnEventBottomSheet frag = new OrganizeAnEventBottomSheet();
                    frag.show(getActivity().getSupportFragmentManager(), frag.getTag());
                    OptionsForCleanArea.this.dismiss();
                }
                return true;
            }
        });
        binding.complaint.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                new OnPressUI().onPressUi(view, motionEvent);
                if (gestureDetector.onTouchEvent(motionEvent)) {
                    FileAComplaintBottomSheet frag = new FileAComplaintBottomSheet();
                    frag.show(getActivity().getSupportFragmentManager(), frag.getTag());
                    OptionsForCleanArea.this.dismiss();
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