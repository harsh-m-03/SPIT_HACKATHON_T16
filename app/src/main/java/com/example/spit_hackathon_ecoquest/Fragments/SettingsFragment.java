package com.example.spit_hackathon_ecoquest.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.spit_hackathon_ecoquest.ActivitySignIn;
import com.example.spit_hackathon_ecoquest.R;
import com.example.spit_hackathon_ecoquest.databinding.FragmentFileAComplaintBottomSheetBinding;
import com.example.spit_hackathon_ecoquest.databinding.FragmentSettingsBinding;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends Fragment {
    FragmentSettingsBinding binding;
    FirebaseAuth auth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater, container, false);

        auth=FirebaseAuth.getInstance();

        binding.logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Toast.makeText(getContext(), "Sign out successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), ActivitySignIn.class);
                startActivity(intent);
            }
        });

        return binding.getRoot();
    }
}