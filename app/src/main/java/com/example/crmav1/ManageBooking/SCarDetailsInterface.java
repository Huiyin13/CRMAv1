package com.example.crmav1.ManageBooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.crmav1.Adapter.CarPhotoHolder;
import com.example.crmav1.ManageAccount.StudentAccountInterface;
import com.example.crmav1.ManageLoginandRegistration.StudentMainInterface;
import com.example.crmav1.Model.Car;
import com.example.crmav1.Model.cPhotoUri;
import com.example.crmav1.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SCarDetailsInterface extends AppCompatActivity {
    private TextView model, plate, person, type, fee, description;
    private RecyclerView photo;
    private Button book, cancel;

    private FirebaseDatabase db;
    private DatabaseReference carDBRef, carDBRef2, updateRef;
    private FirebaseUser user;

    private String cid, coId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scar_details_interface);

        model = findViewById(R.id.cModel);
        plate = findViewById(R.id.cPlate);
        person = findViewById(R.id.cPax);
        type = findViewById(R.id.cType);
        fee = findViewById(R.id.cFee);
        description = findViewById(R.id.description);
        cancel = findViewById(R.id.cancel);
        book = findViewById(R.id.book);

        photo = findViewById(R.id.photoListView);
        photo.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        photo.setLayoutManager(layoutManager);

        cid = getIntent().getStringExtra("cid");
        coId = getIntent().getStringExtra("coId");

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2list = new Intent(SCarDetailsInterface.this, SCarListInterface.class);
                intent2list.putExtra("cid", cid);
                intent2list.putExtra("coId", coId);
                startActivity(intent2list);
            }
        });

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2book = new Intent(SCarDetailsInterface.this, BookingFormInterface.class);
                intent2book.putExtra("cid", cid);
                intent2book.putExtra("coId", coId);
                startActivity(intent2book);
            }
        });

        carDBRef = FirebaseDatabase.getInstance().getReference("Car").child(coId).child(cid);
        carDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Car car = snapshot.getValue(Car.class);

                    model.setText(car.getcModel());
                    plate.setText(car.getcPlate());
                    person.setText(car.getcPerson());
                    type.setText(car.getcType());
                    fee.setText(car.getRentFee());
                    description.setText(car.getDescription());
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
                        startActivity(new Intent(SCarDetailsInterface.this, StudentMainInterface.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.account:
                        startActivity(new Intent(SCarDetailsInterface.this, StudentAccountInterface.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        carDBRef2 = FirebaseDatabase.getInstance().getReference("Car").child(coId).child(cid).child("cPhotoUri");
        FirebaseRecyclerOptions<cPhotoUri> carPhoto =
                new  FirebaseRecyclerOptions.Builder<cPhotoUri>()
                        .setQuery(carDBRef2, cPhotoUri.class)
                        .build();
        FirebaseRecyclerAdapter<cPhotoUri, CarPhotoHolder> photoAdapter = new FirebaseRecyclerAdapter<cPhotoUri, CarPhotoHolder>(carPhoto) {
            @Override
            protected void onBindViewHolder(@NonNull CarPhotoHolder holder, int position, @NonNull cPhotoUri model) {
                Picasso.get().load(model.getImageLink()).into(holder.cPhoto);
            }

            @NonNull
            @Override
            public CarPhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_adapter, parent, false);
                CarPhotoHolder holder = new CarPhotoHolder(view);
                return holder;
            }
        };
        photo.setAdapter(photoAdapter);
        photoAdapter.startListening();
    }
}