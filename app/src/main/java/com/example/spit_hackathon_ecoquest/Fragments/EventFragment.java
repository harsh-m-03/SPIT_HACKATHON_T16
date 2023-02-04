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

import com.example.spit_hackathon_ecoquest.Adapters.EventAdapter;
import com.example.spit_hackathon_ecoquest.Models.Events;
import com.example.spit_hackathon_ecoquest.Modules.SingleTapClick;
import com.example.spit_hackathon_ecoquest.databinding.FragmentEventBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EventFragment extends Fragment {
    FragmentEventBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    GestureDetector gestureDetector;
    ProgressDialog progressDialog;
    EventAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEventBinding.inflate(inflater, container, false);
        Initialization();

        progressDialog.show();
        database.getReference().child("Test/Events").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Events> eventsList=new ArrayList<>();
                for (DataSnapshot s : snapshot.getChildren()) {
                    eventsList.add(s.getValue(Events.class));
                }

                binding.recyclerView.setHasFixedSize(true);
                binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                if (eventsList.size() == 0) {
                    Toast.makeText(getContext(), "Empty", Toast.LENGTH_SHORT).show();
                }
                adapter = new EventAdapter(getContext(), eventsList);
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