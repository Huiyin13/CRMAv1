package com.example.crmav1.ManageBooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.crmav1.ManageAccount.CarOwnerAccountInterface;
import com.example.crmav1.ManageLoginandRegistration.CarOwnerMainInterface;
import com.example.crmav1.Model.Car;
import com.example.crmav1.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CCheckCarDetailsInterface extends AppCompatActivity {

    private TextView model, plate, person, type;
    private Button back;

    private DatabaseReference carDBRef;
    private FirebaseUser user;

    private String cid, sId, bid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ccheck_car_details_interface);

        model = findViewById(R.id.cModel);
        plate = findViewById(R.id.cPlate);
        back = findViewById(R.id.back);
        person = findViewById(R.id.cPax);
        type = findViewById(R.id.cType);

        bid = getIntent().getStringExtra("bid");
        sId = getIntent().getStringExtra("sId");
        cid = getIntent().getStringExtra("cid");

        user = FirebaseAuth.getInstance().getCurrentUser();
        carDBRef = FirebaseDatabase.getInstance().getReference("Car").child(user.getUid()).child(cid);
        carDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Car car = snapshot.getValue(Car.class);

                    model.setText(car.getcModel());
                    plate.setText(car.getcPlate());
                    person.setText(car.getcPerson());
                    type.setText(car.getcType());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2view = new Intent(CCheckCarDetailsInterface.this, CBookingDetailsInterface.class);
                intent2view.putExtra("sId", sId);
                intent2view.putExtra("bid", bid);
                intent2view.putExtra("cid", cid);
                startActivity(intent2view);
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
                        startActivity(new Intent(CCheckCarDetailsInterface.this, CarOwnerMainInterface.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.account:
                        startActivity(new Intent(CCheckCarDetailsInterface.this, CarOwnerAccountInterface.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

    }
}