package com.example.spit_hackathon_ecoquest.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.Carousel;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Toast;

import com.example.spit_hackathon_ecoquest.Adapters.EventAdapter;
import com.example.spit_hackathon_ecoquest.Adapters.TaskAdapter;
import com.example.spit_hackathon_ecoquest.Models.Events;
import com.example.spit_hackathon_ecoquest.Models.Task;
import com.example.spit_hackathon_ecoquest.Modules.SingleTapClick;
import com.example.spit_hackathon_ecoquest.R;
import com.example.spit_hackathon_ecoquest.databinding.FragmentProfileBinding;
import com.example.spit_hackathon_ecoquest.databinding.FragmentTaskBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TaskFragment extends Fragment {
    FragmentTaskBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    GestureDetector gestureDetector;
    ProgressDialog progressDialog;
    TaskAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTaskBinding.inflate(inflater, container, false);

        Initialization();

        database.getReference().child("Test/Task").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Task> taskList=new ArrayList<>();
                for (DataSnapshot s : snapshot.getChildren()) {
                    taskList.add(s.getValue(Task.class));
                }
                binding.recyclerView.setHasFixedSize(true);
                binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                if (taskList.size() == 0) {
                    Toast.makeText(getContext(), "Empty", Toast.LENGTH_SHORT).show();
                }
                adapter = new TaskAdapter(getContext(), taskList);
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