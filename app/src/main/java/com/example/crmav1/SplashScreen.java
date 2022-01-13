package com.example.crmav1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.crmav1.ManageLoginandRegistration.AdminMainInterface;
import com.example.crmav1.ManageLoginandRegistration.CarOwnerMainInterface;
import com.example.crmav1.ManageLoginandRegistration.LoginInterface;
import com.example.crmav1.ManageLoginandRegistration.StudentMainInterface;
import com.example.crmav1.Model.Admin;
import com.example.crmav1.Model.CarOwner;
import com.example.crmav1.Model.Student;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashScreen extends AppCompatActivity {

     private Animation topAnim, bottomAnim;
     private ImageView image;
     private TextView font;
     private static  int SPLASH_SCREEN = 3000;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private DatabaseReference dbRef, userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (getSupportActionBar() !=null){
            getSupportActionBar().hide();
        }

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        image = findViewById(R.id.logo);
        font = findViewById(R.id.font);
        firebaseAuth = FirebaseAuth.getInstance();
        image.setAnimation(topAnim);
        font.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                Intent intent = new Intent(SplashScreen.this, LoginInterface.class);
//                startActivity(intent);
//                finish();
                user = FirebaseAuth.getInstance().getCurrentUser();
                if (user !=null){
                    userRef = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                Student std = snapshot.getValue(Student.class);
                                CarOwner co = snapshot.getValue(CarOwner.class);
                                Admin admin = snapshot.getValue(Admin.class);

                                if (std.getUserType().equals("Student")){
                                    if(firebaseAuth.getCurrentUser().isEmailVerified()) {
                                        Intent intent2main = new Intent(SplashScreen.this, StudentMainInterface.class);
                                        intent2main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent2main);
                                        finish();
                                    }
                                }

                                else if (co.getUserType().equalsIgnoreCase("Car Owner")){
                                    if(firebaseAuth.getCurrentUser().isEmailVerified()) {
                                        Intent intent2main = new Intent(SplashScreen.this, CarOwnerMainInterface.class);
                                        intent2main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent2main);
                                        finish();
                                    }
                                }
                                else  if (admin.getUserType().equalsIgnoreCase("Admin")){
                                    if(firebaseAuth.getCurrentUser().isEmailVerified()){
                                        Intent intent2main = new Intent(SplashScreen.this, AdminMainInterface.class);
                                        intent2main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent2main);
                                        finish();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else
                {
                    startActivity(new Intent(SplashScreen.this, LoginInterface.class));
                    finish();
                }
            }
        },SPLASH_SCREEN);


    }
}