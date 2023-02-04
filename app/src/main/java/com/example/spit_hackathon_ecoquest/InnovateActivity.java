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
import com.example.spit_hackathon_ecoquest.Models.InnovateTask;
import com.example.spit_hackathon_ecoquest.Modules.OnPressUI;
import com.example.spit_hackathon_ecoquest.Modules.SingleTapClick;
import com.example.spit_hackathon_ecoquest.databinding.ActivityInnovateBinding;
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
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

public class InnovateActivity extends AppCompatActivity {
    ActivityInnovateBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage firebaseStorage;
    GestureDetector gestureDetector;
    ProgressDialog progressDialog;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_innovate);

        Initialization();

        binding.uploadImage.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                new OnPressUI().onPressUi(view, motionEvent);
                if (gestureDetector.onTouchEvent(motionEvent)) {
                    ImagePicker.Companion.with(InnovateActivity.this)
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

        binding.share.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                new OnPressUI().onPressUi(view, motionEvent);
                if (gestureDetector.onTouchEvent(motionEvent)) {
                    if (binding.task.getText().toString().isEmpty())
                        binding.task.setError("Required");
                    else if (uri == null)
                        Toast.makeText(InnovateActivity.this, "Upload an image", Toast.LENGTH_SHORT).show();
                    else {
                        SimpleDateFormat firebaseDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        String date = firebaseDateFormat.format(new Date());
                        Toast.makeText(InnovateActivity.this, "Uploaded to firestore", Toast.LENGTH_SHORT).show();
                        Calendar calendar = Calendar.getInstance();
                        String mili=String.valueOf(calendar.getTimeInMillis());
                        progressDialog.show();
                        StorageReference storageReference = firebaseStorage.getReference().child("Task Images")
                                .child(date + auth.getUid()+mili);
                        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();
                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                                        whatsappIntent.setType("text/plain");
                                        whatsappIntent.setPackage("com.whatsapp");
                                        whatsappIntent.putExtra(Intent.EXTRA_TEXT, binding.task.getText().toString()+"\n"+uri.toString());
                                        whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                                        try {
                                            startActivity(whatsappIntent);
                                        } catch (android.content.ActivityNotFoundException ex) {
                                        }
                                    }
                                });
                            }
                        });
                    }

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
        binding = ActivityInnovateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(InnovateActivity.this);
        progressDialog.setMessage("Loading");

        database = FirebaseDatabase.getInstance();

        gestureDetector = new GestureDetector(InnovateActivity.this, new SingleTapClick());

        auth = FirebaseAuth.getInstance();

        firebaseStorage = FirebaseStorage.getInstance();

        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

}