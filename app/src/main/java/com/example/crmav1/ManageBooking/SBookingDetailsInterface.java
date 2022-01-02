package com.example.crmav1.ManageBooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.crmav1.ManageAccount.StudentAccountInterface;
import com.example.crmav1.ManageChat.ChatInterface;
import com.example.crmav1.ManageLoginandRegistration.StudentMainInterface;
import com.example.crmav1.ManagePayment.PaymentSelectionInterface;
import com.example.crmav1.Model.Booking;
import com.example.crmav1.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SBookingDetailsInterface extends AppCompatActivity {

    private TextView timeF, timeT, dateF, dateT, status, payment, memo;
    private ImageView chat;
    private Button pay, viewCo, cancel, complete;

    private FirebaseUser user;
    private DatabaseReference display;

    private String coId, bid, fee, cid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sbooking_details_interface);

        chat = findViewById(R.id.chat);
        pay = findViewById(R.id.payment);
        timeF = findViewById(R.id.timeF);
        timeT = findViewById(R.id.timeT);
        dateF = findViewById(R.id.dateF);
        dateT = findViewById(R.id.dateT);
        status = findViewById(R.id.status);
        payment = findViewById(R.id.ttlPayment);
        memo = findViewById(R.id.memo);
        viewCo = findViewById(R.id.viewC);
        cancel = findViewById(R.id.cancel);
        complete = findViewById(R.id.complete);

        coId = getIntent().getStringExtra("coId");
        bid = getIntent().getStringExtra("bid");

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2chat = new Intent(SBookingDetailsInterface.this, ChatInterface.class);
                intent2chat.putExtra("uid", coId);
                startActivity(intent2chat);
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        display = FirebaseDatabase.getInstance().getReference("Booking").child(user.getUid()).child(bid);

        display.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Booking booking = snapshot.getValue(Booking.class);
                    fee = booking.getTtlFee();
                    cid = booking.getCid();
                    timeF.setText(booking.getTimeF());
                    timeT.setText(booking.getTimeT());
                    dateF.setText(booking.getDateF());
                    dateT.setText(booking.getDateT());
                    status.setText(booking.getbStatus());
                    payment.setText("RM " +booking.getTtlFee());
                    memo.setText(booking.getMemo());

                    if (booking.getbStatus().equalsIgnoreCase("Applying") || booking.getbStatus().equalsIgnoreCase("Accepted")){
                        complete.setVisibility(View.GONE);
                        pay.setVisibility(View.GONE);
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });
                    }
                    else if (booking.getbStatus().equalsIgnoreCase("Pick Up")){
                        cancel.setVisibility(View.GONE);
                        pay.setVisibility(View.GONE);
                        complete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });
                    }
                    else  if (booking.getbStatus().equalsIgnoreCase("Completed")){
                        cancel.setVisibility(View.GONE);
                        complete.setVisibility(View.GONE);
                        pay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent2payment = new Intent(SBookingDetailsInterface.this, PaymentSelectionInterface.class);
                                intent2payment.putExtra("bid", bid);
                                intent2payment.putExtra("fee", fee);
                                intent2payment.putExtra("coId", coId);
                                intent2payment.putExtra("cid", cid);
                                startActivity(intent2payment);
                            }
                        });
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
                        startActivity(new Intent(SBookingDetailsInterface.this, StudentMainInterface.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.account:
                        startActivity(new Intent(SBookingDetailsInterface.this, StudentAccountInterface.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
}