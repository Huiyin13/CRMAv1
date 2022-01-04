package com.example.crmav1.ManageAccount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.crmav1.Adapter.CSFaqAdapter;
import com.example.crmav1.Adapter.CarListAdapter;
import com.example.crmav1.ManageBooking.CBookingListInterface;
import com.example.crmav1.ManageLoginandRegistration.CarOwnerMainInterface;
import com.example.crmav1.Model.Car;
import com.example.crmav1.Model.FAQ;
import com.example.crmav1.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CViewFaqInterface extends AppCompatActivity implements CSFaqAdapter.ItemClickListener {

    private RecyclerView recyclerView;
    private DatabaseReference faqRef;
    private ArrayList<FAQ> faqList;
    private CSFaqAdapter adapter;
    private Button back;
    private String name, email, status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cview_faq_interface);

        recyclerView = findViewById(R.id.faqListView);
        back = findViewById(R.id.back);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        faqList = new ArrayList<>();

        adapter = new CSFaqAdapter(this, faqList);
        recyclerView.setAdapter(adapter);

        faqRef = FirebaseDatabase.getInstance().getReference("Faq");

        faqRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        FAQ faq1 = dataSnapshot.getValue(FAQ.class);
                        if (faq1.getUserType().equals("Car Owner")){
                            faqList.add(faq1);
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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2back = new Intent(CViewFaqInterface.this, CarOwnerAccountInterface.class);
                startActivity(intent2back);
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
                        startActivity(new Intent(CViewFaqInterface.this, CarOwnerMainInterface.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.account:
                        startActivity(new Intent(CViewFaqInterface.this, CarOwnerAccountInterface.class));
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