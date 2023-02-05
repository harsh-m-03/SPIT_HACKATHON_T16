package com.example.spit_hackathon_ecoquest.BottomSheets;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.spit_hackathon_ecoquest.Models.Quiz;
import com.example.spit_hackathon_ecoquest.Modules.SingleTapClick;
import com.example.spit_hackathon_ecoquest.UserPage;
import com.example.spit_hackathon_ecoquest.databinding.FragmentQuizBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class QuizBottomSheet extends BottomSheetDialogFragment {
    FragmentQuizBottomSheetBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    GestureDetector gestureDetector;
    String emptyFieldError = "Required";
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentQuizBottomSheetBinding.inflate(inflater, container, false);

        Initialization();

        binding.option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                checkOption(binding.option1.getText().toString());
            }
        });
        binding.option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkOption(binding.option2.getText().toString());
            }
        });
        binding.option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkOption(binding.option3.getText().toString());
            }
        });
        binding.option4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkOption(binding.option4.getText().toString());
            }
        });

        database.getReference().child("Test/Quiz").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Quiz> eventsList = new ArrayList<>();
                for (DataSnapshot s : snapshot.getChildren()) {
                    eventsList.add(s.getValue(Quiz.class));
                }
                if (eventsList.size() == 0) {
                    Toast.makeText(getContext(), "Empty", Toast.LENGTH_SHORT).show();
                } else {
                    Quiz randomQuiz = eventsList.get(0);
                    binding.question.setText(randomQuiz.getQuestion());
                    binding.option1.setText(randomQuiz.getOption1());
                    binding.option2.setText(randomQuiz.getOption2());
                    binding.option3.setText(randomQuiz.getOption3());
                    binding.option4.setText(randomQuiz.getOption4());
                    binding.correctOption.setText(randomQuiz.getCorrectOption());
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return binding.getRoot();
    }

    void checkOption(String option) {
        String correct = binding.correctOption.getText().toString();

        if (option.equals(correct)) {
            database.getReference().child("Test/Users").child(auth.getUid()).child("greenPoints")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int currentPoints = Integer.parseInt(snapshot.getValue(String.class));
                            currentPoints += 25;
                            database.getReference().child("Test/Users").child(auth.getUid())
                                    .child("greenPoints").setValue(String.valueOf(currentPoints));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

            Toast.makeText(getContext(), "Hurray, Correct answer. 25 Green Points added!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Oops, You selected the wrong option", Toast.LENGTH_SHORT).show();
        }
        QuizBottomSheet.this.dismiss();
        database.getReference().child("Test/ShowQuiz").setValue("no");
        Intent intent = new Intent(getContext(), UserPage.class);
        startActivity(intent);

    }

    void Initialization() {

        auth = FirebaseAuth.getInstance();

        gestureDetector = new GestureDetector(getContext(), new SingleTapClick());

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading");

        database = FirebaseDatabase.getInstance();
    }
}