package com.example.spit_hackathon_ecoquest.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.spit_hackathon_ecoquest.Adapters.ItemAdapter;
import com.example.spit_hackathon_ecoquest.Adapters.OrderAdapter;
import com.example.spit_hackathon_ecoquest.HaraBazarActivity;
import com.example.spit_hackathon_ecoquest.Models.Item;
import com.example.spit_hackathon_ecoquest.Models.Orders;
import com.example.spit_hackathon_ecoquest.Modules.SingleTapClick;
import com.example.spit_hackathon_ecoquest.databinding.FragmentWeeklyReportBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WeeklyReportFragment extends Fragment {
    FragmentWeeklyReportBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    GestureDetector gestureDetector;
    ProgressDialog progressDialog;
    OrderAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentWeeklyReportBinding.inflate(inflater, container, false);
        Initialization();

        progressDialog.show();
        database.getReference().child("Test/Orders").child(auth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Orders> eventsList = new ArrayList<>();
                for (DataSnapshot s : snapshot.getChildren()) {
                    eventsList.add(s.getValue(Orders.class));
                }

                binding.recyclerView.setHasFixedSize(true);
                binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                if (eventsList.size() == 0) {
                    Toast.makeText(getContext(), "Empty", Toast.LENGTH_SHORT).show();
                }
                adapter = new OrderAdapter(getContext(), eventsList);
                adapter.notifyDataSetChanged();
                binding.recyclerView.setAdapter(adapter);
                progressDialog.hide();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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