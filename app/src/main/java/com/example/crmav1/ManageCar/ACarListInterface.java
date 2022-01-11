package com.example.crmav1.ManageCar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.crmav1.Adapter.CarListAdapter;
import com.example.crmav1.Adapter.Holder;
import com.example.crmav1.ManageAccount.CarOwnerAccountInterface;
import com.example.crmav1.ManageAccount.CarOwnerListInterface;
import com.example.crmav1.ManageAccount.CarOwnerViewInterface;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ACarListInterface extends AppCompatActivity implements CarListAdapter.ItemClickListener {
    private RecyclerView recyclerView;
    private DatabaseReference carRef;
    private ArrayList<Car> carList;
    private CarListAdapter adapter;
    private String cid, uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acar_list_interface);

        recyclerView = findViewById(R.id.carListView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        carList = new ArrayList<>();

        adapter = new CarListAdapter(this, carList, this);
        recyclerView.setAdapter(adapter);

        carRef = FirebaseDatabase.getInstance().getReference("Car");

        carRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        String key = dataSnapshot.getKey();
                        for (DataSnapshot get : dataSnapshot.getChildren()){
                            Car car = get.getValue(Car.class);
                            cid = car.getCid();
                            System.out.println(car.getcStatus());
                            if (car.getcStatus().equalsIgnoreCase("Free") || car.getcStatus().equalsIgnoreCase("Hide")){
                                carList.add(car);
                            }
                        }
                    }
                    Toast.makeText(getApplicationContext(), "Data Found", Toast.LENGTH_LONG).show();
                    adapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(getApplicationContext(),"No Data Found", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),error.getDetails(), Toast.LENGTH_LONG).show();
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