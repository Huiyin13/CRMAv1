package com.example.crmav1.ManageAccount;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crmav1.ManageBooking.CBookingListInterface;
import com.example.crmav1.ManageLoginandRegistration.CarOwnerMainInterface;
import com.example.crmav1.ManageLoginandRegistration.LoginInterface;
import com.example.crmav1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class CarOwnerProfileInterface extends AppCompatActivity {

    private EditText name, phone;
    private TextView ic, email, id;
    private RadioButton gambang, pekan;
    private Button update, delete;
    private ImageView icPhoto, idPhoto;

    private FirebaseDatabase db;
    private DatabaseReference stdDBRef;
    private FirebaseAuth auth;
    private Uri icFile, idFile, licenceFile;

    private String uid, nameCO, emailCO, phoneCO, campusCO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_owner_profile_interface);

        this.setTitle("Edit Profile");

        name = findViewById(R.id.coName);
        ic = findViewById(R.id.coIC);
        id = findViewById(R.id.coID);
        email = findViewById(R.id.coEmail);
        phone = findViewById(R.id.coPhone);
        icPhoto = findViewById(R.id.coICPhoto);
        idPhoto = findViewById(R.id.coIDPhoto);
        gambang = findViewById(R.id.rbGambang);
        pekan = findViewById(R.id.rbPekan);
        update = findViewById(R.id.coUpdate);
        delete = findViewById(R.id.coDelete);

        name.setText(getIntent().getStringExtra("nameCo").toString());
        ic.setText(getIntent().getStringExtra("icCo").toString());
        id.setText(getIntent().getStringExtra("matricIDCo").toString());
        email.setText(getIntent().getStringExtra("emailCo").toString());
        phone.setText(getIntent().getStringExtra("phoneCo").toString());
        if (getIntent().getStringExtra("campusCo").equals("Gambang")){
            gambang.setChecked(true);
        }
        else{
            pekan.setChecked(true);
        }

        String ic = getIntent().getStringExtra("ICUriCo");
        icFile = Uri.parse(ic);
        String id = getIntent().getStringExtra("IDUriCo");
        idFile = Uri.parse(id);

        try{
            Picasso.get().load(icFile).placeholder(R.drawable.add_image).into(icPhoto);
        }
        catch (Exception e) {

        }
        try{
            Picasso.get().load(idFile).placeholder(R.drawable.add_image).into(idPhoto);
        }
        catch (Exception e) {

        }

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
                        startActivity(new Intent(CarOwnerProfileInterface.this, CarOwnerMainInterface.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.account:
                        startActivity(new Intent(CarOwnerProfileInterface.this, CarOwnerAccountInterface.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        stdDBRef = db.getReference("Users");

        stdDBRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (name.getText().length()!=0 && phone.getText().length()!=0){
                            uid = auth.getUid();
                            nameCO = name.getText().toString();
                            phoneCO = phone.getText().toString();

                            if (gambang.isChecked()){
                                campusCO = "Gambang";
                            }
                            else if (pekan.isChecked()){
                                campusCO = "Pekan";
                            }

                            stdDBRef.child(uid).child("coName").setValue(nameCO);
                            stdDBRef.child(uid).child("coPhone").setValue(phoneCO);
                            stdDBRef.child(uid).child("coCampus").setValue(campusCO);
                            Toast.makeText(CarOwnerProfileInterface.this, "Profile Update Successful!", Toast.LENGTH_SHORT).show() ;
                            Intent intent2done = new Intent(CarOwnerProfileInterface.this, CarOwnerAccountInterface.class);
                            startActivity(intent2done);
                        }else {
                            Toast.makeText(CarOwnerProfileInterface.this, "All field must be filled!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog deleteAlert = confirmation();
                deleteAlert.show();
            }
        });
    }

    private AlertDialog confirmation(){
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                // set message, title, and icon
                .setTitle("THINK TWICE!!")
                .setMessage("Do you want to Delete Your account?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        FirebaseUser coID = FirebaseAuth.getInstance().getCurrentUser();
                        coID.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                String userID = auth.getUid();
                                DatabaseReference ID = FirebaseDatabase.getInstance().getReference("Users");
                                ID.child(userID).removeValue();
                                Toast.makeText(CarOwnerProfileInterface.this, "Your account is deleted. Please REGISTER again for login!!", Toast.LENGTH_SHORT).show();
                                Intent intent2delete = new Intent(CarOwnerProfileInterface.this, LoginInterface.class);
                                startActivity(intent2delete);
                            }
                        });

                    }

                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();

        return myQuittingDialogBox;
    }
}