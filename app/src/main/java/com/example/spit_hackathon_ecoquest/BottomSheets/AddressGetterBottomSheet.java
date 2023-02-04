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

import com.example.spit_hackathon_ecoquest.Models.Orders;
import com.example.spit_hackathon_ecoquest.Modules.OnPressUI;
import com.example.spit_hackathon_ecoquest.Modules.SingleTapClick;
import com.example.spit_hackathon_ecoquest.databinding.FragmentAddressGetterBottomSheetBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class AddressGetterBottomSheet extends BottomSheetDialogFragment {
    FragmentAddressGetterBottomSheetBinding binding;
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
        binding = FragmentAddressGetterBottomSheetBinding.inflate(inflater, container, false);

        Initialization();

        String name = getArguments().getString("name", "");
        String id = getArguments().getString("id", "");
        String price = getArguments().getString("price", "");


        binding.confirm.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                new OnPressUI().onPressUi(view, motionEvent);
                if (gestureDetector.onTouchEvent(motionEvent)) {
                    if (binding.address.getText().toString().isEmpty())
                        binding.address.setError(emptyFieldError);
                    else {
                        progressDialog.show();
                        Orders orders = new Orders();
                        orders.setAddress(binding.address.getText().toString());
                        orders.setName(name);
                        orders.setUid(id);
                        orders.setPrice(price);
                        orders.setUserID(auth.getUid());

                        database.getReference().child("Test/Orders").child(auth.getUid()).child(orders.getUid())
                                .setValue(orders).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressDialog.dismiss();
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getContext(), "Order Placed", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
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