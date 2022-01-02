package com.example.crmav1.ManageBooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.crmav1.ManageAccount.StudentAccountInterface;
import com.example.crmav1.ManageLoginandRegistration.StudentMainInterface;
import com.example.crmav1.Model.CarOwner;
import com.example.crmav1.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SCarOwnerProfileInterface extends AppCompatActivity {

    private TextView name, id, ic, phone, email;
    private ImageView icPhoto, idPhoto;
    private Button back;

    private String cid, bid, coId;
    private DatabaseReference carOwner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scar_owner_profile_interface);

        name = findViewById(R.id.coName);
        id = findViewById(R.id.coID);
        ic = findViewById(R.id.coIC);
        phone = findViewById(R.id.coPhone);
        email = findViewById(R.id.coEmail);
        icPhoto = findViewById(R.id.coICPhoto);
        idPhoto = findViewById(R.id.coIDPhoto);
        back = findViewById(R.id.back);

        cid = getIntent().getStringExtra("cid");
        bid = getIntent().getStringExtra("bid");
        coId = getIntent().getStringExtra("coId");

        carOwner = FirebaseDatabase.getInstance().getReference("Users").child(coId);

        carOwner.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    CarOwner co = snapshot.getValue(CarOwner.class);
                    name.setText(co.getCoName());
                    id.setText(co.getCoMatricID());
                    ic.setText(co.getCoIC());
                    phone.setText(co.getCoPhone());
                    email.setText(co.getCoEmail());

                    Picasso.get().load(co.getCoICUri()).into(icPhoto);
                    Picasso.get().load(co.getCoIDUri()).into(idPhoto);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2back = new Intent(SCarOwnerProfileInterface.this, SBookingDetailsInterface.class);
                intent2back.putExtra("coId", coId);
                intent2back.putExtra("bid", bid);
                intent2back.putExtra("cid", cid);
                startActivity(intent2back);
                finish();
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
                        startActivity(new Intent(SCarOwnerProfileInterface.this, StudentMainInterface.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.account:
                        startActivity(new Intent(SCarOwnerProfileInterface.this, StudentAccountInterface.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
}