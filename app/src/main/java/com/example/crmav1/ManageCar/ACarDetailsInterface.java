package com.example.crmav1.ManageCar;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crmav1.ACarOwnerDetailsInterface;
import com.example.crmav1.Adapter.CarPhotoHolder;
import com.example.crmav1.ManageAccount.CarOwnerListInterface;
import com.example.crmav1.ManageAccount.CarOwnerRejectInterface;
import com.example.crmav1.ManageBooking.SBookingDetailsInterface;
import com.example.crmav1.ManageBooking.SCarOwnerProfileInterface;
import com.example.crmav1.ManageLoginandRegistration.AdminMainInterface;
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

public class ACarDetailsInterface extends AppCompatActivity {
    private TextView model, plate, person, type, reason, fee, description, status, tvReason;
    private ImageView sticker;
    private RecyclerView photo;
    private Button unlist, list, cancel, cancel2, view;

    private FirebaseDatabase db;
    private DatabaseReference carDBRef, carDBRef2, updateRef;
    private FirebaseUser user;

    private String cid, uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acar_details_interface);

        model = findViewById(R.id.cModel);
        plate = findViewById(R.id.cPlate);
        person = findViewById(R.id.cPax);
        type = findViewById(R.id.cType);
        reason = findViewById(R.id.cHideReason);
        fee = findViewById(R.id.cFee);
        description = findViewById(R.id.description);
        status = findViewById(R.id.cStatus);
        tvReason = findViewById(R.id.tvReason);
        sticker = findViewById(R.id.cSticker);
        unlist = findViewById(R.id.cHide);
        list = findViewById(R.id.cUnhide);
        cancel = findViewById(R.id.cancel);
        cancel2 = findViewById(R.id.cancel2);
        view = findViewById(R.id.viewC);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2view = new Intent(ACarDetailsInterface.this, ACarOwnerDetailsInterface.class);
                intent2view.putExtra("coId", uid);
                intent2view.putExtra("cid", cid);
                startActivity(intent2view);
            }
        });

        photo = findViewById(R.id.photoListView);
        photo.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        photo.setLayoutManager(layoutManager);


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

        cid = getIntent().getStringExtra("cid");
        uid = getIntent().getStringExtra("coId");
        carDBRef = FirebaseDatabase.getInstance().getReference("Car").child(uid).child(cid);
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                carDBRef.child("cStatus").setValue("Free");
                carDBRef.child("cHideReason").setValue(" ");
                Toast.makeText(ACarDetailsInterface.this, "You have unlist the car.", Toast.LENGTH_SHORT).show() ;
                Intent intent2back = new Intent(ACarDetailsInterface.this, ACarListInterface.class);
                startActivity(intent2back);
            }
        });
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
                    status.setText(car.getcStatus());

                    Picasso.get().load(car.getcSticker()).into(sticker);

                    if (car.getcStatus().equalsIgnoreCase("Hide")) {
                        tvReason.setVisibility(View.VISIBLE);
                        reason.setVisibility(View.VISIBLE);
                        list.setVisibility(View.VISIBLE);
                        cancel2.setVisibility(View.VISIBLE);
                        unlist.setVisibility(View.GONE);
                        cancel.setVisibility(View.GONE);
                        reason.setText(car.getcHideReason());

                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent2back = new Intent(ACarDetailsInterface.this, ACarListInterface.class);
                                startActivity(intent2back);
                            }
                        });



                    } else {
                        reason.setVisibility(View.GONE);
                        tvReason.setVisibility(View.GONE);
                        list.setVisibility(View.GONE);
                        cancel2.setVisibility(View.GONE);
                        unlist.setVisibility(View.VISIBLE);
                        cancel.setVisibility(View.VISIBLE);

                        cancel2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent2back = new Intent(ACarDetailsInterface.this, ACarListInterface.class);
                                startActivity(intent2back);
                            }
                        });

                        unlist.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent2hide = new Intent(ACarDetailsInterface.this, ACarHideReasonInterface.class);
                                intent2hide.putExtra("uid", uid);
                                intent2hide.putExtra("cid", cid);
                                startActivity(intent2hide);
                            }
                        });
                    }

                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        carDBRef2 = FirebaseDatabase.getInstance().getReference("Car").child(uid).child(cid).child("cPhotoUri");
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