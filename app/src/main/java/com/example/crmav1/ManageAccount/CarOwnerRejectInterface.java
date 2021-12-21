package com.example.crmav1.ManageAccount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.crmav1.ManageCar.ACarListInterface;
import com.example.crmav1.ManageLoginandRegistration.AdminMainInterface;
import com.example.crmav1.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class CarOwnerRejectInterface extends AppCompatActivity {
    private Button save,cancel;
    private EditText reason;
    private String uid,rejectReason;

    private FirebaseDatabase db;
    private DatabaseReference coDBRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_owner_reject_interface);

        this.setTitle("Registration");

        save = findViewById(R.id.aSave);
        cancel = findViewById(R.id.aCancel);
        reason = findViewById(R.id.coReason);

        uid = getIntent().getStringExtra("uid");

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2cancel = new Intent(CarOwnerRejectInterface.this, CarOwnerViewInterface.class);
                startActivity(intent2cancel);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = FirebaseDatabase.getInstance();
                coDBRef = db.getReference("Users");
                Query query = coDBRef.child("Users").orderByChild("uid").equalTo(uid);

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (reason.getText().length()!=0){
                            rejectReason = reason.getText().toString();

                            coDBRef.child(uid).child("coStatus").setValue("Rejected");
                            coDBRef.child(uid).child("reason").setValue(rejectReason);
                            Toast.makeText(CarOwnerRejectInterface.this, "You have reject the registration.", Toast.LENGTH_SHORT).show() ;
                            Intent intent2reject = new Intent(CarOwnerRejectInterface.this, CarOwnerListInterface.class);
                            startActivity(intent2reject);
                        }else {
                            Toast.makeText(CarOwnerRejectInterface.this, "All field must be filled!", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        //bottom nav
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.car:
                        startActivity(new Intent(getApplicationContext(), ACarListInterface.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.list:
                        startActivity(new Intent(getApplicationContext(), CarOwnerListInterface.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), AdminMainInterface.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
}