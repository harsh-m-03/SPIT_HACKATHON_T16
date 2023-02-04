package com.example.spit_hackathon_ecoquest.BottomSheets;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.spit_hackathon_ecoquest.R;

public class TaskProofBottomSheet extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_proof_bottom_sheet, container, false);
    }
}