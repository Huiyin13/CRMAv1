package com.example.crmav1.ManageBooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crmav1.Adapter.CarListAdapter;
import com.example.crmav1.Adapter.SCarListAdapter;
import com.example.crmav1.ManageAccount.StudentAccountInterface;
import com.example.crmav1.ManageCar.ACarDetailsInterface;
import com.example.crmav1.ManageLoginandRegistration.StudentMainInterface;
import com.example.crmav1.Model.Car;
import com.example.crmav1.R;
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

public class SCarListInterface extends AppCompatActivity implements SCarListAdapter.ItemClickListener {

    private RecyclerView recyclerView;
    private DatabaseReference carRef;
    private ArrayList<Car> carList;
    private SCarListAdapter adapter;
    private String sId, coId, coName, cid;
    private TextView tv;
    private Button viewBook;
    private FirebaseUser auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scar_list_interface);

        recyclerView = findViewById(R.id.carListView);
        tv = findViewById(R.id.tvCarList);
        viewBook = findViewById(R.id.viewBook);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        carList = new ArrayList<>();

        adapter = new SCarListAdapter(this, carList, this);
        recyclerView.setAdapter(adapter);

        auth = FirebaseAuth.getInstance().getCurrentUser();
        sId = auth.getUid();

        viewBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2view = new Intent(SCarListInterface.this, BookingListInterface.class);
                intent2view.putExtra("sId", sId);
                startActivity(intent2view);
            }
        });

        coId = getIntent().getStringExtra("coId");
        coName = getIntent().getStringExtra("coName");

        carRef = FirebaseDatabase.getInstance().getReference("Car");
        Query query = FirebaseDatabase.getInstance().getReference("Car").child(coId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Car car = dataSnapshot.getValue(Car.class);
                        cid = car.getCid();
                        if (car.getcStatus().equalsIgnoreCase("Free")){
                            tv.setText("Car List of " + coName);
                            carList.add(car);
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
                        startActivity(new Intent(SCarListInterface.this, StudentMainInterface.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.account:
                        startActivity(new Intent(SCarListInterface.this, StudentAccountInterface.class));
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