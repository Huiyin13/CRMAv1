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

import com.example.crmav1.ManageAccount.StudentAccountInterface;
import com.example.crmav1.ManageLoginandRegistration.StudentMainInterface;
import com.example.crmav1.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SBookingCancelReasonInterface extends AppCompatActivity {
    private Button save,cancel;
    private EditText reason;
    private String bid,cancelreason, coId, cid;

    ProgressDialog progressDialog;

    private FirebaseDatabase db;
    private FirebaseUser user;
    private DatabaseReference book, book2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sbooking_cancel_reason);

        save = findViewById(R.id.aSave);
        cancel = findViewById(R.id.aCancel);
        reason = findViewById(R.id.rReason);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving....");

        bid = getIntent().getStringExtra("bid");
        coId = getIntent().getStringExtra("coId");
        cid = getIntent().getStringExtra("cid");

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2cancel = new Intent(SBookingCancelReasonInterface.this, SBookingDetailsInterface.class);
                intent2cancel.putExtra("bid",bid);
                intent2cancel.putExtra("coId", coId);
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
                book = db.getReference("Booking").child(user.getUid()).child(bid);
                book2 = db.getReference("Car").child(coId).child(cid).child("Booking").child(user.getUid()).child(bid);

                book.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (reason.getText().length()!=0){
                            cancelreason = reason.getText().toString();
                            progressDialog.show();
                            book.child("bStatus").setValue("Cancelled");
                            book.child("bCancelReason").setValue(cancelreason);
                            book2.child("bStatus").setValue("Cancelled");
                            book2.child("bCancelReason").setValue(cancelreason);
                            Toast.makeText(SBookingCancelReasonInterface.this, "You have cancelled this booking.", Toast.LENGTH_SHORT).show() ;
                            Intent intent2reject = new Intent(SBookingCancelReasonInterface.this, BookingListInterface.class);
                            startActivity(intent2reject);
                            finish();
                        }else {
                            Toast.makeText(SBookingCancelReasonInterface.this, "All field must be filled!", Toast.LENGTH_SHORT).show();
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
                        startActivity(new Intent(getApplicationContext(), BookingListInterface.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(SBookingCancelReasonInterface.this, StudentMainInterface.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.account:
                        startActivity(new Intent(SBookingCancelReasonInterface.this, StudentAccountInterface.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
}