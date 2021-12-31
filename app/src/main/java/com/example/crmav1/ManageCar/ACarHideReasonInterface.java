package com.example.crmav1.ManageCar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.crmav1.ManageAccount.CarOwnerListInterface;
import com.example.crmav1.ManageAccount.CarOwnerRejectInterface;
import com.example.crmav1.ManageLoginandRegistration.AdminMainInterface;
import com.example.crmav1.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ACarHideReasonInterface extends AppCompatActivity {

    private Button hide, cancel;
    private EditText reason;
    private String cid, uid, rejectReason;

    private FirebaseDatabase db;
    private DatabaseReference carRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acar_hide_reason_interface);

        hide = findViewById(R.id.aSave);
        cancel = findViewById(R.id.aCancel);
        reason = findViewById(R.id.hReason);

        cid = getIntent().getStringExtra("cid");
        uid = getIntent().getStringExtra("uid");
        carRef = FirebaseDatabase.getInstance().getReference("Car").child(uid).child(cid);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2cancel = new Intent(ACarHideReasonInterface.this, ACarDetailsInterface.class);
                intent2cancel.putExtra("uid", uid);
                intent2cancel.putExtra("cid", cid);
                startActivity(intent2cancel);

            }
        });

        hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (reason.length() == 0)
                {
                    Toast.makeText(ACarHideReasonInterface.this, "Reason Is Needed", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    hideCar();
                }
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

    private void hideCar() {
        rejectReason = reason.getText().toString();
        carRef.child("cHideReason").setValue(rejectReason)
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                carRef.child("cStatus").setValue("Hide");
                Toast.makeText(ACarHideReasonInterface.this, "Updated Reason and Hidden", Toast.LENGTH_SHORT).show();
                Intent intent2cancel = new Intent(ACarHideReasonInterface.this, ACarDetailsInterface.class);
                intent2cancel.putExtra("uid", uid);
                intent2cancel.putExtra("cid", cid);
                startActivity(intent2cancel);
            }
        });
    }
}