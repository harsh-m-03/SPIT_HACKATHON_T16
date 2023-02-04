package com.example.spit_hackathon_ecoquest.Fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.spit_hackathon_ecoquest.BottomSheets.OptionsForCleanArea;
import com.example.spit_hackathon_ecoquest.BottomSheets.UpdateProgressBottomSheet;
import com.example.spit_hackathon_ecoquest.InnovateActivity;
import com.example.spit_hackathon_ecoquest.Models.Users;
import com.example.spit_hackathon_ecoquest.Models.Waste;
import com.example.spit_hackathon_ecoquest.Modules.FcmNotificationsSender;
import com.example.spit_hackathon_ecoquest.Modules.FirebaseMessagingService;
import com.example.spit_hackathon_ecoquest.Modules.OnPressUI;
import com.example.spit_hackathon_ecoquest.Modules.SingleTapClick;
import com.example.spit_hackathon_ecoquest.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    GestureDetector gestureDetector;
    ProgressDialog progressDialog;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        Initialization();
        SimpleDateFormat firebaseDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String date = firebaseDateFormat.format(new Date());

        binding.dryWasteText.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                new OnPressUI().onPressUi(view, motionEvent);
                if (gestureDetector.onTouchEvent(motionEvent)) {
                    Snackbar snackbar = Snackbar.make(binding.layout,
                            "Dry Waste: " + binding.dryWaste.getText().toString(),
                            Snackbar.LENGTH_LONG);
                    snackbar.setBackgroundTint(Color.parseColor("#00ab41"));//green
                    snackbar.setTextColor(Color.parseColor("#FFFFFF"));//white
                    snackbar.show();
                }
                return true;
            }
        });
        binding.wetWasteText.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                new OnPressUI().onPressUi(view, motionEvent);
                if (gestureDetector.onTouchEvent(motionEvent)) {
                    Snackbar snackbar = Snackbar.make(binding.layout,
                            "Wet Waste: " + binding.wetWaste.getText().toString(),
                            Snackbar.LENGTH_LONG);
                    snackbar.setBackgroundTint(Color.parseColor("#00ab41"));//green
                    snackbar.setTextColor(Color.parseColor("#FFFFFF"));//white
                    snackbar.show();
                }
                return true;
            }
        });
        binding.innovate.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                new OnPressUI().onPressUi(view, motionEvent);
                if (gestureDetector.onTouchEvent(motionEvent)) {
                    Intent intent = new Intent(getContext(), InnovateActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });
        binding.othersText.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                new OnPressUI().onPressUi(view, motionEvent);
                if (gestureDetector.onTouchEvent(motionEvent)) {
                    Snackbar snackbar = Snackbar.make(binding.layout,
                            "Others: " + binding.others.getText().toString(),
                            Snackbar.LENGTH_LONG);
                    snackbar.setBackgroundTint(Color.parseColor("#00ab41"));//green
                    snackbar.setTextColor(Color.parseColor("#FFFFFF"));//white
                    snackbar.show();
                }
                return true;
            }
        });
        binding.updateProgress.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                new OnPressUI().onPressUi(view, motionEvent);
                if (gestureDetector.onTouchEvent(motionEvent)) {
                    UpdateProgressBottomSheet frag = new UpdateProgressBottomSheet();
                    frag.show(getActivity().getSupportFragmentManager(), frag.getTag());
                }
                return true;
            }
        });
        binding.dailyTask.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                new OnPressUI().onPressUi(view, motionEvent);
                if (gestureDetector.onTouchEvent(motionEvent)) {

                }
                return true;
            }
        });  binding.cleanMyArea.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                new OnPressUI().onPressUi(view, motionEvent);
                if (gestureDetector.onTouchEvent(motionEvent)) {
                    OptionsForCleanArea frag = new OptionsForCleanArea();
                    frag.show(getActivity().getSupportFragmentManager(), frag.getTag());
                }
                return true;
            }
        });
        binding.notifications.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                FirebaseMessaging.getInstance().subscribeToTopic("all");
                FcmNotificationsSender notificationsSender = new FcmNotificationsSender("/topics/all", "Congratulations", "Winner for this Week is ", getActivity().getApplicationContext(), getActivity());
                notificationsSender.SendNotifications();

                return true;
            }
        });


        database.getReference().child("Test/Waste").child(date).child(Objects.requireNonNull(auth.getUid())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Waste waste = snapshot.getValue(Waste.class);
                assert waste != null;
                binding.dryWaste.setText(waste.getDryWaste() + " KG");
                binding.wetWaste.setText(waste.getWetWaste() + " KG");
                binding.others.setText(waste.getOthers() + " KG");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        database.getReference().child("Test/Users").child(Objects.requireNonNull(auth.getUid()))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Users users = snapshot.getValue(Users.class);
                        assert users != null;
                        binding.userFname.setText("Hello " + users.getfName() + ",");
                        binding.greenPoints.setText(" " + users.getGreenPoints());
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