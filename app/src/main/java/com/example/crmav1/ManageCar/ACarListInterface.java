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

import com.example.crmav1.Adapter.CarListAdapter;
import com.example.crmav1.Adapter.Holder;
import com.example.crmav1.ManageAccount.CarOwnerAccountInterface;
import com.example.crmav1.ManageAccount.CarOwnerListInterface;
import com.example.crmav1.ManageBooking.CBookingListInterface;
import com.example.crmav1.ManageLoginandRegistration.AdminMainInterface;
import com.example.crmav1.ManageLoginandRegistration.CarOwnerMainInterface;
import com.example.crmav1.Model.Car;
import com.example.crmav1.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ACarListInterface extends AppCompatActivity implements CarListAdapter.ItemClickListener {
    private RecyclerView recyclerView;
    private DatabaseReference carRef;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Car> carOption =
                new FirebaseRecyclerOptions.Builder<Car>()
                        .setQuery(carRef, Car.class)
                        .build();
        FirebaseRecyclerAdapter<Car, Holder> carAdapter = new FirebaseRecyclerAdapter<Car, Holder>(carOption) {
            @Override
            protected void onBindViewHolder(@NonNull Holder holder, int position, @NonNull Car model) {
                holder.cModel.setText(model.getcModel());
                holder.cPlate.setText(model.getcPlate());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent2view = new Intent(ACarListInterface.this, ACarDetailsInterface.class);
                        intent2view.putExtra("cid", model.getCid());
                        startActivity(intent2view);
                    }
                });
            }

            @NonNull
            @Override
            public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_list_adapter, parent, false);
                Holder holder = new Holder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(carAdapter);
        carAdapter.startListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acar_list_interface);

        recyclerView = findViewById(R.id.carListView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        carRef = FirebaseDatabase.getInstance().getReference("Car");


        //bottom nav
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.car:
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



    @Override
    public void onItemClick(int position) {

    }
}