package com.example.spit_hackathon_ecoquest;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.spit_hackathon_ecoquest.Adapters.EventAdapter;
import com.example.spit_hackathon_ecoquest.Adapters.ItemAdapter;
import com.example.spit_hackathon_ecoquest.Models.Item;
import com.example.spit_hackathon_ecoquest.Modules.SingleTapClick;
import com.example.spit_hackathon_ecoquest.databinding.ActivityHaraBazarBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HaraBazarActivity extends AppCompatActivity {
    ActivityHaraBazarBinding binding;
    ProgressDialog progressDialog;
    GestureDetector gestureDetector;
    String emptyFieldError = "Required";
    String dbType = "Test";
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    FirebaseAuth auth;
    ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Initialization();

        firebaseDatabase.getReference().child("Test/Item").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Item> eventsList = new ArrayList<>();
                for (DataSnapshot s : snapshot.getChildren()) {
                    eventsList.add(s.getValue(Item.class));
                }

                binding.recyclerView.setHasFixedSize(true);
                binding.recyclerView.setLayoutManager(new LinearLayoutManager(HaraBazarActivity.this));
                if (eventsList.size() == 0) {
                    Toast.makeText(HaraBazarActivity.this, "Empty", Toast.LENGTH_SHORT).show();
                }
                adapter = new ItemAdapter(HaraBazarActivity.this, eventsList);
                adapter.notifyDataSetChanged();
                binding.recyclerView.setAdapter(adapter);
                progressDialog.hide();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void Initialization() {
        binding = ActivityHaraBazarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(HaraBazarActivity.this);
        progressDialog.setMessage("Loading");

        firebaseDatabase = FirebaseDatabase.getInstance();

        gestureDetector = new GestureDetector(HaraBazarActivity.this, new SingleTapClick());

        auth = FirebaseAuth.getInstance();

        firebaseStorage = FirebaseStorage.getInstance();

        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}