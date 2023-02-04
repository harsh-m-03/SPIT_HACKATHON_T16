package com.example.spit_hackathon_ecoquest;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spit_hackathon_ecoquest.Models.CompletedTask;
import com.example.spit_hackathon_ecoquest.Modules.OnPressUI;
import com.example.spit_hackathon_ecoquest.Modules.SingleTapClick;
import com.example.spit_hackathon_ecoquest.databinding.ActivityTaskProofBinding;
import com.github.drjacky.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

public class TaskProof extends AppCompatActivity {
    ActivityTaskProofBinding binding;
    ProgressDialog progressDialog;
    GestureDetector gestureDetector;
    String emptyFieldError = "Required";
    String dbType = "Test";
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    FirebaseAuth auth;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Initialization();

        SimpleDateFormat firebaseDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String date = firebaseDateFormat.format(new Date());

        String day = getIntent().getStringExtra("day");
        String task = getIntent().getStringExtra("task");
        String green_points = getIntent().getStringExtra("green_points");

        binding.day.setText(day + "'s Task");
        binding.task.setText(task);
        binding.greenPoints.setText(green_points);

        binding.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.Companion.with(TaskProof.this)
                        .crop().cropSquare().cropOval()
                        .maxResultSize(512, 512, true)
                        .createIntentFromDialog((Function1) (new Function1() {
                            public Object invoke(Object var1) {
                                this.invoke((Intent) var1);
                                return Unit.INSTANCE;
                            }

                            public final void invoke(@NotNull Intent it) {
                                Intrinsics.checkNotNullParameter(it, "it");
                                launcher.launch(it);
                            }
                        }));
            }
        });

        binding.uploadImage.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                new OnPressUI().onPressUi(view, motionEvent);
                if (gestureDetector.onTouchEvent(motionEvent)) {
                    ImagePicker.Companion.with(TaskProof.this)
                            .crop().cropSquare().cropOval()
                            .maxResultSize(512, 512, true)
                            .createIntentFromDialog((Function1) (new Function1() {
                                public Object invoke(Object var1) {
                                    this.invoke((Intent) var1);
                                    return Unit.INSTANCE;
                                }

                                public final void invoke(@NotNull Intent it) {
                                    Intrinsics.checkNotNullParameter(it, "it");
                                    launcher.launch(it);
                                }
                            }));

                }
                return true;
            }
        });
        binding.submit.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                new OnPressUI().onPressUi(view, motionEvent);
                if (gestureDetector.onTouchEvent(motionEvent)) {
                    if (uri != null) {
                        progressDialog.show();
                        StorageReference storageReference = firebaseStorage.getReference().child("Task Images")
                                .child(date + auth.getUid());
                        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();
                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        progressDialog.show();
                                        Toast.makeText(TaskProof.this, "Uploaded to firestore", Toast.LENGTH_SHORT).show();
                                        CompletedTask completedTask = new CompletedTask();
                                        completedTask.setTask(task);
                                        completedTask.setDay(day);
                                        completedTask.setUser_id(auth.getUid());
                                        completedTask.setGreen_points(green_points);
                                        completedTask.setImage(uri.toString());

                                        firebaseDatabase.getReference().child("Test/Completed Task").child(date).child(auth.getUid())
                                                .setValue(completedTask).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        progressDialog.dismiss();
                                                        if (task.isSuccessful()) {
                                                            Intent intent = new Intent(TaskProof.this, UserPage.class);
                                                            startActivity(intent);
                                                            SharedPreferences sharedPreferences = TaskProof.this.getSharedPreferences("shared preferences", MODE_PRIVATE);
                                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                                            editor.putString(day, "false");
                                                            editor.commit();
                                                            progressDialog.show();
                                                            firebaseDatabase.getReference().child("Test/Users").child(auth.getUid()).
                                                                    child("greenPoints").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                            String user_greenPoint = snapshot.getValue(String.class);
                                                                            int newPoints = (Integer.parseInt(user_greenPoint) + Integer.parseInt(green_points));
                                                                            firebaseDatabase.getReference().child("Test/Users").child(auth.getUid()).
                                                                                    child("greenPoints").setValue(String.valueOf(newPoints)).
                                                                                    addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                            progressDialog.dismiss();
                                                                                            if (!task.isSuccessful())
                                                                                                Toast.makeText(TaskProof.this, "Green Point Error: " +
                                                                                                        task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                                                                                            else{
                                                                                                Toast.makeText(TaskProof.this, "Task completed and Green Points are updated", Toast.LENGTH_SHORT).show();
                                                                                            }
                                                                                        }
                                                                                    });
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                        }
                                                                    });

                                                        } else {
                                                            Toast.makeText(TaskProof.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }
                                });
                            }
                        });
                    } else
                        Toast.makeText(TaskProof.this, "Nahi", Toast.LENGTH_SHORT).show();

                }
                return true;
            }
        });

    }

    Uri uri = null;
    ActivityResultLauncher<Intent> launcher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), (ActivityResult result) -> {
                if (result.getResultCode() == RESULT_OK) {
                    uri = result.getData().getData();
                    binding.image.setImageURI(uri);
                    Toast.makeText(this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    // Use the uri to load the image
                } else if (result.getResultCode() == ImagePicker.RESULT_ERROR) {
                    Toast.makeText(this, ImagePicker.Companion.getError(result.getData()), Toast.LENGTH_SHORT).show();
                }
            });

    private void Initialization() {
        binding = ActivityTaskProofBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(TaskProof.this);
        progressDialog.setMessage("Loading");

        firebaseDatabase = FirebaseDatabase.getInstance();

        gestureDetector = new GestureDetector(TaskProof.this, new SingleTapClick());

        auth = FirebaseAuth.getInstance();

        firebaseStorage = FirebaseStorage.getInstance();

        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

}