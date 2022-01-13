package com.example.crmav1.ManageBooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crmav1.ManageAccount.StudentAccountInterface;
import com.example.crmav1.ManageChat.ChatInterface;
import com.example.crmav1.ManageLoginandRegistration.StudentMainInterface;
import com.example.crmav1.ManagePayment.PaymentSelectionInterface;
import com.example.crmav1.Model.Booking;
import com.example.crmav1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SBookingDetailsInterface extends AppCompatActivity {

    private TextView timeF, timeT, dateF, dateT, status, payment, memo, reject, tvMemo, tvReject, tvCancel,reasonCancel;
    private ImageView chat;
    private Button pay, viewCo, cancel, complete, cancel2, viewCar;

    private FirebaseUser user;
    private DatabaseReference display, updateB, updateB2, paymentB;

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
        viewCar = findViewById(R.id.viewCar);
        cancel = findViewById(R.id.cancel);
        cancel2 = findViewById(R.id.cancel2);
        complete = findViewById(R.id.complete);
        reject = findViewById(R.id.reason);
        tvMemo = findViewById(R.id.tvMemo);
        tvReject = findViewById(R.id.tvReject);
        tvCancel = findViewById(R.id.tvcancel);
        reasonCancel = findViewById(R.id.cancelReason);

        coId = getIntent().getStringExtra("coId");
        bid = getIntent().getStringExtra("bid");

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2chat = new Intent(SBookingDetailsInterface.this, ChatInterface.class);
                intent2chat.putExtra("uid", coId);
                intent2chat.putExtra("bid", bid);
                intent2chat.putExtra("cid", cid);
                startActivity(intent2chat);
            }
        });

        viewCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2view = new Intent(SBookingDetailsInterface.this, SCheckCarDetailsInterface.class);
                intent2view.putExtra("coId", coId);
                intent2view.putExtra("bid", bid);
                intent2view.putExtra("cid", cid);
                startActivity(intent2view);

            }
        });

        viewCo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2view = new Intent(SBookingDetailsInterface.this, SCarOwnerProfileInterface.class);
                intent2view.putExtra("coId", coId);
                intent2view.putExtra("bid", bid);
                intent2view.putExtra("cid", cid);
                startActivity(intent2view);

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

                    if (booking.getbStatus().equalsIgnoreCase("Applying") ){
                        complete.setVisibility(View.GONE);
                        pay.setVisibility(View.GONE);
                        tvReject.setVisibility(View.GONE);
                        reject.setVisibility(View.GONE);
                        tvCancel.setVisibility(View.GONE);
                        reasonCancel.setVisibility(View.GONE);
                        cancel2.setVisibility(View.GONE);
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AlertDialog deleteAlert = confirmation();
                                deleteAlert.show();
                            }
                        });
                    }
                    else if (booking.getbStatus().equalsIgnoreCase("Cancelled")){
                        complete.setVisibility(View.GONE);
                        pay.setVisibility(View.GONE);
                        tvReject.setVisibility(View.GONE);
                        reject.setVisibility(View.GONE);
                        cancel2.setVisibility(View.GONE);
                        cancel.setVisibility(View.GONE);
                        reasonCancel.setText(booking.getbCancelReason());

                    }
                    else if (booking.getbStatus().equalsIgnoreCase("Accepted")){
                        complete.setVisibility(View.GONE);
                        pay.setVisibility(View.GONE);
                        tvReject.setVisibility(View.GONE);
                        reject.setVisibility(View.GONE);
                        tvCancel.setVisibility(View.GONE);
                        reasonCancel.setVisibility(View.GONE);
                        cancel.setVisibility(View.GONE);
                        cancel2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent2cancel = new Intent(SBookingDetailsInterface.this, SBookingCancelReasonInterface.class);
                                intent2cancel.putExtra("bid", bid);
                                intent2cancel.putExtra("coId", coId);
                                intent2cancel.putExtra("cid", cid);
                                startActivity(intent2cancel);
                                finish();
                            }
                        });
                    }
                    else if (booking.getbStatus().equalsIgnoreCase("Paid")){
                        cancel.setVisibility(View.GONE);
                        pay.setVisibility(View.GONE);
                        tvReject.setVisibility(View.GONE);
                        reject.setVisibility(View.GONE);
                        tvCancel.setVisibility(View.GONE);
                        reasonCancel.setVisibility(View.GONE);
                        cancel2.setVisibility(View.GONE);

                        complete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                updateB = FirebaseDatabase.getInstance().getReference("Booking").child(user.getUid()).child(bid);
                                updateB2 = FirebaseDatabase.getInstance().getReference("Car").child(coId).child(cid).child("Booking").child(user.getUid()).child(bid);
//                                paymentB = FirebaseDatabase.getInstance().getReference("Payment");
//                                paymentB.addValueEventListener(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                        if (snapshot.exists()){
//                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
//                                                Payment paid = dataSnapshot.getValue(Payment.class);
//                                                status.setText(paid.getPaymentStatus());
//                                                System.out.println(paid.getPaymentStatus());
//                                            }
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError error) {
//
//                                    }
//                                });
                                updateB.child("bStatus").setValue("Completed");
                                updateB2.child("bStatus").setValue("Completed");
                                Intent intent2list = new Intent(SBookingDetailsInterface.this, BookingListInterface.class);
                                startActivity(intent2list);
                                finish();
                            }
                        });
                    }
                    else  if (booking.getbStatus().equalsIgnoreCase("Pick Up")){
                        cancel.setVisibility(View.GONE);
                        complete.setVisibility(View.GONE);
                        tvReject.setVisibility(View.GONE);
                        reject.setVisibility(View.GONE);
                        tvCancel.setVisibility(View.GONE);
                        reasonCancel.setVisibility(View.GONE);
                        cancel2.setVisibility(View.GONE);
                        pay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent intent2payment = new Intent(SBookingDetailsInterface.this, PaymentSelectionInterface.class);
                                intent2payment.putExtra("bid", bid);
                                intent2payment.putExtra("fee", fee);
                                intent2payment.putExtra("coId", coId);
                                intent2payment.putExtra("cid", cid);
                                startActivity(intent2payment);
                                finish();
                            }
                        });
                    }
                    else if (booking.getbStatus().equalsIgnoreCase("Not yet receive cash payment.") || booking.getbStatus().equalsIgnoreCase("Completed")){
                        cancel.setVisibility(View.GONE);
                        complete.setVisibility(View.GONE);
                        tvReject.setVisibility(View.GONE);
                        reject.setVisibility(View.GONE);
                        pay.setVisibility(View.GONE);
                        tvCancel.setVisibility(View.GONE);
                        reasonCancel.setVisibility(View.GONE);
                        cancel2.setVisibility(View.GONE);

                    }
                    else {
                        complete.setVisibility(View.GONE);
                        pay.setVisibility(View.GONE);
                        tvMemo.setVisibility(View.GONE);
                        memo.setVisibility(View.GONE);
                        tvCancel.setVisibility(View.GONE);
                        reasonCancel.setVisibility(View.GONE);
                        cancel2.setVisibility(View.GONE);
                        reject.setText(booking.getbRejectReason());
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AlertDialog deleteAlert = confirmation();
                                deleteAlert.show();
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

    private AlertDialog confirmation() {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                // set message, title, and icon
                .setTitle("Sure?")
                .setMessage("Do you want to cancel this booking?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        DatabaseReference deleteB = FirebaseDatabase.getInstance().getReference("Booking").child(user.getUid()).child(bid);
                        DatabaseReference deleteB2 = FirebaseDatabase.getInstance().getReference("Car").child(coId).child(cid).child("Booking").child(user.getUid()).child(bid);
                        deleteB.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                deleteB2.removeValue();
                                Toast.makeText(SBookingDetailsInterface.this, "This booking is cancelled.", Toast.LENGTH_SHORT).show();
                                Intent intent2delete = new Intent(SBookingDetailsInterface.this, BookingListInterface.class);
                                startActivity(intent2delete);
                                finish();
                            }
                        });

                    }

                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();

        return myQuittingDialogBox;
    }
}