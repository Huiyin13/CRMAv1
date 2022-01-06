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

import com.example.crmav1.ManageAccount.CarOwnerAccountInterface;
import com.example.crmav1.ManageChat.ChatInterface;
import com.example.crmav1.ManageLoginandRegistration.CarOwnerMainInterface;
import com.example.crmav1.Model.Booking;
import com.example.crmav1.Model.Payment;
import com.example.crmav1.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CBookingDetailsInterface extends AppCompatActivity {

    private TextView timeF, timeT, dateF, dateT, status, payment, payMethod, tvCancel, cancelReason;
    private EditText memo;
    private ImageView chat;
    private Button viewS, reject, approve, pickup, paidReceive, viewCar;

    private FirebaseUser user;
    private DatabaseReference display, updateB, updateB2, paymentB;

    private String bid, fee, cid, sId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cbooking_details_interface);

        chat = findViewById(R.id.chat);
        timeF = findViewById(R.id.timeF);
        timeT = findViewById(R.id.timeT);
        dateF = findViewById(R.id.dateF);
        dateT = findViewById(R.id.dateT);
        status = findViewById(R.id.status);
        payment = findViewById(R.id.ttlPayment);
        memo = findViewById(R.id.memo);
        viewS = findViewById(R.id.viewS);
        reject = findViewById(R.id.reject);
        viewCar = findViewById(R.id.viewCar);
        payMethod = findViewById(R.id.paymentMethod);
        approve = findViewById(R.id.approve);
        pickup = findViewById(R.id.pickUp);
        paidReceive = findViewById(R.id.done);
        tvCancel = findViewById(R.id.tvcancel);
        cancelReason = findViewById(R.id.cancelReason);

        bid = getIntent().getStringExtra("bid");
        sId = getIntent().getStringExtra("sId");
        cid = getIntent().getStringExtra("cid");

        viewCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2view = new Intent(CBookingDetailsInterface.this, CCheckCarDetailsInterface.class);
                intent2view.putExtra("sId", sId);
                intent2view.putExtra("bid", bid);
                intent2view.putExtra("cid", cid);
                startActivity(intent2view);
            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2chat = new Intent(CBookingDetailsInterface.this, ChatInterface.class);
                intent2chat.putExtra("uid", sId);
                intent2chat.putExtra("bid", bid);
                intent2chat.putExtra("cid", cid);
                startActivity(intent2chat);
            }
        });

        viewS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2view = new Intent(CBookingDetailsInterface.this, CStudentProfileInterface.class);
                intent2view.putExtra("sId", sId);
                intent2view.putExtra("bid", bid);
                intent2view.putExtra("cid", cid);
                startActivity(intent2view);
            }
        });

        paymentB = FirebaseDatabase.getInstance().getReference("Payment");
        paymentB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                   for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                       Payment pay = dataSnapshot.getValue(Payment.class);
                       if (pay.getBid().equals(bid)){
                           payMethod.setText(pay.getPaymentType());
                           System.out.println("yesss");
                       }
                       else {
                           payMethod.setText("Still in progress.");
                           System.out.println("none");
                       }
                   }
                }else {
                    payMethod.setText("Still in progress.");
                    System.out.println("none");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        display = FirebaseDatabase.getInstance().getReference("Booking").child(sId).child(bid);
        display.addListenerForSingleValueEvent(new ValueEventListener() {
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


                    if (booking.getbStatus().equalsIgnoreCase("Applying")){
                        pickup.setVisibility(View.GONE);
                        paidReceive.setVisibility(View.GONE);
                        tvCancel.setVisibility(View.GONE);
                        cancelReason.setVisibility(View.GONE);
                        approve.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                updateB = FirebaseDatabase.getInstance().getReference("Booking").child(sId).child(bid);
                                updateB2 = FirebaseDatabase.getInstance().getReference("Car").child(user.getUid()).child(cid).child("Booking").child(sId).child(bid);
                                updateB.child("bStatus").setValue("Accepted");
                                updateB2.child("bStatus").setValue("Accepted");
                                String memos = memo.getText().toString().trim();
                                updateB.child("memo").setValue(memos);
                                updateB2.child("memo").setValue(memos);
                                Intent intent2list = new Intent(CBookingDetailsInterface.this, CBookingListInterface.class);
                                startActivity(intent2list);
                                finish();
                            }
                        });

                        reject.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent intent2reject = new Intent(CBookingDetailsInterface.this, CBookingRejectInterface.class);
                                intent2reject.putExtra("bid", bid);
                                intent2reject.putExtra("sId", sId);
                                intent2reject.putExtra("cid", cid);
                                startActivity(intent2reject);
                            }
                        });
                    }
                    else if (booking.getbStatus().equalsIgnoreCase("Accepted")){
                        approve.setVisibility(View.GONE);
                        reject.setVisibility(View.GONE);
                        paidReceive.setVisibility(View.GONE);
                        tvCancel.setVisibility(View.GONE);
                        cancelReason.setVisibility(View.GONE);
                        pickup.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                updateB = FirebaseDatabase.getInstance().getReference("Booking").child(sId).child(bid);
                                updateB2 = FirebaseDatabase.getInstance().getReference("Car").child(user.getUid()).child(cid).child("Booking").child(sId).child(bid);
                                updateB.child("bStatus").setValue("Pick Up");
                                updateB2.child("bStatus").setValue("Pick Up");
                                Intent intent2list = new Intent(CBookingDetailsInterface.this, CBookingListInterface.class);
                                startActivity(intent2list);
                                finish();
                            }
                        });
                    }
                    else if (booking.getbStatus().equalsIgnoreCase("Pick Up") || booking.getbStatus().equalsIgnoreCase("Paid") || booking.getbStatus().equalsIgnoreCase("Rejected")){
                        approve.setVisibility(View.GONE);
                        reject.setVisibility(View.GONE);
                        paidReceive.setVisibility(View.GONE);
                        pickup.setVisibility(View.GONE);
                        tvCancel.setVisibility(View.GONE);
                        cancelReason.setVisibility(View.GONE);
                    }
                    else if (booking.getbStatus().equalsIgnoreCase("Cancelled")){
                        approve.setVisibility(View.GONE);
                        reject.setVisibility(View.GONE);
                        paidReceive.setVisibility(View.GONE);
                        pickup.setVisibility(View.GONE);
                        cancelReason.setText(booking.getbCancelReason());
                    }
                    else{
                        approve.setVisibility(View.GONE);
                        reject.setVisibility(View.GONE);
                        pickup.setVisibility(View.GONE);

                        paidReceive.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                updateB = FirebaseDatabase.getInstance().getReference("Booking").child(sId).child(bid);
                                updateB2 = FirebaseDatabase.getInstance().getReference("Car").child(user.getUid()).child(cid).child("Booking").child(sId).child(bid);
                                updateB.child("bStatus").setValue("Paid");
                                updateB2.child("bStatus").setValue("Paid");
                                DatabaseReference paymentRef = FirebaseDatabase.getInstance().getReference("Payment");
                                paymentRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()){
                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                                Payment paymentCheck = dataSnapshot.getValue(Payment.class);
                                                if (paymentCheck.getBid().equalsIgnoreCase(bid)){
                                                    paymentRef.child(paymentCheck.getPid()).child("paymentStatus").setValue("Paid With Cash");
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                                Intent intent2list = new Intent(CBookingDetailsInterface.this, CBookingListInterface.class);
                                startActivity(intent2list);
                                finish();
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
                        startActivity(new Intent(getApplicationContext(), CBookingListInterface.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(CBookingDetailsInterface.this, CarOwnerMainInterface.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.account:
                        startActivity(new Intent(CBookingDetailsInterface.this, CarOwnerAccountInterface.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

}