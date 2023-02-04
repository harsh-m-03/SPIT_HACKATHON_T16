package com.example.spit_hackathon_ecoquest.BottomSheets;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.spit_hackathon_ecoquest.Modules.SingleTapClick;
import com.example.spit_hackathon_ecoquest.R;
import com.example.spit_hackathon_ecoquest.databinding.FragmentProfileBinding;
import com.example.spit_hackathon_ecoquest.databinding.FragmentUpdateProgressBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class UpdateProgressBottomSheet extends BottomSheetDialogFragment {
    FragmentUpdateProgressBottomSheetBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    GestureDetector gestureDetector;
    ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUpdateProgressBottomSheetBinding.inflate(inflater, container, false);

        Initialization();

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