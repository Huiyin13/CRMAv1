package com.example.crmav1.ManagePayment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crmav1.ManageBooking.BookingFormInterface;
import com.example.crmav1.ManageBooking.BookingListInterface;
import com.example.crmav1.ManageBooking.SBookingDetailsInterface;
import com.example.crmav1.ManageLoginandRegistration.EmailVerificationInterface;
import com.example.crmav1.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashMap;

public class PaymentSelectionInterface extends AppCompatActivity {

    private TextView amount;
    private RadioButton cash, card;
    private Button payment;

    private String bid, fee, coId, cid;

    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_selection_interface);

//        amount = findViewById(R.id.amount);
//        cash = findViewById(R.id.cash);
//        card = findViewById(R.id.card);
//        payment = findViewById(R.id.pay);
//
//        user = FirebaseAuth.getInstance().getCurrentUser();
//        bid = getIntent().getStringExtra("bid");
//        fee = getIntent().getStringExtra("fee");
//        coId = getIntent().getStringExtra("coId");
//        cid = getIntent().getStringExtra("cid");
//        amount.setText("Your Total Payment is RM "+ fee );

//        payment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (cash.isChecked()){
//                    DatabaseReference cashref = FirebaseDatabase.getInstance().getReference("Payment");
//                    String pid = cashref.push().getKey();
//                    DatabaseReference pay = cashref.child(pid);
////                    DatabaseReference bookref = FirebaseDatabase.getInstance().getReference("Booking").child(user.getUid()).child(bid);
////                    DatabaseReference carref = FirebaseDatabase.getInstance().getReference("Car").child(coId).child(cid).child("Booking").child(user.getUid()).child(bid);
//
//                    HashMap<String,Object> hashMap = new HashMap<>();
//                    hashMap.put("pid", pid);
//                    hashMap.put("paymentStatus", "NOT YET PAY");
//                    hashMap.put("bid", bid);
//                    hashMap.put("paymentType", "Cash");
//
//                    pay.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void unused) {
//
//                            Toast.makeText(PaymentSelectionInterface.this,"Updated", Toast.LENGTH_SHORT).show();
//                            Intent intent2back = new Intent(PaymentSelectionInterface.this, SBookingDetailsInterface.class);
//                            intent2back.putExtra("coId", coId);
//                            intent2back.putExtra("bid", bid);
//                            startActivity(intent2back);
//
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(PaymentSelectionInterface.this, "Failed to update.", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//                else{
//
//                }
//            }
//        });


    }
}