package com.example.crmav1.ManageBooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.crmav1.ManageAccount.CarOwnerAccountInterface;
import com.example.crmav1.ManageAccount.CarOwnerListInterface;
import com.example.crmav1.ManageAccount.CarOwnerRejectInterface;
import com.example.crmav1.ManageAccount.CarOwnerViewInterface;
import com.example.crmav1.ManageLoginandRegistration.CarOwnerMainInterface;
import com.example.crmav1.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class CBookingRejectInterface extends AppCompatActivity {

    private Button save,cancel;
    private EditText reason;
    private String bid,rejectReason, sId, cid;

    ProgressDialog progressDialog;

    private FirebaseDatabase db;
    private FirebaseUser user;
    private DatabaseReference book, book2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cbooking_reject_interface);

        save = findViewById(R.id.aSave);
        cancel = findViewById(R.id.aCancel);
        reason = findViewById(R.id.rReason);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving....");

        bid = getIntent().getStringExtra("bid");
        sId = getIntent().getStringExtra("sId");
        cid = getIntent().getStringExtra("cid");

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2cancel = new Intent(CBookingRejectInterface.this, CBookingDetailsInterface.class);
                intent2cancel.putExtra("bid",bid);
                intent2cancel.putExtra("sId", sId);
                intent2cancel.putExtra("cid", cid);
                startActivity(intent2cancel);
                finish();
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = FirebaseDatabase.getInstance();
                book = db.getReference("Booking").child(sId).child(bid);
                book2 = db.getReference("Car").child(user.getUid()).child(cid).child("Booking").child(sId).child(bid);

                book.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                       if (reason.getText().length()!=0){
                           rejectReason = reason.getText().toString();
                           progressDialog.show();
                           book.child("bStatus").setValue("Rejected");
                           book.child("bRejectReason").setValue(rejectReason);
                           book2.child("bStatus").setValue("Rejected");
                           book2.child("bRejectReason").setValue(rejectReason);
                           Toast.makeText(CBookingRejectInterface.this, "You have rejected this booking.", Toast.LENGTH_SHORT).show() ;
                           Intent intent2reject = new Intent(CBookingRejectInterface.this, CBookingListInterface.class);
                           startActivity(intent2reject);
                           finish();
                       }else {
                           Toast.makeText(CBookingRejectInterface.this, "All field must be filled!", Toast.LENGTH_SHORT).show();
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
                    case R.id.book:
                        startActivity(new Intent(getApplicationContext(), CBookingListInterface.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(CBookingRejectInterface.this, CarOwnerMainInterface.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.account:
                        startActivity(new Intent(CBookingRejectInterface.this, CarOwnerAccountInterface.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

    }
}