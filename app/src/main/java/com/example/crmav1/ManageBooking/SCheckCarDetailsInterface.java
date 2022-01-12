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
import android.widget.ProgressBar;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class SCheckCarDetailsInterface extends AppCompatActivity {

    private TextView model, plate, person, type, fee, description;
    private RecyclerView photo;
    private Button back;

    private FirebaseDatabase db;
    private DatabaseReference carDBRef, carDBRef2, updateRef;
    private FirebaseUser user;

    private String cid, coId, coName, feetointent, bid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheck_car_details_interface);

        model = findViewById(R.id.cModel);
        plate = findViewById(R.id.cPlate);
        person = findViewById(R.id.cPax);
        type = findViewById(R.id.cType);
        fee = findViewById(R.id.cFee);
        description = findViewById(R.id.description);
        back = findViewById(R.id.back);

        photo = findViewById(R.id.photoListView);
        photo.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        photo.setLayoutManager(layoutManager);

        cid = getIntent().getStringExtra("cid");
        coId = getIntent().getStringExtra("coId");
        bid = getIntent().getStringExtra("bid");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2view = new Intent(SCheckCarDetailsInterface.this, SBookingDetailsInterface.class);
                intent2view.putExtra("coId", coId);
                intent2view.putExtra("bid", bid);
                intent2view.putExtra("cid", cid);
                startActivity(intent2view);
                finish();
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
                    feetointent = car.getRentFee();
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
                        startActivity(new Intent(SCheckCarDetailsInterface.this, StudentMainInterface.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.account:
                        startActivity(new Intent(SCheckCarDetailsInterface.this, StudentAccountInterface.class));
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
                holder.progressBar.setVisibility(View.VISIBLE);
                Picasso.get().load(model.getImageLink()).into(holder.cPhoto, new ImageLoadedCallback(holder.progressBar){
                    @Override
                    public void onSuccess() {
                        this.progressBar.setVisibility(View.GONE);
                    }
                });
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

    private class ImageLoadedCallback implements Callback {
        ProgressBar progressBar;

        public ImageLoadedCallback(ProgressBar progressBar) {
            this.progressBar = progressBar;
        }

        @Override
        public void onSuccess() {

        }

        @Override
        public void onError(Exception e) {

        }
    }
}