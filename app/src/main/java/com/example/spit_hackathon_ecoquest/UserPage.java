package com.example.spit_hackathon_ecoquest;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.spit_hackathon_ecoquest.BottomSheets.OrganizeAnEventBottomSheet;
import com.example.spit_hackathon_ecoquest.BottomSheets.QuizBottomSheet;
import com.example.spit_hackathon_ecoquest.databinding.ActivityUserPageBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class UserPage extends AppCompatActivity {

    FirebaseDatabase database;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityUserPageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUserPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setSupportActionBar(binding.appBarUserPage.toolbar);
        Objects.requireNonNull(getSupportActionBar()).hide();

        binding.appBarUserPage.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.drawerLayout.isDrawerOpen(GravityCompat.START))
                    binding.drawerLayout.openDrawer(GravityCompat.START);
                else binding.drawerLayout.closeDrawer(GravityCompat.END);
            }
        });

        database.getReference().child("Test/ShowQuiz").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String isShow=snapshot.getValue(String.class);
                if(isShow.equals("yes")){
                    QuizBottomSheet frag = new QuizBottomSheet();
                    frag.show(getSupportFragmentManager(), frag.getTag());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.profile, R.id.events, R.id.weekly_report, R.id.task, R.id.settings)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_user_page);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_page, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_user_page);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}