package com.example.crmav1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.crmav1.ManageAccount.CarOwnerListInterface;
import com.example.crmav1.ManageBooking.SBookingDetailsInterface;
import com.example.crmav1.ManageBooking.SCarOwnerProfileInterface;
import com.example.crmav1.ManageCar.ACarDetailsInterface;
import com.example.crmav1.ManageCar.ACarListInterface;
import com.example.crmav1.ManageLoginandRegistration.AdminMainInterface;
import com.example.crmav1.Model.CarOwner;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ACarOwnerDetailsInterface extends AppCompatActivity {

    private TextView name, id, ic, phone, email, campus;
    private ImageView icPhoto, idPhoto;
    private Button back;

    private String cid, coId;
    private DatabaseReference carOwner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acar_owner_details_interface);

        name = findViewById(R.id.coName);
        id = findViewById(R.id.coID);
        ic = findViewById(R.id.coIC);
        phone = findViewById(R.id.coPhone);
        email = findViewById(R.id.coEmail);
        icPhoto = findViewById(R.id.coICPhoto);
        idPhoto = findViewById(R.id.coIDPhoto);
        back = findViewById(R.id.back);
        campus = findViewById(R.id.coCampus);

        cid = getIntent().getStringExtra("cid");
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
                    campus.setText(co.getCoCampus());

                    Picasso.get().load(co.getCoICUri()).into(icPhoto);
                    Picasso.get().load(co.getCoIDUri()).into(idPhoto);

                    phone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent callIntent = new Intent(Intent.ACTION_DIAL);
                            callIntent.setData(Uri.parse("tel:" + co.getCoPhone()));//change the number
                            startActivity(callIntent);
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2back = new Intent(ACarOwnerDetailsInterface.this, ACarDetailsInterface.class);
                intent2back.putExtra("coId", coId);
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